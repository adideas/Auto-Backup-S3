package BackupS3.Configs;

import BackupS3.Libs.Files.IFile;
import com.google.gson.annotations.SerializedName;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Amazon path mapping
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class BucketConfig {
    @SerializedName("BUCKET") private String BUCKET = "bucket"; // Name bucket in amazon
    @SerializedName("FOLDER") private String FOLDER = "folder_in_bucket"; // Name folder in bucket [bucket / folder]

    /**
     * Get name bucket
     *
     * @return name bucket
     * @since 1.0
     */
    public String getBucket() {
        return this.BUCKET;
    }

    /**
     * Get path for amazon
     *
     * @param path Relelative path
     * @return Path for Amazone
     * @since 1.0
     */
    public Path getPathAmazon(Path path) {
        return Paths.get(this.FOLDER).resolve(path);
    }

    /**
     * Get path for amazon
     *
     * @param file Relelative path
     * @return Path for Amazone
     * @since 1.0
     */
    public Path getPathAmazon(IFile file) {
        return Paths.get(this.FOLDER).resolve(file.virtualLink());
    }
}
