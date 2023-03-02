import BackupS3.Libs.Cron.Timer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CronTimerTest {
    @Test
    public void test() {
        TestTimer testTimer = new TestTimer();
        long time = testTimer.getTime();
        testTimer.run();
        assertNotEquals(time, testTimer.getTime(), "Time not run tick");
        assertEquals((testTimer.periodInMinutes() * 60) + time, testTimer.getTime(), "Time not valid run tick");
        assertEquals(testTimer.getRunHandle(), 1, "Not run handle");

        testTimer.setTime(0);

        boolean validSync = false;
        for(int i = 0; i < 30; i++) {
            testTimer.run();
            if (testTimer.getTime() > time) {
                validSync = true;
                break;
            }
        }

        assertTrue(validSync, "Invalid sync time");
    }

    static class TestTimer extends Timer {

        private int runHandle = 0;

        @Override
        protected void handle() {
            runHandle++;
        }

        @Override
        public long periodInMinutes() {
            return 5;
        }

        public long getTime() {
            return time;
        }

        public int getRunHandle() {
            return runHandle;
        }

        public void setTime(int t) {
            this.time = t;
        }
    }
}


