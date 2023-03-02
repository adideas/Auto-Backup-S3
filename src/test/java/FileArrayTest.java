import BackupS3.Libs.Store.FileArray;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileArrayTest {
    @Test
    public void test() throws NoSuchMethodException, IOException {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "file_array.bin");
        Files.deleteIfExists(file);

        Store store = new Store(file.toFile());
        HashSet<Long> data = new HashSet<>();
        for (int i = 0; i < 1; i++) {
            long v = (long) (Math.random() * 1000000);
            data.add(v);
            store.add(v);
        }
        store.close();

        Store store1 = new Store(file.toFile());
        assertEquals(data, store1.getData());
        Files.deleteIfExists(file);
    }

    static class Store extends FileArray<Long> {
        HashSet<Long> data = new HashSet<>();
        public Store(File file) throws NoSuchMethodException {
            super(Long.class, file);
        }

        @Override
        protected void read(Long value) {
            data.add(value);
        }

        public void add(long value) {
            write(value);
        }

        public HashSet<Long> getData() {
            init();
            return data;
        }
    }
}
