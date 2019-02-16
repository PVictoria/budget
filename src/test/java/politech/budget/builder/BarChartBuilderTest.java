package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.Balance;
import politech.budget.dto.BarChart;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BarChartBuilderTest {
    @Autowired
    private BarChartBuilder barChartBuilder;

    @Test
    public void build() {
        //given
        Balance balance = givenBalance();
        //when
        BarChart barChart = barChartBuilder.build(balance);
        //then
        then(balance, barChart);

    }

    private void then(Balance balance, BarChart barChart) {
        assertEquals(barChart.getAmount(), balance.getAmount());
        assertEquals(barChart.getCredit(), balance.getCredit());
        assertEquals(barChart.getDebit(), balance.getDebit());
        assertEquals(barChart.getText(), "2019-02-15");
    }

    private Balance givenBalance() {
        Balance balance = new Balance();
        balance.setAmount(12.0);
        balance.setCredit(1.0);
        balance.setDebit(2.0);
        balance.setUserId(1);
        balance.setCreateDate(LocalDate.of(2019, 2, 15));
        balance.setId(1);
        balance.setUserId(1);
        return balance;
    }
}