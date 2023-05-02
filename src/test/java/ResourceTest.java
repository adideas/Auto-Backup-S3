import BackupS3.Libs.ResourceLoader.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceTest {
    private static final String template = "Example you resource\nMy name is {{NAME}}!\n";
    private static Path file;
    private static final HashMap<String, String> replace = new HashMap<>(){{
        put("{{NAME}}", "MY_NAME");
    }};

    @BeforeAll
    static void init() throws Exception {
        Path resources = Paths.get(System.getProperty("user.dir"), "src", "test", "resources");
        file = resources.resolve("file_resource_test.form");
        Files.writeString(file, template);
    }

    private static URL getResourceLink() {
        return ResourceTest.class.getResource("file_resource_test.form");
    }

    @Test
    void read() {
        if (!Files.exists(file)) {
            return;
        }
        assertEquals(template, Resource.read(getResourceLink()));
    }

    @Test
    void testRead() {
        if (!Files.exists(file)) {
            return;
        }
        assertEquals(
                template.replace("{{NAME}}", "MY_NAME"),
                Resource.read(getResourceLink(), replace)
        );
    }
}