package BackupS3.Libs.Cron;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

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
    public Scheduler(Function<Calendar, Function<String, Boolean>> validator) {
        super(validator);
        service = Executors.newFixedThreadPool(this.getMaxThread());
    }

    /**
     * ABSTRACT: This method gets the maximum number of threads.
     *
     * @return Maximum number of threads
     * @since 1.0
     */
    public abstract int getMaxThread();

    /**
     * Method for checking task completion time [* * * * *]
     *
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean everyTick(Consumer<ExecutorService> cronFunction) {
        cronFunction.accept(service);
        return true;
    }

    /**
     * Run method from cron config
     *
     * @param expression [* * * * *] cron expression
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean cron(String expression, Consumer<ExecutorService> cronFunction) {
        if (validator.apply(expression)) {
            this.everyTick(cronFunction);
            return true;
        }
        return false;
    }
}
