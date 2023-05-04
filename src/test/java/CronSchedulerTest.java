import BackupS3.Libs.Cron.Scheduler;
import BackupS3.Libs.Quartz.SimpleQuartz;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CronSchedulerTest {
    @Test
    public void test() {
        TestScheduler testScheduler = new TestScheduler((now) -> {
            SimpleQuartz simpleQuartz = new SimpleQuartz(now);
            return simpleQuartz::validate;
        });
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 2);

        testScheduler.setTime(calendar);
        testScheduler.run();

        assertEquals(testScheduler.getCountRun(), 4, "Invalid run");
    }

    static class TestScheduler extends Scheduler {
        private int every = 0;
        private void func(ExecutorService executorService) {
            every++;
        }

        /**
         * Make Thread pool.
         *
         * @param validator Function get validator
         * @apiNote <li> Config count pool {@link Scheduler#getMaxThread()} </li>
         * @since 1.0
         */
        public TestScheduler(Function<Calendar, Function<String, Boolean>> validator) {
            super(validator);
        }

        @Override
        protected void handle(Calendar now) {
            System.out.println(this.cron("* * * * *", this::func));
            System.out.println(this.cron("2 * * * *", this::func));
            System.out.println(this.cron("3 * * * *", this::func));
            System.out.println(this.cron("5 * * * *", this::func));
            System.out.println(this.cron("7 * * * *", this::func));
            System.out.println(this.cron("1-2 * * * *", this::func));
            System.out.println(this.cron("4-5 * * * *", this::func));
            System.out.println(this.cron("6-7 * * * *", this::func));
            System.out.println(this.everyTick(this::func));
        }

        public void setTime(Calendar calendar) {
            this.now.setTimeInMillis(calendar.getTimeInMillis());
        }

        public int getCountRun() {
            return every;
        }

        @Override
        public int getMaxThread() {
            return 1;
        }
    }
}
