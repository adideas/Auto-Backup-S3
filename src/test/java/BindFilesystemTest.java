import BackupS3.Libs.BindFilesystem.BindFilesystem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BindFilesystemTest {
    @Test
    public void test() throws IOException {
        Path resources = Paths.get(System.getProperty("user.dir"), "src", "test", "resources");

        Path bindFilesystem = resources.resolve("bindFilesystem");
        Path includeFolder = bindFilesystem.resolve("includeFolder");

        Path file_1 = bindFilesystem.resolve("file_1.txt");
        Path file_2 = includeFolder.resolve("file_2.txt");

        Files.deleteIfExists(file_2);
        Files.deleteIfExists(file_1);
        Files.deleteIfExists(includeFolder);
        Files.deleteIfExists(bindFilesystem);

        bindFilesystem.toFile().mkdirs();
        includeFolder.toFile().mkdirs();

        Files.writeString(file_1, "BackuperS3.BindFilesystemTest");
        Files.writeString(file_2, "BackuperS3.BindFilesystemTest");

        HashMap<String, Long> map_1 = getMap(bindFilesystem);
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file_2, BasicFileAttributes.class);
        Files.writeString(file_2, "BackuperS3.BindFilesystemTest-BackuperS3.BindFilesystemTest");
        assertNotEquals(map_1, getMap(bindFilesystem));

        Files.setLastModifiedTime(file_2, basicFileAttributes.lastModifiedTime());
        assertEquals(map_1, getMap(bindFilesystem));

        Files.deleteIfExists(file_2);
        Files.deleteIfExists(file_1);
        Files.deleteIfExists(includeFolder);
        Files.deleteIfExists(bindFilesystem);
    }

    private HashMap<String, Long> getMap(Path path) {
        AtomicReference<String> fileName = new AtomicReference<String>();
        HashMap<String, Long> map = new HashMap<>();


        BindFilesystem.sync(
                path,
                (isExist, hash) -> {
                    if (!isExist) {
                        map.put(fileName.get(), hash);
                    }
                    return false;
                },
                (file) -> {
                    fileName.set(file.toString());
                    return true;
                }
        );

        return map;
    }
}
