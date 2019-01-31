package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.Balance;
import politech.budget.dto.BarChart;

@Component
public class BarChartBuilder {

    public BarChart build(Balance balance) {
        BarChart barChart = new BarChart();
        barChart.setText(balance.getCreateDate().toString().substring(0, 10));
        barChart.setCredit(balance.getCredit());
        barChart.setDebit(balance.getDebit());
        barChart.setAmount(balance.getAmount());
        return barChart;
    }

}
