package politech.budget.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import politech.budget.dto.Article;
import politech.budget.dto.Balance;
import politech.budget.dto.Operation;
import politech.budget.dto.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Dao {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OperationsRepository operationsRepository;
    private final BalanceRepository balanceRepository;


    public User getUser(Integer id) {
        return userRepository.findUserById(id);
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

    public Article findArticleByName(String name) {
        return articleRepository.findArticleByName(name);
    }

    @Transactional
    public Article postArticle(Article article) {
        return articleRepository.saveAndFlush(article);
    }

    @Transactional
    public void deleteArticle(String name) {
        articleRepository.deleteArticleByName(name);
    }

    public List<Operation> findOperationsByUserId(String userName) {
        Integer userId = userRepository.findUserByName(userName).getId();
        return operationsRepository.findOperationsByUserId(userId);
    }

    public List<Operation> findOperationsByUserIdAndArticle(String userName, String articleName) {
        Integer userId = userRepository.findUserByName(userName).getId();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        return operationsRepository.findOperationsByUserIdAndArticleId(userId, articleId);
    }

    public List<Operation> findOperationsByUserIdAndCreationTime(String userName, Date date) {
        Integer userId = userRepository.findUserByName(userName).getId();
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return operationsRepository.findOperationsByUserIdAndCreateDate(userId, date1, date2);
    }

    public List<Operation> findOperationsByUserIdAndArticleIdAndCreationTime(String userName, String articleName, Date date) {
        Integer userId = userRepository.findUserByName(userName).getId();
        Integer articleId = articleRepository.findArticleByName(articleName).getId();
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        Date date1 = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return operationsRepository.findOperationsByUserIdAAndArticleIdAndCreateDate(userId, articleId, date1, date2);
    }

    @Transactional
    public Operation postOperation(Operation operation) {
        return operationsRepository.saveAndFlush(operation);
    }

    public void deleteOperation(Integer operationId) {
        operationsRepository.deleteById(operationId);
    }

    public List<Balance> findBalanceByUserId(Integer userId) {
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
