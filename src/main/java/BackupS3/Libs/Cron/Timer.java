package BackupS3.Libs.Cron;

import BackupS3.App.Cron;
import BackupS3.App.Helpers.CronHelper;

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
     * Current time in unix time (in current day)
     * @apiNote
     *  <li> time = 1677674952501000 / 1000 = 1677674952501 </li>
     *  <li> time = time MOD 86400 (86400 = 24 * 60 * 60) </li>
     *  <li> time = 14901 (time in day) </li>
     * @since 1.0
     */
    protected long time = (System.currentTimeMillis() / 1000) % 86400;

    /**
     * Period sync
     * @apiNote
     *  <li> 0) Current tick </li>
     *  <li> 1) Maximum tick for update time (default 10) </li>
     * @since 1.0
     */
    private final int[] syncTick = new int[]{0, 10};

    /**
     * {@inheritDoc}
     * @since 1.0
     */
    @Override
    final public void run() {
        // Run action cron
        this.handle();
        // Time update event
        this.tick();
    }

    /**
     * Time update event
     * @apiNote
     *  <li> Update {@link Timer#time} use {@link Timer#periodInMinutes()} </li>
     *  <li> Update syncTick </li>
     * @since 1.0
     */
    private void tick() {
        // Auto increment time (add minutes)
        this.time += this.periodInMinutes() * 60;
        // Auto increment time sync
        this.syncTick[0]++;

        // If it's time to sync
        if (this.syncTick[0] > this.syncTick[1]) {
            // Time synchronization
            this.time = (System.currentTimeMillis() / 1000) % 86400;
            // Reset sync period
            this.syncTick[0] = 0;
        }
    }

    /**
     * ABSTRACT: This method is used to configure "CRON". See {@link Cron#handle()}
     *
     * @apiNote
     *      <li> To start, you need to call the {@link Cron#start()} method. </li>
     * @since 1.0
     */
    protected abstract void handle();

    /**
     * ABSTRACT: This method gets the interval between action. See {@link CronHelper#periodInMinutes()}
     * @return Interval between action.
     */
    public abstract long periodInMinutes();
}
