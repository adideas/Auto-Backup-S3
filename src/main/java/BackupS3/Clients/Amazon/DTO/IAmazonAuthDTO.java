package BackupS3.Clients.Amazon.DTO;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;

/**
 * Authorization data transfer interface (AWS)
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public interface IAmazonAuthDTO {
    /**
     * Provides access to the AWS credentials used for accessing AWS services
     * <li>Returns the AWS access key ID for this credentials object.</li>
     * <li>Returns the AWS secret access key for this credentials object.</li>
     *
     * @return Provides access to the AWS credentials used for accessing AWS services.
     * @since 1.0
     */
    AWSCredentials getCredentials();

    /**
     * Set access to the AWS credentials used for accessing AWS services.
     *
     * @param credentials Access to the AWS credentials.
     * @return Auth Amazon AWS {@link IAmazonAuthDTO}
     * @since 1.0
     */
    IAmazonAuthDTO setCredentials(AWSCredentials credentials);

    /**
     * Returns a region enum corresponding to the given region name.
     *
     * @return Returns a region name.
     * @since 1.0
     */
    Regions getRegion();

    /**
     * Set a region enum corresponding to the given region name.
     *
     * @param region Enumeration of region name.
     * @return Auth Amazon AWS {@link IAmazonAuthDTO}
     * @since 1.0
     */
    IAmazonAuthDTO setRegion(Regions region);
}
