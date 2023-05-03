import BackupS3.Libs.Quartz.SimpleQuartz;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QuartzTest {
    @SuppressWarnings({"SameParameterValue"})
    private static Calendar Date(String dateTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
        } catch (Exception ignore) {}
        return calendar;
    }

    private static String DateToString(Calendar calendar) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    @Test
    public void test() {
        HashMap<String, Calendar> cases = new HashMap<>() {{
            put("5 0 * 8 *", Date("2023-08-01 00:05:00"));
            put("15 14 1 * *", Date("2023-06-01 14:15:00"));
            put("0 22 * * 1-5", Date("2023-05-03 22:00:00"));
            put("1 22 * * 1-5", Date("2023-05-09 22:01:00"));
        }};

        for (Map.Entry<String, Calendar> item : cases.entrySet()) {
            assertTrue(
                    new SimpleQuartz(item.getValue()).validate(item.getKey()),
                    item.getKey() + " <=> " + DateToString(item.getValue())
            );
        }

        HashMap<String, Calendar> casesNotWorking = new HashMap<>() {{
            put("*/2 * * * *", Date("2023-05-03 19:10:00"));
            put("1/2 * * * *", Date("2023-05-03 19:12:00"));
            put("0,2 * * * *", Date("2023-05-03 20:00:00"));
            put("0,1,2 * * * *", Date("2023-05-03 20:00:00"));
            put("0,1,2/2 * * * *", Date("2023-05-03 19:20:00"));
            put("0,2/2 * * * *", Date("2023-05-03 19:12:00"));
        }};

        for (Map.Entry<String, Calendar> item : casesNotWorking.entrySet()) {
            assertFalse(
                    new SimpleQuartz(item.getValue()).validate(item.getKey()),
                    item.getKey() + " <=> " + DateToString(item.getValue())
            );
        }
    }
}
