import BackupS3.App.Stories.LogFileStore;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LogFileStoreTest {
    private ArrayList<Long> remove = new ArrayList<>();

    @Test
    public void test() throws Exception {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "file_array_update.bin");
        Files.deleteIfExists(file);

        LogFileStore log = new LogFileStore(file.toFile(), 3, remove::add);

        log.add(1L);
        log.add(2L);
        log.add(2L);
        log.add(3L);
        log.add(3L);
        log.add(3L);
        log.add(4L);
        log.add(5L);
        log.add(5L);
        log.add(5L);

        Long[] values = log.getValues();

        assertEquals(remove, new ArrayList<>(){{add(1L); add(2L);}});
        log.close();
        log = null;
        remove = new ArrayList<>();

        log = new LogFileStore(file.toFile(), 3, remove::add);
        log.add(17L);
        Long[] new_values = log.getValues();


        assertEquals(values[0], new_values[0]);
        assertEquals(values[1], new_values[1]);
        assertNotEquals(values[2], new_values[2]);
        assertEquals(remove, new ArrayList<>(){{add(3L);}});

        assertEquals(17L, new_values[2]);
    }
}
