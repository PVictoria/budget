package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.Operation;
import politech.budget.dto.PieChart;

import static java.util.Objects.nonNull;

@Component
public class PieChartBuilder {

    public PieChart build(Operation operation, String articleName) {
        PieChart pieChart = new PieChart();
        pieChart.setName(articleName);
        pieChart.setValue(nonNull(operation.getCredit()) ? operation.getCredit() : 0);
        return pieChart;
    }
}
