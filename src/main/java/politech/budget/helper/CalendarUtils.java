package politech.budget.helper;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class CalendarUtils {

    public class DateUtils {
        private final String monthYear;
        @Getter
        private Date dateFrom;
        @Getter
        private Date dateTo;

        public DateUtils(String monthYear) {
            this.monthYear = monthYear;
        }

        public DateUtils invoke() {
            int month = Integer.valueOf(monthYear.split("-")[0]);
            int year = Integer.valueOf(monthYear.split("-")[1]);
            dateFrom = Date.valueOf(LocalDate.of(year, month, 1));
            int lastDate = dateFrom.toLocalDate().lengthOfMonth();
            dateTo = Date.valueOf(LocalDate.of(year, month, lastDate));
            return this;
        }
    }
}
