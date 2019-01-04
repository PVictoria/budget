package politech.budget.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import politech.budget.builder.OperationBuilder;
import politech.budget.builder.OperationGetBuilder;
import politech.budget.dto.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class Dao {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OperationsRepository operationsRepository;
    private final BalanceRepository balanceRepository;
    private final OperationBuilder operationBuilder;
    private final OperationGetBuilder operationGetBuilder;

    public User getUser(Integer id) {
        return userRepository.findUserById(id);
    }

    public User getUser(User user) {
        String name = user.getName();
        String password = user.getPassword();
        return userRepository.findUserByNameAndPassword(name, password);
    }

    public User getUserByName(String userName) {
        return userRepository.findUserByName(userName);
    }

    @Transactional
    public User postUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<Article> getArticles() {
        return articleRepository.getAll();
    }

    public Article getArticlesByName(String name) {
        return articleRepository.findArticleByName(name);
    }

    @Transactional
    public Article postArticle(Article article) {
        return articleRepository.saveAndFlush(article);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteArticle(String name) {
        Article article = getArticlesByName(name);
        articleRepository.delete(article);
        articleRepository.flush();
    }

    public List<OperationGet> findOperationsByUserId(Integer userId) {
        List<Operation> operations = operationsRepository.findOperationsByUserId(userId);
        return getOperationGet(operations);
    }

    private List<OperationGet> getOperationGet(List<Operation> operations) {
        List<Integer> articleIds = operations.stream()
                .mapToInt(Operation::getArticleId).boxed().collect(Collectors.toList());
        List<Article> articles = new ArrayList<>();
        articleIds.forEach(id -> articles.add(articleRepository.findById(id).get()));
        return operationGetBuilder.buildList(operations, articles);
    }

    public List<OperationGet> findOperationsByUserIdAndArticle(Integer userId, String articleName) {
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
        return getOperationGet(operations);
    }

    public List<OperationGet> findOperationsByUserIdAndCreationTime(Integer userId, Date date) {
        int lastDate = getLastDate(date);
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(lastDate);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(userId, date1, date2);
        return getOperationGet(operations);
    }

    public List<OperationGet> findOperationsByUserIdAndArticleIdAndCreationTime(Integer userId, String articleName, Date date) {
        int lastDate = getLastDate(date);
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(lastDate);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAAndArticleIdAndCreateDate(userId, articleId, date1, date2);
        return getOperationGet(operations);
    }

    private int getLastDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDay());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Transactional
    public Operation postOperation(OperationPost operationPost) {
        Article article = articleRepository.findArticleByName(operationPost.getArticleName());
        Operation operation = operationBuilder.build(operationPost, article);
        return operationsRepository.saveAndFlush(operation);
    }

    public void deleteOperation(Integer operationId) {
        operationsRepository.deleteById(operationId);
    }

    public List<Balance> findBalanceByUserId(Integer userId) {
        return balanceRepository.findBalanceByUserId(userId);
    }

    public List<Balance> findBalanceByUserName(Integer userId) {
        List<Balance> balanceByUserId = balanceRepository.findBalanceByUserId(userId);
        balanceByUserId.sort(Comparator.comparing(Balance::getId).reversed());
        return balanceByUserId;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Balance postBalance(Balance balance) {

        Date date = balance.getCreateDate();
        int lastDate = getLastDate(date);
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(lastDate);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(balance.getUserId(), date1, date2);
        int credit = 0;
        int debit = 0;
        for (Operation operation : operations) {
            credit += (nonNull(operation.getCredit()) ? operation.getCredit() : 0);
            debit += (nonNull(operation.getDebit()) ? operation.getDebit() : 0);
        }

        balance.setCreateDate(new Timestamp(date2.getTime()));
        balance.setCredit(credit);
        balance.setDebit(debit);
        balance.setAmount(debit - credit);

        Balance balance1 = balanceRepository.saveAndFlush(balance);
        operations.forEach(operation -> operationsRepository.updateBalance(operation.getId(), balance1.getId()));
        return balance1;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBalance(Integer userId) {
        Balance balance = balanceRepository.getLastBalance(userId);
        operationsRepository.deleteBalance(balance.getId());
        balanceRepository.delete(balance);
    }

    public List<BarChart> getBarChartStatistics(Integer userId, String time) {
        List<BarChart> barChartList = new ArrayList<>();
        List<Balance> balanceByUserId = balanceRepository.findBalanceByUserId(userId);
        if (time.equals("allTime")) {
            balanceByUserId.forEach(balance -> {
                BarChart barChart = buildBarChart(balance);
                barChartList.add(barChart);
            });
        } else if (time.equals("year")) {
            balanceByUserId.stream()
                    .filter(balance -> (LocalDateTime.now().getYear() == new Date(balance.getCreateDate().getTime()).getYear() + 1900))
                    .forEach(balance -> {
                        BarChart barChart = buildBarChart(balance);
                        barChartList.add(barChart);
                    });
        }
        return barChartList;
    }

    public List<LineChart> getLineChartStatistics(Integer userId, String time, String articleName) {
        List<LineChart> lineChartList = new ArrayList<>();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operationsByUserIdAndArticleId = operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
        switch (time) {
            case "allTime":
                operationsByUserIdAndArticleId.forEach(operation -> {
                    LineChart lineChart = buildLineChart(operation);
                    lineChartList.add(lineChart);
                });
                break;
            case "year":
                operationsByUserIdAndArticleId.stream()
                        .filter(operation -> (LocalDateTime.now().getYear() == new Date(operation.getCreateDate().getTime()).getYear() + 1900))
                        .forEach(operation -> {
                            LineChart lineChart = buildLineChart(operation);
                            lineChartList.add(lineChart);
                        });
                break;
            case "month":
                operationsByUserIdAndArticleId.stream()
                        .filter(operation -> (LocalDateTime.now().getYear() == new Date(operation.getCreateDate().getTime()).getYear() + 1900))
                        .filter(operation -> (LocalDateTime.now().getMonth().getValue() == operation.getCreateDate().getMonth()))
                        .forEach(operation -> {
                            LineChart lineChart = buildLineChart(operation);
                            lineChartList.add(lineChart);
                        });
                break;
        }
        return lineChartList;
    }

    private LineChart buildLineChart(Operation operation) {
        LineChart lineChart = new LineChart();
        lineChart.setXDate(operation.getCreateDate().toLocalDateTime().toLocalDate().toString());
        lineChart.setCredit(nonNull(operation.getCredit()) ? operation.getCredit() : 0);
        lineChart.setDebit(operation.getDebit());
        return lineChart;
    }

    private BarChart buildBarChart(Balance balance) {
        BarChart barChart = new BarChart();
        barChart.setText(balance.getCreateDate().toString().substring(0, 10));
        barChart.setCredit(balance.getCredit());
        barChart.setDebit(balance.getDebit());
        barChart.setAmount(balance.getAmount());
        return barChart;
    }

    public List<PieChart> getPieChartStatistics(Integer userId, String monthYear) {
        List<PieChart> pieChartList = new ArrayList<>();


        Date date = new Date();
        date.setMonth(Integer.valueOf(monthYear.split("-")[0]) - 1);
        date.setYear(Integer.valueOf(monthYear.split("-")[1]) - 1900);
        int lastDate = getLastDate2(date);
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(lastDate);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(userId, date1, date2);
        operations.forEach(operation -> {
            String articleName = articleRepository.findById(operation.getArticleId()).get().getName();
            PieChart pieChart = buildPieChart(operation, articleName);
            pieChartList.add(pieChart);
        });
        return pieChartList;
    }

    private PieChart buildPieChart(Operation operation, String articleName) {
        PieChart pieChart = new PieChart();
        pieChart.setName(articleName);
        pieChart.setValue(nonNull(operation.getCredit()) ? operation.getCredit() : 0);
        return pieChart;
    }

    //todo: cast dates correctly, 4-6-9 incorrect
    private int getLastDate2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() + 1, date.getDay());
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
