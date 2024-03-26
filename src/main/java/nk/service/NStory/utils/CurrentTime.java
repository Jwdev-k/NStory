package nk.service.NStory.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CurrentTime {
    private static LocalDateTime localDateTime;

    public static String getTime() {
        localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static String getTime2() {
        localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    public static String getTime3() {
        localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static String getTime4() {
        localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
