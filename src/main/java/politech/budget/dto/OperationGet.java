package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationGet {

    private Integer id;
    private String articleName;
    private Integer debit;
    private Integer credit;
    private String createDate;
    private Boolean balance;
}
