package BackupS3.App.Loggers;

import BackupS3.Configs.Env;
import BackupS3.Libs.Files.LogFile;
import BackupS3.Libs.Logger.Column;
import BackupS3.Libs.Logger.Logger;

/**
 * MySQL Syslog
 * <a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class MysqlLogger extends Logger {
    private static LogFile instance = null;

    /**
     * Get instance log file
     * @return log file
     * @since 1.0
     */
    private static LogFile getInstance() {
        if (instance == null) {
            instance = LogFile.getInstance(Env.getLogFile());
        }
        return instance;
    }

    /**
     * Informational: informational messages
     * @param msg message
     * @since 1.0
     */
    public static void info(String msg) {
        getInstance().append(Logger.getLine(Column.HOSTNAME.MYSQL, Column.LEVEL.INFO, msg));
    }

    /**
     * Error: error conditions
     * @param msg message
     * @since 1.0
     */
    public static void error(String msg) {
        getInstance().append(Logger.getLine(Column.HOSTNAME.MYSQL, Column.LEVEL.ERROR, msg));
    }
}
