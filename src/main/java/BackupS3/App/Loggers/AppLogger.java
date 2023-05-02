package BackupS3.App.Loggers;

import BackupS3.Configs.Env;
import BackupS3.Libs.Files.LogFile;
import BackupS3.Libs.Logger.Column;
import BackupS3.Libs.Logger.Logger;

public class AppLogger {
    private static LogFile instance = null;

    /**
     * Get instance log file
     * @return log file
     * @since 1.1
     */
    private static LogFile getInstance() {
        if (instance == null) {
            instance = LogFile.getInstance(Env.getLogFile());
        }
        return instance;
    }

    /**
     * Warning: warning conditions
     * @param msg message
     * @since 1.0
     */
    public static void warn(String msg) {
        getInstance().append(Logger.getLine(Column.HOSTNAME.CRON, Column.LEVEL.WARN, msg));
    }
}
