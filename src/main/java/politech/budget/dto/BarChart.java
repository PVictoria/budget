package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BarChart {

    private String text;
    private Integer credit;
    private Integer debit;
    private Integer amount;
}
