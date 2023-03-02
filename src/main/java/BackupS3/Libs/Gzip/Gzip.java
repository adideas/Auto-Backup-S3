package BackupS3.Libs.Gzip;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
/**
 * Zip any files or folders
 *
 * <a href="https://mkyong.com/java/how-to-create-tar-gz-in-java/">create tar gz</a>
 * <a href="https://mkyong.com/java/how-to-compress-a-file-in-gzip-format/">compress a file in gzip</a>
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class Gzip {
    /**
     * Get stream to archive folder
     *
     * @return Archive stream
     * @throws IOException If you file not exists
     * @since 1.0
     */
    private static TarArchiveOutputStream getArchiveFolderStream(String pathArchive) throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
                new GzipCompressorOutputStream(
                        new BufferedOutputStream(Files.newOutputStream(Paths.get(pathArchive)))
                )
        );

        // Вам нужно установить формат в posix перед использованием потока:
        // http://commons.apache.org/proper/commons-compress/tar.html#Long_File_Names
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

        return tarArchiveOutputStream;
    }

    /**
     * Get stream to archive file
     *
     * @return Archive stream
     * @throws IOException If you file not exists
     * @since 1.0
     */
    private static GZIPOutputStream getArchiveFileStream(String pathArchive) throws IOException {
        return new GZIPOutputStream(
                new FileOutputStream(pathArchive)
        );
    }

    /**
     * Archive object
     *
     * @param pathObject  - FROM OBJECT {FILE or FOLDER}
     * @param pathArchive - TO OBJECT {TAR or ZIP}
     * @since 1.0
     */
    public static void archive(Path pathObject, Path pathArchive) throws IOException {
        if (Files.notExists(pathObject)) {
            throw new IOException();
        }

        if (Files.isDirectory(pathObject)) {

            try (TarArchiveOutputStream tarArchiveOutputStream = getArchiveFolderStream(pathArchive.toString())) {
                Files.walkFileTree(pathObject, new TreeFiles(tarArchiveOutputStream, pathObject));
                tarArchiveOutputStream.finish();
            }


        } else {

            try (GZIPOutputStream gzipOutputStream = getArchiveFileStream(pathArchive.toString())) {
                Files.copy(pathObject, gzipOutputStream);
            }

        }

        Files.exists(pathArchive);
    }
}
