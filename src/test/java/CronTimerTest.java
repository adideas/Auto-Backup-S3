import BackupS3.Libs.Cron.Timer;
import BackupS3.Libs.Quartz.SimpleQuartz;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class CronTimerTest {
    @Test
    public void testAdding() {
        TestTimer testTimer = new TestTimer((now) -> {
            SimpleQuartz simpleQuartz = new SimpleQuartz(now);
            return simpleQuartz::validate;
        });
        Calendar time = Calendar.getInstance();

        time.add(Calendar.SECOND, -100);
        testTimer.setTime(time);
        testTimer.run();

        assertEquals(
                testTimer.getTime().getTimeInMillis() / 1000,
                System.currentTimeMillis() / 1000,
                "Time not valid run tick"
        );

        assertEquals(testTimer.getRunHandle(), 1, "Not run handle");
    }

    @Test
    public void testSync() {
        TestTimer testTimer = new TestTimer((now) -> {
            SimpleQuartz simpleQuartz = new SimpleQuartz(now);
            return simpleQuartz::validate;
        });
        Calendar time = Calendar.getInstance();

        time.add(Calendar.SECOND, 100);
        testTimer.setTime(time);
        testTimer.run();
        testTimer.run();

        assertEquals(
                testTimer.getTime().getTimeInMillis() / 1000,
                System.currentTimeMillis() / 1000,
                "Time not valid run tick"
        );

        assertEquals(testTimer.getRunHandle(), 2, "Not run handle");
    }

    static class TestTimer extends Timer {
        private int runHandle = 0;

        public TestTimer(Function<Calendar, Function<String, Boolean>> validator) {
            super(validator);
        }

        public Calendar getTime() {
            return now;
        }

        public int getRunHandle() {
            return runHandle;
        }

        public void setTime(Calendar calendar) {
            this.now.setTimeInMillis(calendar.getTimeInMillis());
        }

        @Override
        protected void handle(Calendar now) {
            runHandle++;
        }
    }
}


