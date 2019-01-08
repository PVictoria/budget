package politech.budget.helper;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CalendarUtils {
    public Integer getLastDay(Integer month) {
        switch (month) {
            case 0:
                return 31;
            case 1:
                return 28;
            case 2:
                return 31;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 31;
            case 8:
                return 30;
            case 9:
                return 31;
            case 10:
                return 30;
            case 11:
                return 31;
        }
        return null;
    }

    public class DateUtils {
        private String monthYear;
        @Getter
        private Date dateFrom;
        @Getter
        private Date dateTo;

        public DateUtils(String monthYear) {
            this.monthYear = monthYear;
        }

        public DateUtils invoke() {
            Date date = new Date();
            date.setMonth(Integer.valueOf(monthYear.split("-")[0]) - 1);
            date.setYear(Integer.valueOf(monthYear.split("-")[1]) - 1900);
            int lastDate = getLastDay(date.getMonth());
            LocalDate startDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
            dateFrom = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
            LocalDate endDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(lastDate);
            dateTo = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return this;
        }
    }


}
