package politech.budget.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarUtilsTest {

    @Autowired
    private CalendarUtils calendarUtils;

    @Test
    public void invoke() {
        CalendarUtils.DateUtils dateUtils = calendarUtils.new DateUtils("02-2019").invoke();
        assertEquals(new Date(119, 1, 1), dateUtils.getDateFrom());
        assertEquals(new Date(119, 1, 28), dateUtils.getDateTo());
    }
}