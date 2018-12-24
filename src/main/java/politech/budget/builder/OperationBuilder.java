package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.Article;
import politech.budget.dto.Operation;
import politech.budget.dto.OperationPost;

import static java.util.Objects.nonNull;

@Component
public class OperationBuilder {

    public Operation build(OperationPost operationPost, Article article) {
        Operation operation = new Operation();
        operation.setArticleId(article.getId());
        operation.setDebit(nonNull(operationPost.getDebit()) ? operationPost.getDebit() : null);
        operation.setCredit(nonNull(operationPost.getCredit()) ? -operationPost.getCredit() : null);
        operation.setCreateDate(operationPost.getCreateDate());
        operation.setUserId(operationPost.getUserId());
        operation.setBalanceId(null);
        operation.setId(null);
        return operation;
    }
}
