package BackupS3.Libs.Files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Log file
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class LogFile implements IFile {
    private final Path path;
    private static LogFile instance = null;

    /**
     * Get instance log file
     *
     * @param path path file
     * @since 1.0
     */
    private LogFile(Path path) {
        this.path = path;
    }

    /**
     * Get instance log file
     *
     * @param path path file
     * @since 1.0
     */
    public static LogFile getInstance(Path path) {
        if (instance == null) {
            instance = new LogFile(path);
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     *
     * @param text Text append file
     * @return instance log file
     * @since 1.0
     */
    @Override
    public IFile append(String text) {
        try {Files.writeString(path, text, StandardOpenOption.APPEND);} catch (Exception ignore) {
            if (!Files.exists(path)) {
                try {Files.writeString(path, text);} catch (Exception ignored) {}
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param bytes Bytes append files
     * @return instance log file
     * @since 1.0
     */
    @Override
    public IFile append(byte[] bytes) {
        try {Files.write(path, bytes, StandardOpenOption.APPEND);} catch (Exception ignore) {}
        return this;
    }
}
