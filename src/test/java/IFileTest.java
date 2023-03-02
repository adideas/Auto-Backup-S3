import BackupS3.Libs.Files.TempFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IFileTest {
    @Test
    public void start() throws IOException {
        TempFile file = new TempFile("1","2");

        try {
            test(file);
        } catch (Exception error) {
            file.remove();
        }
    }
    public void test(TempFile file) throws IOException {
        System.out.println(file.getPath());

        assertTrue(Files.exists(file.getPath()));

        String randomStr = getRandomString();
        byte[] randomBytes = getRandomBytes();

        file.append(randomStr);
        file.append(randomBytes);

        assertTrue(file.isFile());

        assertEquals(file.virtualLink(), Paths.get("1","2").toString());

        file.remove();
    }

    private static String getRandomString() {
        return new String(getRandomBytes(), StandardCharsets.UTF_8);
    }

    private static byte[] getRandomBytes() {
        byte[] array = new byte[20];
        new Random().nextBytes(array);
        return array;
    }
}
