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


    public User getUser(long id) {
        return userRepository.findUserById(id);
    }

    @Transactional
    public User postUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<Article> getArticles() {
        return articleRepository.getAll();
    }

    @Transactional
    public Article postArticle(Article article) {
        return articleRepository.saveAndFlush(article);
    }

    @Transactional
    public void deleteArticle(String name) {
        articleRepository.deleteArticleByName(name);
    }

    public List<Operation> findOperationsByUserId(Integer userId) {
        return operationsRepository.findOperationsByUserId(userId);
    }

    public List<Operation> findOperationsByUserIdAndArticle(Long userId, Article article) {
        return operationsRepository.findOperationsByUserIdAndArticleId(userId, article.getId());
    }

    public List<Operation> findOperationsByUserIdAndCreationTime(Long userId, Date date) {
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        return operationsRepository.findOperationsByUserIdAndCreateDate(userId, startDay, endDay);
    }

    public List<Operation> findOperationsByUserIdAndArticleIdAndCreationTime(Long userId, Article article, Date date) {
        LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(31);
        return operationsRepository.findOperationsByUserIdAAndArticleIdAndCreateDate(userId, article.getId(), startDay, endDay);
    }

    @Transactional
    public Operation postOperation(Operation operation) {
        return operationsRepository.saveAndFlush(operation);
    }

    public void deleteOperation(Operation operation) {
        operationsRepository.delete(operation);
    }

    public List<Balance> findBalanceByUserId(Long userId) {
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
