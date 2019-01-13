package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer articleId;
    private Double debit;
    private Double credit;
    private Timestamp createDate;
    private Integer balanceId;
    private Integer userId;
}
