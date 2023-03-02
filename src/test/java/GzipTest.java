import BackupS3.Libs.Gzip.Gzip;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GzipTest {
    @Test
    public void testFile() throws IOException {
        Path testResource = Paths.get(System.getProperty("user.dir"), "src", "test", "resources");

        Path file = Paths.get(testResource.toString(),"testGzip.txt");
        Path output = Paths.get(testResource.toString(), "testGzip.zip");

        Files.deleteIfExists(file);
        Files.deleteIfExists(output);

        Files.writeString(file, "Gzip test file for archive");
        Gzip.archive(file, output);

        String text;

        try (
            GZIPInputStream gis = new GZIPInputStream(
                    new FileInputStream(output.toFile())
            )
        ) {
            byte[] bytes = gis.readAllBytes();
            text = new String(bytes, StandardCharsets.UTF_8);
        }

        assertEquals(text, "Gzip test file for archive", "Error unzip");

        Files.deleteIfExists(file);
        Files.deleteIfExists(output);
    }

    @Test
    public void testFolder() throws IOException {
        Path testResource = Paths.get(System.getProperty("user.dir"), "src", "test", "resources");

        Path file = Paths.get(testResource.toString(),"GzipTest", "folder", "testGzip.txt");
        Files.deleteIfExists(file);

        Path folder = Paths.get(testResource.toString(), "GzipTest");
        Path folder1 = Paths.get(testResource.toString(), "GzipTest", "folder");
        Files.deleteIfExists(folder1);
        Files.deleteIfExists(folder);
        folder.toFile().mkdir();
        folder1.toFile().mkdir();


        Path output = Paths.get(testResource.toString(), "GzipTest.tar");
        Files.deleteIfExists(output);

        Files.writeString(file, "Gzip test file for archive");

        Gzip.archive(folder, output);

        Files.deleteIfExists(file);

        TarArchiveInputStream tararchiveinputstream = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(Files.newInputStream(output))));

        ArchiveEntry archiveentry;
        while( (archiveentry = tararchiveinputstream.getNextEntry()) != null ) {
            Path pathEntryOutput = folder.resolve(archiveentry.getName());
            if( archiveentry.isDirectory() ) {
                if( !Files.exists( pathEntryOutput ) )
                    Files.createDirectory( pathEntryOutput );
            }
            else
                Files.copy( tararchiveinputstream, pathEntryOutput );
        }

        tararchiveinputstream.close();

        assertEquals(Files.readString(file), "Gzip test file for archive", "Error unzip");

        Files.deleteIfExists(file);
        Files.deleteIfExists(folder1);
        Files.deleteIfExists(folder);
        Files.deleteIfExists(output);
    }
}
