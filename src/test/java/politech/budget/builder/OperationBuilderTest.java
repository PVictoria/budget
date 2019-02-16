package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.Article;
import politech.budget.dto.Operation;
import politech.budget.dto.OperationPost;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationBuilderTest {

    @Autowired
    private OperationBuilder operationBuilder;

    @Test
    public void build() {
        //given
        Article article = givenArticle();
        OperationPost operationPost = givenOperationPost(givenArticle());
        //when
        Operation operation = operationBuilder.build(operationPost, article);
        //then
        then(operationPost, article, operation);
    }

    private Article givenArticle() {
        Article article = new Article();
        article.setId(1);
        article.setName("articleName");
        return article;
    }

    private OperationPost givenOperationPost(Article article) {
        OperationPost operationPost = new OperationPost();
        operationPost.setArticleName(article.getName());
        operationPost.setCreateDate(Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 0, 0, 0)));
        operationPost.setDebit(1.0);
        operationPost.setCredit(2.0);
        operationPost.setUserId(null);
        return operationPost;
    }

    private void then(OperationPost operationPost, Article article, Operation operation) {
        assertEquals(operation.getCredit(), Double.valueOf(-operationPost.getCredit()));
        assertEquals(operation.getDebit(), operationPost.getDebit());
        assertEquals(operation.getCreateDate(), operationPost.getCreateDate());
        assertEquals(operation.getArticleId(), article.getId());
        assertNull(operation.getBalanceId());
        assertNull(operation.getUserId());

        assertEquals(operationPost.getArticleName(), article.getName());
    }
}