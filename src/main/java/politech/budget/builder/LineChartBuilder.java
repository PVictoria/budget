package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.LineChart;
import politech.budget.dto.Operation;

import static java.util.Objects.nonNull;

@Component
public class LineChartBuilder {

    public LineChart build(Operation operation) {
        LineChart lineChart = new LineChart();
        lineChart.setXDate(operation.getCreateDate().toLocalDateTime().toLocalDate().toString());
        lineChart.setCredit(nonNull(operation.getCredit()) ? operation.getCredit() : 0.0);
        lineChart.setDebit(operation.getDebit());
        return lineChart;
    }
}
