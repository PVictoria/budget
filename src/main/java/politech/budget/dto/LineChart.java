package politech.budget.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LineChart {
    private String xDate;
    private Integer credit;
    private Integer debit;
}
