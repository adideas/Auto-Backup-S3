import BackupS3.Clients.FlareApp.FlareDocs;
import BackupS3.Clients.FlareApp.FlareError;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlareAppTest {
    @Test
    public void testErrorDescription() {
        String json = """
                {
                    "class_name": "FlareAppTest$FlareAppExcTest",
                    "code_snippet": {
                        "1": "Test-description"
                    },
                    "column_number": 1,
                    "file": "FlareAppTest.java",
                    "line_number": 1,
                    "method": "makeError",
                    "trimmed_column_number": 500
                }
                """;

        json = json.replaceAll("\n", "");
        json = json.trim().replaceAll(" +", "");

        try {
            new FlareAppExcTest().makeError();
        } catch (Exception error) {
            assertEquals(
                    json,
                    new Gson().toJson(new FlareError(error.getStackTrace()[0]))
            );
        }
    }

    public static class FlareAppExcTest {
        @FlareDocs("Test-description")
        public void makeError() throws Exception {
            throw new Exception("TEST");
        }
    }
}
