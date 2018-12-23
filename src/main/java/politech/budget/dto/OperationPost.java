package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OperationPost {

    private String articleName;
    private Integer debit;
    private Integer credit;
    private Timestamp createDate;
    private Integer userId;
}
