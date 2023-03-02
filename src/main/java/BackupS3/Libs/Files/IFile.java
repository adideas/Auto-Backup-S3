package BackupS3.Libs.Files;

import java.io.IOException;
import java.nio.file.Path;

/**
 * File Interface
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
@SuppressWarnings("UnusedReturnValue")
public interface IFile {
    /**
     * Append bytes to file
     *
     * @return File Interface
     * @throws IOException If you file not exists
     * @since 1.0
     */
    default IFile append(byte[] bytes) throws IOException {
        return this;
    }

    /**
     * Append text to file
     *
     * @return File Interface
     * @throws IOException If you file not exists
     * @since 1.0
     */
    default IFile append(String text) throws IOException {
        return this;
    }

    /**
     * Get file path
     *
     * @return If virtual file return null;
     * @since 1.0
     */
    default Path getPath() {
        return null;
    }

    /**
     * Remove file
     *
     * @return If virtual file return true allays;
     * @throws IOException If you file not exists
     * @since 1.0
     */
    default boolean remove() throws IOException {
        return true;
    }

    /**
     * Check is virtual file
     *
     * @since 1.0
     */
    default boolean isFile() {
        return false;
    }

    /**
     * Get virtual link
     *
     * @return text link
     * @since 1.0
     */
    default String virtualLink() {
        return Math.random() + "-" + System.currentTimeMillis() + ".bin";
    }
}
