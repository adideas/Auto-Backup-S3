package BackupS3.Configs;

import BackupS3.Clients.Amazon.DTO.AmazonAuthDTO;
import BackupS3.Clients.Amazon.DTO.IAmazonAuthDTO;
import BackupS3.Configs.Functions.ConfigUseFunction;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.google.gson.annotations.SerializedName;

/**
 * Authorization config Amazon (AWS)
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class AmazonConfig implements ConfigUseFunction {
    @SerializedName("USE") private Boolean USE = false;
    @SerializedName("ACCESS_KEY") private String ACCESS_KEY = "***";
    @SerializedName("SECRET_KEY") private String SECRET_KEY = "***";
    @SerializedName("REGION") private String REGION = "eu-central-1";

    /**
     * {@inheritDoc}
     *
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
     * <li>
     *      Provides access to the AWS credentials used for accessing
     *      AWS services: AWS access key ID and secret access key.
     *      These credentials are used to securely sign requests to AWS services.
     * </li>
     * <li>
     *     Returns a region enum corresponding to the given region name.
     * </li>
     *
     * @return Auth Amazon AWS (if you is use) {@link AmazonAuthDTO}
     * @since 1.0
     */
    public final IAmazonAuthDTO getAuth() {
        if (!this.isUse()) return null;

        return new AmazonAuthDTO()
                .setCredentials(new BasicAWSCredentials(this.ACCESS_KEY, this.SECRET_KEY))
                .setRegion(Regions.fromName(this.REGION));
    }
}
