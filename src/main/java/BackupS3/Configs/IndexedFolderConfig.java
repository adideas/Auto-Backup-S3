package BackupS3.Configs;

import BackupS3.Configs.Functions.ConfigFileFunction;
import BackupS3.Configs.Functions.ConfigPathFunction;
import BackupS3.Configs.Functions.ConfigUseFunction;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.nio.file.Path;

/**
 * Configuration for file system indexing.
 * A process that simply constantly collects data about where and what files / folders are,
 * trying not to overload the system too much.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class IndexedFolderConfig implements ConfigUseFunction, ConfigFileFunction, ConfigPathFunction {
    @SerializedName("USE") private Boolean use = false;
    @SerializedName("PATH") private String path = "";
    @SerializedName("CACHE_FILE_NAME") private String cacheFile = "";
    @SerializedName("CRON") private CronConfig cronConfig = new CronConfig();
    @SerializedName("AMAZON") private BucketConfig amazon = new BucketConfig();

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    public boolean isUse() {
        return this.use;
    }

    /**
     * Get datastore file
     * @return File datastore
     * @since 1.0
     */
    public File getCacheFile() {
        return this.isUse() ? TO_FILE(this.cacheFile) : null;
    }

    /**
     * Return the path of the observed object
     * @return object Path
     */
    public Path getPath() {
        return this.isUse() ? TO_PATH_OR_NULL(this.getPathString()) : null;
    }

    /**
     * Return the path of the observed object
     * @return object text address
     */
    public String getPathString() {
        return this.path;
    }

    /**
     * Get task scheduler configuration
     *
     * @return Task scheduler configuration
     * @since 1.0
     */
    public CronConfig getCronConfig() {
        return this.cronConfig;
    }

    /**
     * Get name bucket
     *
     * @return name bucket {@link BucketConfig#getBucket()}
     * @since 1.0
     */
    public String getAmazonBucketName() {
        return this.amazon.getBucket();
    }

    /**
     * Get path for amazon
     *
     * @param path Relelative path
     * @return Path for Amazone {@link BucketConfig#getPathAmazon(Path)}
     * @since 1.0
     */
    public Path getPathAmazon(Path path) {
        return this.amazon.getPathAmazon(path);
    }
}
