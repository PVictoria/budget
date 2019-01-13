package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate createDate;
    private Double debit;
    private Double credit;
    private Double amount;
    private Integer userId;
}
