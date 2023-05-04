package BackupS3.App.Helpers;

import BackupS3.App.Cron;
import BackupS3.App.Loggers.CronLogger;
import BackupS3.Configs.CronConfig;
import BackupS3.Configs.Env;
import BackupS3.Configs.SchedulerConfig;
import BackupS3.Libs.Cron.Scheduler;
import BackupS3.Libs.Quartz.SimpleQuartz;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class is helper for {@link Cron}
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public abstract class CronHelper extends Scheduler {
    public CronHelper(Function<Calendar, Function<String, Boolean>> validator) {
        super(validator);
    }

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
     * Run method from cron config
     *
     * @param config {@link CronConfig}
     * @param cronFunction Action
     * @return true - if action submit
     * @since 1.0
     */
    protected boolean cron(CronConfig config, Consumer<ExecutorService> cronFunction) {
        if (!config.isUse()) {
            return false;
        }

        return cron(config.getCron(), cronFunction);
    }

    /**
     * This method starts a cron maintenance service.
     *
     * @since 1.0
     * @apiNote Run this method with start app. Cron tick default 60s.
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

        Function<Calendar, Function<String, Boolean>> validator = (calendar) -> {
            SimpleQuartz simpleQuartz = new SimpleQuartz(calendar);
            return simpleQuartz::validate;
        };

        Cron cron = new Cron(validator);

        System.out.println("PERIOD: 60s. DELAY: 1s. MAX_TH: " + cron.getMaxThread());

        // Submits a periodic action that becomes enabled first after the given initial delay,
        // and subsequently with the given period.
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(cron, 1, 60, TimeUnit.SECONDS);

        CronLogger.debug("[" + number + "] CRON RUN.");
    }
}
