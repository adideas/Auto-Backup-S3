package BackupS3.Interfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Read a file from resources folder
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 * @since 1.1
 */
public class Resource {
    /**
     * Read template file and replace parameters
     *
     * @param resource URL resource
     * @param data parameters
     * @return raw string (resource)
     * @since 1.1
     */
    public static String read(URL resource, HashMap<String, String> data) {
        String out = read(resource);
        for (Map.Entry<String, String> item : data.entrySet()) {
            out = out.replace(item.getKey(), item.getValue());
        }
        return out;
    }

    /**
     * Read resource file
     *
     * @param resource URL resource
     * @return raw string (resource)
     * @since 1.1
     */
    public static String read(URL resource) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.openStream()));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            bufferedReader.close();

            return builder.toString();
        } catch (Exception ignore) {}

        return "";
    }
}
