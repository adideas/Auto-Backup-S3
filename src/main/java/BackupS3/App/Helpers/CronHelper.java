package BackupS3.App.Helpers;

import BackupS3.App.Cron;
import BackupS3.App.Loggers.CronLogger;
import BackupS3.Configs.CronConfig;
import BackupS3.Configs.Env;
import BackupS3.Configs.SchedulerConfig;
import BackupS3.Libs.Cron.Schedule;
import BackupS3.Libs.Cron.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is helper for {@link Cron}
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public abstract class CronHelper extends Scheduler {
    /**
     * This method gets the maximum number of threads
     *
     * @since 1.0
     * @return Maximum number of threads {@link Env#getSchedulerConfig()} {@link SchedulerConfig#getMaxThread()}
     */
    @Override
    public int getMaxThread() {
        return Env.getSchedulerConfig().getMaxThread();
    }

    /**
     * This method gets the interval between action.
     *
     * @since 1.0
     * @return Interval between action {@link Env#getSchedulerConfig()} {@link SchedulerConfig#periodInSeconds()}
     */
    @Override
    public long periodInSeconds() {
        return Env.getSchedulerConfig().periodInSeconds();
    }

    /**
     * Run method from cron config
     *
     * @param config {@link CronConfig}
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean cron(CronConfig config, Consumer<ExecutorService> cronFunction) {
        if (config.getType() == Schedule.EVERY) {
            this.everyTick(cronFunction);
            return true;
        } else if (config.getType() == Schedule.DAILY) {
            return this.daily(config.getDailyHour(), config.getDailyMinute(), cronFunction);
        }
        return false;
    }

    /**
     * This method starts a cron maintenance service.
     *
     * @since 1.0
     * @apiNote Run this method with start app.
     *
     * <pre>{@code
     * // Example:
     * import BackuperS3.App.Cron;
     * public class Main {
     *     public static void main(String[] args) {
     *         Cron.start();
     *     }
     * }}</pre>
     */
    public static void start() {
        String number = String.valueOf((int)(Math.random() * 10000));

        CronLogger.debug("[" + number + "] CRON START.");

        Cron cron = new Cron();
        long period = cron.periodInSeconds();
        long delay = 5L;

        if (period < 60) {
            delay = period / 5;
        }

        System.out.println(period);
        System.out.println(delay);

        // Submits a periodic action that becomes enabled first after the given initial delay,
        // and subsequently with the given period.
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(cron, delay, cron.periodInSeconds(), TimeUnit.SECONDS);

        CronLogger.debug("[" + number + "] CRON RUN.");
    }
}
