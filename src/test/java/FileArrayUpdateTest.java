import BackupS3.Libs.Store.FileArray;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileArrayUpdateTest {
    @Test
    public void test() throws Exception {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "file_array_update.bin");
        Files.deleteIfExists(file);

        Store store = new Store(file.toFile());
        store.add(1L);
        store.add(2L);
        store.add(3L);
        store.add(4L);
        store.runUpdate(0, 5L);
        store.runUpdate(1, 6L);
        store.runUpdate(2, 7L);
        store.add(1L);
        store.close();

        Store store1 = new Store(file.toFile());
        store1.init();

        assertEquals(store1.values, new ArrayList<>(){{
            add(5L);
            add(6L);
            add(7L);
            add(4L);
            add(1L);
        }});

        Files.deleteIfExists(file);
    }

    static class Store extends FileArray<Long> {

        ArrayList<Long> values = new ArrayList<>();
        public Store(File file) throws NoSuchMethodException {
            super(Long.class, file);
        }

        @Override
        protected void read(Long value) {
            values.add(value);
        }

        public void add(long v) {
            write(v);
        }

        public void runUpdate(int index, long value) throws IOException, InvocationTargetException, IllegalAccessException {
            update(index, value);
        }
    }
}
