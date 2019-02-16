package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.Operation;
import politech.budget.dto.PieChart;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PieChartBuilderTest {
    @Autowired
    private PieChartBuilder pieChartBuilder;

    @Test
    public void build() {
        //given
        Operation operation = givenOperation();
        //when
        PieChart pieChart = pieChartBuilder.build(operation, "articleName");
        //then
        assertEquals(pieChart.getName(), "articleName");
        assertEquals(pieChart.getValue(), operation.getCredit());

    }

    private Operation givenOperation() {
        Operation operations = new Operation();
        operations.setCreateDate(Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 0, 0, 0)));
        operations.setDebit(1.0);
        operations.setCredit(2.0);
        operations.setBalanceId(1);
        return operations;
    }
}