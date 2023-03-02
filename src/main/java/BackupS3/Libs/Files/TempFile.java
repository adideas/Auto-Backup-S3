package BackupS3.Libs.Files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Temporary file
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class TempFile implements IFile {
    private final Path path;
    private String[] links = null;

    public TempFile() throws IOException {
        this.path = Files.createTempFile("temp_file" + Math.random(), ".tmp");
    }

    public TempFile(String... links) throws IOException {
        this();
        this.links = links;
    }

    /**
     * {@inheritDoc}
     *
     * @return If virtual file return null;
     * @since 1.0
     */
    @Override
    public Path getPath() {
        return path;
    }

    /**
     * Append bytes to file
     *
     * @return File Interface
     * @throws IOException If you file not exists
     * @since 1.0
     */
    @Override
    public TempFile append(byte[] bytes) throws IOException {
        Files.write(this.getPath(), bytes, StandardOpenOption.APPEND);
        return this;
    }

    /**
     * Append text to file
     *
     * @return File Interface
     * @throws IOException If you file not exists
     * @since 1.0
     */
    @Override
    public TempFile append(String text) throws IOException {
        return append(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Remove file
     *
     * @return If virtual file return true allays;
     * @throws IOException If you file not exists
     * @since 1.0
     */
    @Override
    public boolean remove() throws IOException {
        return Files.deleteIfExists(getPath());
    }

    /**
     * Check is virtual file
     *
     * @since 1.0
     */
    @Override
    public boolean isFile() {
        return true;
    }

    /**
     * Get virtual link
     *
     * @return text link
     * @since 1.0
     */
    @Override
    public String virtualLink() {
        return this.links == null
                ? IFile.super.virtualLink()
                : String.join("/", this.links);
    }
}
