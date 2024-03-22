package nk.service.NStory.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CurrentTime {
    private static final LocalDateTime localDateTime = LocalDateTime.now();

    public static String getTime() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static String getTime2() {
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    public static String getTime3() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static String getTime4() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
