package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.Article;
import politech.budget.dto.Operation;
import politech.budget.dto.OperationGet;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationGetBuilderTest {
    @Autowired
    private OperationGetBuilder operationGetBuilder;

    @Test
    public void buildList() {
        //given
        List<Operation> operations = givenOperation();
        List<Article> articles = givenArticle();
        //when
        List<OperationGet> operationGets = operationGetBuilder.buildList(operations, articles);
        //then
        then(operations, articles, operationGets);
    }

    private void then(List<Operation> operations, List<Article> articles, List<OperationGet> operationGets) {
        assertEquals(operationGets.get(0).getArticleName(), articles.get(0).getName());
        assertEquals(operationGets.get(0).getCreateDate(), "2019-02-15");
        assertEquals(operationGets.get(0).getBalance(), true);
        assertEquals(operationGets.get(0).getCredit(), operations.get(0).getCredit());
        assertEquals(operationGets.get(0).getDebit(), operations.get(0).getDebit());
        assertNotNull(operationGets.get(0).getId());
    }

    private List<Article> givenArticle() {
        Article article = new Article();
        article.setName("articleName");
        article.setId(1);
        return Collections.singletonList(article);
    }

    private List<Operation> givenOperation() {
        Operation operations = new Operation();
        operations.setCreateDate(Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 0, 0, 0)));
        operations.setDebit(1.0);
        operations.setCredit(2.0);
        operations.setBalanceId(1);
        operations.setId(1);
        return Collections.singletonList(operations);
    }
}