package at.home.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public class DateConverter {

    private DateConverter() {
        // Static class
    }

    public static Date from(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(OffsetDateTime.now().getOffset()));
    }
}
