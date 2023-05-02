package BackupS3.Libs.Cron;

import BackupS3.App.Helpers.CronHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class helper for CRON (Make Thread pool)
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public abstract class Scheduler extends Timer {
    private final ExecutorService service;

    /**
     * Make Thread pool.
     * @apiNote
     *  <li> Config count pool {@link Scheduler#getMaxThread()} </li>
     * @since 1.0
     */
    public Scheduler() {
        service = Executors.newFixedThreadPool(this.getMaxThread());
    }

    /**
     * ABSTRACT: This method gets the maximum number of threads. See: {@link CronHelper#getMaxThread()}
     *
     * @return Maximum number of threads
     * @since 1.0
     */
    public abstract int getMaxThread();

    /**
     * Method for checking task completion time
     *
     * @param hourUTC Number hour
     * @param minuteUTC Number minute
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean daily(long hourUTC, long minuteUTC, Consumer<ExecutorService> cronFunction) {
        long timeTask = (hourUTC * (60 * 60)) + (minuteUTC * 60);

        if (time <= timeTask && timeTask < time + (periodInSeconds() * 60)) {
            cronFunction.accept(service);
            return true;
        }
        return false;
    }

    /**
     * Method for checking task completion time
     *
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean everyTick(Consumer<ExecutorService> cronFunction) {
        cronFunction.accept(service);
        return true;
    }
}
