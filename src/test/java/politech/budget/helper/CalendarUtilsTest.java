package politech.budget.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarUtilsTest {

    @Autowired
    private CalendarUtils calendarUtils;

    @Test
    public void getLastDay() {
        //when
        Integer jan = calendarUtils.getLastDay(0);
        Integer feb = calendarUtils.getLastDay(1);
        Integer mar = calendarUtils.getLastDay(2);
        Integer apr = calendarUtils.getLastDay(3);
        Integer may = calendarUtils.getLastDay(4);
        Integer jun = calendarUtils.getLastDay(5);
        Integer jul = calendarUtils.getLastDay(6);
        Integer aug = calendarUtils.getLastDay(7);
        Integer sep = calendarUtils.getLastDay(8);
        Integer oct = calendarUtils.getLastDay(9);
        Integer nov = calendarUtils.getLastDay(10);
        Integer dec = calendarUtils.getLastDay(11);
        Integer nullMonth = calendarUtils.getLastDay(12);

        //then
        assertEquals(jan, Integer.valueOf(31));
        assertEquals(feb, Integer.valueOf(28));
        assertEquals(mar, Integer.valueOf(31));
        assertEquals(apr, Integer.valueOf(30));
        assertEquals(may, Integer.valueOf(31));
        assertEquals(jun, Integer.valueOf(30));
        assertEquals(jul, Integer.valueOf(31));
        assertEquals(aug, Integer.valueOf(31));
        assertEquals(sep, Integer.valueOf(30));
        assertEquals(oct, Integer.valueOf(31));
        assertEquals(nov, Integer.valueOf(30));
        assertEquals(oct, Integer.valueOf(31));
        assertEquals(nov, Integer.valueOf(30));
        assertEquals(dec, Integer.valueOf(31));
        assertNull(nullMonth);
    }

    @Test
    public void invoke() {
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils("02-2019").invoke();
        assertEquals(new Date(119, 1, 1), dateUtils.getDateFrom());
        assertEquals(new Date(119, 1, 28), dateUtils.getDateTo());


    }
}