package BackupS3.Clients.FlareApp;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FlareApp {
    static String uri = "https://flareapp.io/api/reports";

    public static void post(String key, String body) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            HttpUriRequest request = RequestBuilder.post(uri)
                    .addHeader(":authority", "flareapp.io")
                    .addHeader(":method", "POST")
                    .addHeader(":path", "/api/reports")
                    .addHeader(":scheme", "https")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-token", key)
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "cross-site")
                    .addHeader("sec-fetch-dest", "empty")
                    .setEntity(
                            new StringEntity(
                                    body, ContentType.APPLICATION_JSON
                            )
                    ).build();

            client.execute(request);
        } catch (Exception ignore) {}
    }

    public static String getBody(String key, Object... objects) {
        Exception exception = null;
        String message = null;

        for (Object value : objects) {
            if (value instanceof Exception) {
                exception = (Exception) value;
            }
            if (value instanceof String) {
                message = (String) value;
            }
        }

        if (exception != null && message != null) {
            return new Gson().toJson(new FlareBody(key, exception, message));
        } else if (exception != null) {
            return new Gson().toJson(new FlareBody(key, exception));
        } else if (message != null) {
            return new Gson().toJson(new FlareBody(key, message));
        }
        return null;
    }

    public static void setUrl(String uri) {
        FlareApp.uri = uri;
    }
}
