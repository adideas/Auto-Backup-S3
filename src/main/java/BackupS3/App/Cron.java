package BackupS3.App;

import BackupS3.App.Helpers.CronHelper;
import BackupS3.App.Helpers.FlareLog;
import BackupS3.App.Loggers.CronLogger;
import BackupS3.App.Loggers.MysqlLogger;
import BackupS3.App.Tasks.IndexedFolderTask;
import BackupS3.App.Tasks.MysqlDumpTask;
import BackupS3.Configs.CronConfig;
import BackupS3.Configs.Env;
import BackupS3.Configs.IndexedFolderConfig;
import BackupS3.Configs.MysqlConfig;
import BackupS3.Libs.Cron.Scheduler;
import BackupS3.Libs.Cron.Timer;
import BackupS3.Main;

import java.util.Calendar;
import java.util.function.Function;

/**
 * The class contains the "CRON" configuration.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @apiNote
 *      <li> The configuration is described in the {@link Cron#handle(Calendar now)} method. </li>
 *      <li> To start, you need to call the {@link Cron#start()} method. </li>
 *
 *      <li> {@link Cron} <- {@link CronHelper} <- {@link Scheduler} <- {@link Timer} <- {@link Runnable} </li>
 * @version 1.0
 */
public class Cron extends CronHelper {
    public Cron(Function<Calendar, Function<String, Boolean>> validator) {
        super(validator);
    }

    /**
     * This method is used to configure "CRON".
     * @apiNote
     *      <li> To start, you need to call the {@link Cron#start()} method. </li>
     * @since 1.1
     */
    @Override
    protected void handle(Calendar now) {
        System.out.println("Time: " + now.getTimeInMillis());
        // Delegation run command
        if (Main.type.isOnlyFS()) {
            Env.eachIndexedFolders(this::indexFolder);
        }
        // Delegation run command
        if (Main.type.isOnlyDB()) {
            Env.eachMysqlConfigs(this::dumpMysql);
        }

        // Log file only (verification in progress)
        this.everyTick((service) -> {
            String number = String.valueOf((int)(Math.random() * 10000));

            CronLogger.info("[" + number + "] Cron tick event.");
            service.submit(() -> CronLogger.debug("[" + number + "] Cron task service run."));
        });
    }

    /**
     * @apiNote
     *      <li> This method deals with running a database dump. </li>
     *      <li> Using env file. {@link Env} {@link MysqlConfig} </li>
     * @since 1.0
     * @param mysqlConfig {@link MysqlConfig} MySQL dump configuration.
     */
    private void dumpMysql(MysqlConfig mysqlConfig) {
        // Get cron config
        CronConfig config = mysqlConfig.getCronConfig();

        // If you use cron
        if (config.isUse()) {
            try {
                // Make task for tables
                MysqlDumpTask.eachTables(
                        mysqlConfig,
                        table -> {
                            boolean isCreate = this.cron(
                                    config, service -> service.submit(new MysqlDumpTask(mysqlConfig, table))
                            );
                            String name = mysqlConfig.getDatabaseName();
                            if (isCreate) {
                                CronLogger.info("MySQL dump - commit task [" + name + "." + table + "]");
                            } else {
                                CronLogger.warn("WARN MySQL dump - NO commit task (CRON) [" + name + "." + table + "]");
                            }
                        }
                );
            } catch (Exception error) {
                MysqlLogger.error(error.getMessage());
                CronLogger.debug(error.toString());
                FlareLog.message(error);
            }
        } else if (mysqlConfig.isUse()) {
            String name = mysqlConfig.getDatabaseName();
            CronLogger.warn("MySQL dump - error { CRON.USE = FALSE } [" + name + "]");
        }
    }

    /**
     * @apiNote
     *      <li> This method starts file system indexing. </li>
     *      <li> Using env file. {@link Env} {@link IndexedFolderConfig} </li>
     * @since 1.0
     * @param indexedFolderConfig {@link IndexedFolderConfig} Configuration file system indexing.
     */
    private void indexFolder(IndexedFolderConfig indexedFolderConfig) {
        // Get cron config
        CronConfig config = indexedFolderConfig.getCronConfig();

        // If you use cron
        if (config.isUse()) {
            // Make task
            if (this.cron(config, service -> service.submit(new IndexedFolderTask(indexedFolderConfig)))) {
                String name = indexedFolderConfig.getPathString();
                CronLogger.info("Indexed folder - commit task [" + name + "]");
            }
        } else if (indexedFolderConfig.isUse()) {
            String name = indexedFolderConfig.getPathString();
            CronLogger.error("Indexed folder - error { CRON.USE = FALSE } [" + name + "]");
        }
    }
}
