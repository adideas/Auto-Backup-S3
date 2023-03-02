package BackupS3.Configs;

import BackupS3.App.Stories.LogFileStore;
import BackupS3.Configs.Functions.ConfigFileFunction;
import BackupS3.Configs.Functions.ConfigPathFunction;
import BackupS3.Configs.Functions.ConfigUseFunction;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * The config of syslog protocol, which is used to convey event notification messages.
 * <a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class LogConfig implements ConfigFileFunction, ConfigPathFunction, ConfigUseFunction {
    private static LogFileStore store = null;
    @SerializedName("USE") private Boolean USE = true;
    @SerializedName("CACHE_FILE_NAME") private String CACHE_FILE_NAME = "cache_log.bin";
    @SerializedName("COUNT_DAYS") private Integer COUNT_DAYS = 10;
    @SerializedName("FOLDER") private String FOLDER = "log";

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    @Override
    public boolean isUse() {
        return this.USE;
    }

    /**
     * The file of syslog protocol, which is used to convey event notification messages.
     * <a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>
     *
     * @return Log file
     * @since 1.0
     */
    public Path getLogFile() {
        if (!this.isUse()) return null;

        Long keyLog = this.getKey();
        try { getStore().add(keyLog); } catch (Exception ignore) {}
        return getFolder().resolve(keyLog + ".log");
    }

    /**
     * Get folder where the log file is saved
     *
     * @return Folder where the log file is saved
     * @since 1.0
     */
    private Path getFolder() {
        return TO_PATH(this.FOLDER, true);
    }

    /**
     * The number of log files is limited. This is the key to control the number of logs.
     *
     * @return Get key log file
     * @since 1.0
     */
    private Long getKey() {
        return Long.valueOf(LocalDateTime.now().getDayOfYear());
    }

    /**
     * File for storing a database of existing log files.
     *
     * @return Get cache file
     * @since 1.0
     */
    private File getCache() {
        return TO_FILE(this.CACHE_FILE_NAME);
    }

    /**
     * Implementation of the file control service.
     * It keeps track of a certain number of log files.
     *
     * @return Get file count control service
     * @since 1.0
     */
    private LogFileStore getStore() {
        try {
            if (LogConfig.store == null) LogConfig.store = new LogFileStore(this.getCache(), this.COUNT_DAYS, this::remove);
        } catch (Exception ignore){}
        return LogConfig.store;
    }

    /**
     * Log file deletion method. Since the number of log files is limited.
     * @param keyLog key log file {@link LogConfig#getKey()}
     */
    private void remove(Long keyLog) {
        try { Files.deleteIfExists(this.getFolder().resolve(keyLog + ".log")); } catch (Exception ignore) {}
    }
}
