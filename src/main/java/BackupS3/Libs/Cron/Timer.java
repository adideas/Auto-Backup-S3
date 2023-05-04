package BackupS3.Libs.Cron;

import java.util.Calendar;
import java.util.function.Function;

/**
 * This class implements the action call timer.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @apiNote
 *  <li>This class is helper for {@link Scheduler}</li>
 * @version 1.0
 */
public abstract class Timer implements Runnable {

    /**
     * Current time
     */
    protected final Calendar now = Calendar.getInstance();
    protected final Function<String, Boolean> validator;

    public Timer(Function<Calendar, Function<String, Boolean>> validator) {
        this.validator = validator.apply(now);
    }

    /**
     * {@inheritDoc}
     * @since 1.0
     */
    @Override
    final public void run() {
        // Run action cron
        this.handle(now);
        // Time update event
        this.tick();
    }

    /**
     * Time update event
     * @apiNote
     *  <li> Update {@link Timer#now}  </li>
     * @since 1.0
     */
    private void tick() {
        long milliseconds = System.currentTimeMillis() - now.getTimeInMillis();
        if (milliseconds >= 0L) {
            now.add(Calendar.MILLISECOND, (int) milliseconds);
        } else {
            now.setTimeInMillis(System.currentTimeMillis());
        }
    }

    /**
     * ABSTRACT: This method is used to configure "CRON".
     *
     * @since 1.0
     */
    protected abstract void handle(Calendar now);
}
