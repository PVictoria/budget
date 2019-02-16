package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.LineChart;
import politech.budget.dto.Operation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LineChartBuilderTest {
    @Autowired
    private LineChartBuilder lineChartBuilder;

    @Test
    public void build() {
        //given
        Operation operation = givenOperation();
        //when
        LineChart lineChart = lineChartBuilder.build(operation);
        //then
        then(operation, lineChart);
    }

    private void then(Operation operation, LineChart lineChart) {
        assertEquals(lineChart.getCredit(), operation.getCredit());
        assertEquals(lineChart.getDebit(), operation.getDebit());
        assertEquals(lineChart.getXDate(), "2019-02-15");
    }


    private Operation givenOperation() {
        Operation operation = new Operation();
        operation.setBalanceId(1);
        operation.setUserId(2);
        operation.setCreateDate(Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 0, 0, 0)));
        operation.setArticleId(3);
        operation.setCredit(4.0);
        operation.setDebit(5.0);
        operation.setId(1);
        return operation;
    }
}