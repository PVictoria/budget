package politech.budget.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import politech.budget.builder.OperationBuilder;
import politech.budget.builder.OperationGetBuilder;
import politech.budget.dto.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
//        Integer userId = userRepository.findUserByName(userName).getId();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
        return getOperationGet(operations);
    }

    public List<OperationGet> findOperationsByUserIdAndCreationTime(Integer userId, Date date) {
//        Integer userId = userRepository.findUserByName(userName).getId();
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAndCreateDate(userId, date1, date2);
        return getOperationGet(operations);
    }

    public List<OperationGet> findOperationsByUserIdAndArticleIdAndCreationTime(Integer userId, String articleName, Date date) {
//        Integer userId = userRepository.findUserByName(userName).getId();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Operation> operations = operationsRepository.findOperationsByUserIdAAndArticleIdAndCreateDate(userId, articleId, date1, date2);
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

    public List<Balance> findBalanceByUserId(Integer userId) {
        return balanceRepository.findBalanceByUserId(userId);
    }

    public List<Balance> findBalanceByUserName(String userName) {
        Integer userId = userRepository.findUserByName(userName).getId();
        return balanceRepository.findBalanceByUserId(userId);
    }

    @Transactional
    public Balance postBalance(Balance balance) {
        return balanceRepository.saveAndFlush(balance);
    }

    public void deleteBalance(Balance balance) {
        balanceRepository.delete(balance);
    }


}
