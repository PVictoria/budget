package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.Article;
import politech.budget.dto.Operation;
import politech.budget.dto.OperationGet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
public class OperationGetBuilder {

    private OperationGet build(Operation operation, Article article) {
        OperationGet operationGet = new OperationGet();
        operationGet.setId(operation.getId());
        operationGet.setArticleName(article.getName());
        operationGet.setBalance(nonNull(operation.getBalanceId()));
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        operationGet.setCreateDate(f.format(new Date(operation.getCreateDate().getTime())));
        operationGet.setCredit(operation.getCredit());
        operationGet.setDebit(operation.getDebit());
        return operationGet;
    }

    public List<OperationGet> buildList(List<Operation> operations, List<Article> articles) {
        List<OperationGet> operationGets = new ArrayList<>();
        for (int i = 0; i < operations.size(); i++) {
            operationGets.add(build(operations.get(i), articles.get(i)));
        }
        return operationGets;
    }
}
