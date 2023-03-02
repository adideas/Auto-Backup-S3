package BackupS3.Configs;

import BackupS3.Clients.Amazon.DTO.IAmazonAuthDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * This class contains the application configuration.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class Env {
    protected final static String configFile = "BackupS3.env.json";
    private final static Store store = Store.getInstance();

    public static class Store {
        @SerializedName("LOG") private LogConfig LOG = new LogConfig();
        @SerializedName("SCHEDULER") private SchedulerConfig SCHEDULER = new SchedulerConfig();
        @SerializedName("AMAZON") private AmazonConfig AMAZON = new AmazonConfig();
        @SerializedName("MYSQL_CONFIGS") private ArrayList<MysqlConfig> MYSQL_CONFIGS = new ArrayList<>(){{add(new MysqlConfig());}};
        @SerializedName("FLARE_APP") private FlareConfig FLARE_APP = new FlareConfig();
        @SerializedName("INDEXED_FOLDERS") private ArrayList<IndexedFolderConfig> INDEXED_FOLDERS = new ArrayList<>(){{add(new IndexedFolderConfig());}};;

        public static Env.Store getInstance() {
            if (Files.exists(path())) {
                try { return new Gson().fromJson(Files.readString(path()), Env.Store.class); } catch (Exception ignore) {}
            }
            return new Env.Store();
        }
    }

    /**
     * The file of syslog protocol, which is used to convey event notification messages.
     * <a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>
     *
     * @return Log file
     * @since 1.0
     */
    public static Path getLogFile() {
        return store.LOG.getLogFile();
    }

    /**
     * This method return Flare secret key. <p></p>
     *
     * Flare is exception tracker that helps you spend more time fixing bugs and less time uncovering them.
     * Over at Spatie we built Flare with the same amount of care and attention we put into the hundreds
     * of open source packages we maintain.
     * It's the exception tracker that we always wanted.
     *
     * @apiNote
     *  <li><i>Open project</i></li>
     *  <li><i>Settings</i></li>
     *  <li><i>Configuration</i></li>
     *  <li><i>Search you secret key</i></li>
     * @return Your public key for <a href="https://flareapp.io/">Flare.app</a>
     *
     * @since 1.0
     */
    public static String getFlareKey() {
        return store.FLARE_APP.getKey();
    }

    /**
     * This method return Amazon configuration.
     *
     * @apiNote
     *      Provides access to the AWS credentials used for accessing AWS services:
     *          AWS access key ID and secret access key.
     *      These credentials are used to securely sign requests to AWS services.
     *
     * @return Amazon Authorization Data
     * @since 1.0
     */
    public static IAmazonAuthDTO getAmazonAuth() {
        return store.AMAZON.getAuth();
    }

    /**
     * The method return sheduler configuration
     *
     * @return SchedulerConfig
     * @since 1.0
     */
    public static SchedulerConfig getSchedulerConfig() {
        return store.SCHEDULER;
    }

    /**
     * The method of enumeration of file indexing configurations
     * @param call Callback
     * @since 1.0
     *
     * <pre>{@code
     * // Example:
     *
     * import BackuperS3.Configs.Env;
     * public class Main {
     *     public static void main(String[] args) {
     *         Env.eachIndexedFolders((IndexedFolderConfig) -> {});
     *     }
     * }
     *
     * }</pre>
     */
    public static void eachIndexedFolders(Consumer<IndexedFolderConfig> call) {
        // Iteration configuration
        store.INDEXED_FOLDERS.forEach(indexedFolderConfig -> {
            // If you use configuration
            if (indexedFolderConfig.isUse()) {
                // Callback action
                call.accept(indexedFolderConfig);
            }
        });
    }

    /**
     * Database configuration iteration method.
     * @param call Callback
     * @since 1.0
     *
     * <pre>{@code
     * // Example:
     *
     * import BackuperS3.Configs.Env;
     * public class Main {
     *     public static void main(String[] args) {
     *         Env.eachMysqlConfigs((MysqlConfig) -> {});
     *     }
     * }
     *
     * }</pre>
     */
    public static void eachMysqlConfigs(Consumer<MysqlConfig> call) {
        // Iteration configuration
        store.MYSQL_CONFIGS.forEach(mysqlConfig -> {
            // If you use configuration
            if (mysqlConfig.isUse()) {
                // Callback action
                call.accept(mysqlConfig);
            }
        });
    }

    /**
     * Get path configuration (env file)
     * @return Path env file
     * @since 1.0
     */
    public static Path path() {
        return Paths.get(System.getProperty("user.dir"), configFile);
    }

    /**
     * This method create env file if not exist
     *
     * @since 1.0
     * @apiNote Run this method with start app.
     * @throws IOException Save file
     * @since 1.0
     *
     * <pre>{@code
     * // Example:
     *
     * import BackuperS3.Configs.Env;
     * public class Main {
     *     public static void main(String[] args) {
     *         Env.createIfNotExists();
     *     }
     * }
     *
     * }</pre>
     */
    public static void createIfNotExists() {
        try {
            Path path = path();
            if (!Files.exists(path)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Files.writeString(path, gson.toJson(store));
            }

            System.out.println(path.toFile().getAbsolutePath());
        } catch (Exception error) {
            System.exit(1);
        }
    }
}
