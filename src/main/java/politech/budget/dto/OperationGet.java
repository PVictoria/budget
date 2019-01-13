package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationGet {

    private Integer id;
    private String articleName;
    private Double debit;
    private Double credit;
    private String createDate;
    private Boolean balance;
}
