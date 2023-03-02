package BackupS3.Configs;

import BackupS3.Clients.Mysql.DTO.IMysqlAuthDTO;
import BackupS3.Clients.Mysql.DTO.MysqlAuthDTO;
import BackupS3.Configs.Functions.ConfigUseFunction;
import BackupS3.Libs.Files.IFile;
import com.google.gson.annotations.SerializedName;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration MySQL connection
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class MysqlConfig implements ConfigUseFunction {
    @SerializedName("USE") private Boolean USE = false;
    @SerializedName("USER") private String USER = "user";
    @SerializedName("PASSWORD") private String PASSWORD = "***";
    @SerializedName("DATABASE") private String DATABASE = "mysql";
    @SerializedName("HOST") private String HOST = "localhost";
    @SerializedName("PORT") private String PORT = "3306";
    @SerializedName("IGNORE_TABLES") private Set<String> IGNORE_TABLES = new HashSet<>();
    @SerializedName("CRON") private CronConfig cronConfig = new CronConfig();
    @SerializedName("AMAZON") private BucketConfig amazon = new BucketConfig();

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    @Override
    public boolean isUse() {
        return this.USE;
    }

    /**
     * Authorization data MySQL
     *
     * @return MySQL auth object
     * @throws SQLException If the connection is not established
     * @since 1.0
     */
    public IMysqlAuthDTO getAuth() throws SQLException {
        if (!this.isUse()) throw new SQLException();

        String url = "jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE;

        return new MysqlAuthDTO()
                .setConnection(DriverManager.getConnection(url, this.USER, this.PASSWORD))
                .setIgnoreTables(this.IGNORE_TABLES)
                .setDatabase(this.DATABASE)
                .setHost(this.HOST);
    }

    /**
     * Get database name
     *
     * @return database name
     * @since 1.0
     */
    public String getDatabaseName() {
        return this.DATABASE;
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
     * @param file Relelative path
     * @return Path for Amazone {@link BucketConfig#getPathAmazon(IFile)}
     * @since 1.0
     */
    public Path getPathAmazon(IFile file) {
        return this.amazon.getPathAmazon(file);
    }
}
