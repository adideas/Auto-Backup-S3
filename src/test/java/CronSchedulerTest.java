import BackupS3.Libs.Cron.Scheduler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CronSchedulerTest {
    @Test
    public void test() {
        TestScheduler testScheduler = new TestScheduler();
        testScheduler.setTime();

        assertEquals(testScheduler.getCountRun(), 2, "Invalid run");
    }

    static class TestScheduler extends Scheduler {
        private int daily = 0;
        private int every = 0;
        @Override
        protected void handle() {
            this.daily(0, periodInMinutes(), executorService -> {
                daily++;
            });
            this.everyTick(executorService -> {
                every++;
            });
        }

        @Override
        public long periodInMinutes() {
            return 5;
        }

        public void setTime() {
            this.time = periodInMinutes() * 60;
            run();
        }

        public int getCountRun() {
            return daily + every;
        }

        @Override
        public int getMaxThread() {
            return 1;
        }
    }
}
