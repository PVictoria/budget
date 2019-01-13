package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OperationPost {

    private String articleName;
    private Double debit;
    private Double credit;
    private Timestamp createDate;
    private Integer userId;
}
