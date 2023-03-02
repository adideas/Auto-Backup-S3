package BackupS3.Libs.BindFilesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Dynamic file system indexing
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class BindFilesystem {
    /**
     * Start Sync
     *
     * @param path Path folder or Path file
     * @param check Function check
     * @param action If Function check return false
     * @since 1.0
     */
    public static void sync(Path path, BiFunction<Boolean, Long, Boolean> check, Function<Path, Boolean> action) {
        if (Files.exists(path)) {
            directory(path, check, action);
        } else {
            file(path, check, action);
        }
    }

    /**
     * Get hash file
     *
     * @param file Path file
     * @param check Function check (Long) -> Boolean
     * @param action If Function check return false
     * @since 1.0
     */
    private static void file(Path file, BiFunction<Boolean, Long, Boolean> check, Function<Path, Boolean> action) {
        try {
            // Unique hash (name file)
            int hashName = file.hashCode();

            BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class);
            // Unique hash (last update file)
            int hashOptions = basicFileAttributes.lastModifiedTime().hashCode();

            // Get hash file
            long value = (((long) hashName) << 32) | (hashOptions & 0xffffffffL);

            if (!check.apply(true, value)) {
                if (action.apply(file)) {
                    check.apply(false, value);
                }
            }
        } catch (IOException ignore) {}
    }

    /**
     * Directory handling
     *
     * @param folder Path folder
     * @param check Function check
     * @param action If Function check return false
     * @since 1.0
     */
    private static void directory(Path folder, BiFunction<Boolean, Long, Boolean> check, Function<Path, Boolean> action) {
        try {
            //noinspection resource
            Files.list(folder).forEach((path) -> each(path, check, action));
        } catch (IOException ignore) {}
    }

    /**
     * Loop through files
     *
     * @param path Path folder
     * @param check Function check (Long) -> Boolean
     * @param action If Function check return false
     * @since 1.0
     */
    private static void each(Path path, BiFunction<Boolean, Long, Boolean> check, Function<Path, Boolean> action) {
        if (Files.isDirectory(path)) {
            directory(path, check, action);
        } else {
            file(path, check, action);
        }
    }
}
