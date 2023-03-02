package BackupS3.Configs.Functions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This interface declares a method TO_PATH. For get path from string.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public interface ConfigPathFunction {
    /**
     * Get path or null (if not exist)
     *
     * @param path Path file
     * @return Path object
     * @since 1.0
     */
    default Path TO_PATH_OR_NULL(String path) {
        Path object = Paths.get(path);
        return Files.exists(object) ? object : null;
    }

    /**
     * Get path (if not exist make file)
     *
     * @param path Path file
     * @param isFolder If folder (mkdirs)
     * @return Path object
     * @since 1.0
     */
    default Path TO_PATH(String path, boolean isFolder) {
        Path object = Paths.get(path);
        if (Files.exists(object)) return object;

        object = Paths.get(System.getProperty("user.dir"), path);
        if (Files.exists(object)) return object;

        if (isFolder) {
            try { Files.createDirectories(object); } catch (Exception ignore) {}
        }

        return object;
    }
}
