package politech.budget.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import politech.budget.builder.*;
import politech.budget.dto.*;
import politech.budget.helper.CalendarUtils;

import java.sql.Date;
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
    private final CalendarUtils calendarUtils;
    private final PieChartBuilder pieChartBuilder;
    private final LineChartBuilder lineChartBuilder;
    private final BarChartBuilder barChartBuilder;

    public User getUser(Integer id) {
        return userRepository.findUserById(id);
    }

    public User getUser(User user) {
        String name = user.getName();
        String password = user.getPassword();
        return userRepository.findUserByNameAndPassword(name, password);
    }

    @Transactional
    public User postUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<Article> getArticles() {
        return articleRepository.getAll();
    }

    private Article getArticlesByName(String name) {
        return articleRepository.findArticleByName(name);
    }

    @Transactional
    public Article postArticle(Article article) {
        return articleRepository.saveAndFlush(article);
    }

    @Transactional
    public void deleteArticle(String name) {
        Article article = getArticlesByName(name);
        articleRepository.delete(article);
        articleRepository.flush();
    }

    public List<OperationGet> findOperationsByUserId(Integer userId) {
        List<Operation> operations = operationsRepository.findOperationsByUserId(userId);
        return getOperationGet(operations);
    }

    @Transactional
    public List<OperationGet> getOperationGet(List<Operation> operations) {
        List<Integer> articleIds = operations.stream()
                .mapToInt(Operation::getArticleId).boxed().collect(Collectors.toList());
        List<Article> articles = new ArrayList<>();
        articleIds.forEach(id -> articles.add(articleRepository.findById(id).orElse(null)));
        return operationGetBuilder.buildList(operations, articles);
    }

    @Transactional
    public List<OperationGet> findOperationsByUserIdAndArticle(Integer userId, String articleName) {
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
        return getOperationGet(operations);
    }

    @Transactional
    public List<OperationGet> findOperationsByUserIdAndCreationTime(Integer userId, String monthYear) {
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils(monthYear).invoke();
        List<Operation> operations = operationsRepository.
                findOperationsByUserIdAndCreateDate(userId, dateUtils.getDateFrom(), dateUtils.getDateTo());
        return getOperationGet(operations);
    }

    public List<OperationGet> findOperationsByUserIdAndArticleIdAndCreationTime(Integer userId, String articleName, String monthYear) {
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils(monthYear).invoke();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operations = operationsRepository.
                findOperationsByUserIdAAndArticleIdAndCreateDate(userId, articleId, dateUtils.getDateFrom(), dateUtils.getDateTo());
        return getOperationGet(operations);
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

    @Transactional
    public List<Balance> findBalanceByUserName(Integer userId) {
        List<Balance> balanceByUserId = balanceRepository.findBalanceByUserId(userId);
        balanceByUserId.sort(Comparator.comparing(Balance::getId).reversed());
        return balanceByUserId;

    }

    @Transactional
    public Balance postBalance(String monthYear, Balance balance) {
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils(monthYear).invoke();
        Date dateTo = dateUtils.getDateTo();
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(balance.getUserId(), dateUtils.getDateFrom(), dateUtils.getDateTo());
        Double credit = 0.0;
        Double debit = 0.0;
        for (Operation operation : operations) {
            credit += (nonNull(operation.getCredit()) ? operation.getCredit() : 0);
            debit += (nonNull(operation.getDebit()) ? operation.getDebit() : 0);
        }
        balance.setCreateDate(dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        balance.setCredit(credit);
        balance.setDebit(debit);
        balance.setAmount(debit - credit);

        Balance balance1 = balanceRepository.saveAndFlush(balance);
        operations.forEach(operation -> operationsRepository.updateBalance(operation.getId(), balance1.getId()));
        return balance1;
    }

    @Transactional
    public void deleteBalance(Integer userId) {
        Balance balance = balanceRepository.getLastBalance(userId);
        operationsRepository.deleteBalance(balance.getId());
        balanceRepository.delete(balance);
    }

    @Transactional
    public List<BarChart> getBarChartStatistics(Integer userId, String time) {
        List<BarChart> barChartList = new ArrayList<>();
        List<Balance> balanceByUserId = balanceRepository.findBalanceByUserId(userId);
        balanceByUserId.sort(Comparator.comparing(Balance::getCreateDate));
        if (time.equals("allTime")) {
            balanceByUserId.forEach(balance -> {
                BarChart barChart = barChartBuilder.build(balance);
                barChartList.add(barChart);
            });
        } else if (time.equals("year")) {
            balanceByUserId.stream()
                    .filter(balance -> (LocalDateTime.now().getYear() == balance.getCreateDate().getYear()))
                    .forEach(balance -> {
                        BarChart barChart = barChartBuilder.build(balance);
                        barChartList.add(barChart);
                    });
        }
        return barChartList;
    }

    @Transactional
    public List<LineChart> getLineChartStatistics(Integer userId, String time, String articleName) {
        List<LineChart> lineChartList = new ArrayList<>();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operationsByUserIdAndArticleId = operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
        operationsByUserIdAndArticleId.sort(Comparator.comparing(Operation::getCreateDate));
        switch (time) {
            case "allTime":
                operationsByUserIdAndArticleId.forEach(operation -> {
                    LineChart lineChart = lineChartBuilder.build(operation);
                    lineChartList.add(lineChart);
                });
                break;
            case "year":
                operationsByUserIdAndArticleId.stream()
                        .filter(operation -> (LocalDateTime.now().getYear() == new Date(operation.getCreateDate().getTime()).toLocalDate().getYear() + 1900))
                        .forEach(operation -> {
                            LineChart lineChart = lineChartBuilder.build(operation);
                            lineChartList.add(lineChart);
                        });
                break;
            case "month":
                operationsByUserIdAndArticleId.stream()
                        .filter(operation -> (LocalDateTime.now().getYear() == new Date(operation.getCreateDate().getTime()).toLocalDate().getYear() + 1900))
                        .filter(operation -> (LocalDateTime.now().getMonth().getValue() == operation.getCreateDate().toLocalDateTime().getMonth().getValue() + 1))
                        .forEach(operation -> {
                            LineChart lineChart = lineChartBuilder.build(operation);
                            lineChartList.add(lineChart);
                        });
                break;
        }
        return lineChartList;
    }

    @Transactional
    public Set<PieChart> getPieChartStatistics(Integer userId, String monthYear) {
        Set<PieChart> pieChartList = new HashSet<>();
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils(monthYear).invoke();
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(userId, dateUtils.getDateFrom(), dateUtils.getDateTo());
        operations.forEach(operation -> {
            String articleName = articleRepository.findById(operation.getArticleId())
                    .map(Article::getName)
                    .orElseThrow(() -> new RuntimeException("Статья расходов не найдена"));
            PieChart pieChart = pieChartBuilder.build(operation, articleName);
            pieChartList.stream().filter(pieChart1 -> pieChart1.getName().equals(articleName)).findAny()
                    .ifPresent(pieChart1 -> pieChart1.setValue(pieChart1.getValue() + pieChart.getValue()));
            if (pieChartList.stream().noneMatch(pieChart1 -> pieChart1.getName().equals(articleName))) {
                pieChartList.add(pieChart);
            }
        });
        return pieChartList;
    }
}
