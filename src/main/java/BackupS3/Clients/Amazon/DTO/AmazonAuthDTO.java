package BackupS3.Clients.Amazon.DTO;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;

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
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class AmazonAuthDTO implements IAmazonAuthDTO {
    private transient AWSCredentials credentials = null;
    private transient Regions region = null;

    /**
     * {@inheritDoc}
     *
     * @return Provides access to the AWS credentials used for accessing AWS services.
     * @since 1.0
     */
    @Override
    public AWSCredentials getCredentials() {
        return this.credentials;
    }

    /**
     * {@inheritDoc}
     *
     * @param credentials Access to the AWS credentials.
     * @return Auth Amazon AWS {@link AmazonAuthDTO}
     * @since 1.0
     */
    @Override
    public AmazonAuthDTO setCredentials(AWSCredentials credentials) {
        this.credentials = credentials;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return Returns a region name.
     * @since 1.0
     */
    @Override
    public Regions getRegion() {
        return this.region;
    }

    /**
     * {@inheritDoc}
     *
     * @param region Enumeration of region name.
     * @return Auth Amazon AWS {@link AmazonAuthDTO}
     * @since 1.0
     */
    @Override
    public AmazonAuthDTO setRegion(Regions region) {
        this.region = region;
        return this;
    }
}
