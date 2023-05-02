import BackupS3.Clients.FlareApp.FlareDocs;
import BackupS3.Clients.FlareApp.FlareError;
import BackupS3.Libs.ResourceLoader.Resource;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlareAppTest {
    @Test
    public void testErrorDescription() {
        String json = Resource.read(FlareAppTest.class.getResource("/flare_app_test.json"));

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
