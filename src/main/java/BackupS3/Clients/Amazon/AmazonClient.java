package BackupS3.Clients.Amazon;

import BackupS3.Clients.Amazon.DTO.IAmazonAuthDTO;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Amazon client
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
@SuppressWarnings("UnusedReturnValue")
public class AmazonClient {
    /**
     * Sets the size threshold in bytes when multipart downloads should be used.
     */
    private static final long FILE_SIZE_WITHOUT_MULTI_UPLOAD = 5 * 1024 * 1025;
    /**
     * Maximum number of pools
     */
    private final ExecutorService POOLS = Executors.newFixedThreadPool(2);

    private final AmazonS3 transport;
    private Bucket bucket;

    /**
     * Create amazon client
     *
     * @param amazonAuth Authorization data transfer interface (AWS)
     * @since 1.0
     */
    public AmazonClient(IAmazonAuthDTO amazonAuth) {
        this(amazonAuth.getCredentials(), amazonAuth.getRegion());
    }

    /**
     * Create amazon client
     *
     * @param credentials Provides access to the AWS credentials used for accessing AWS services.
     * @param region A region enum corresponding to the given region name.
     * @since 1.0
     */
    public AmazonClient(AWSCredentials credentials, Regions region) {
        this.transport = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    /**
     * Set bucket for client
     *
     * @param bucket Bucket
     * @return Client
     * @since 1.0
     */
    public AmazonClient setBucket(Bucket bucket) {
        this.bucket = bucket;
        return this;
    }

    /**
     * Set bucket for client
     *
     * @param bucketName Bucket name
     * @return Client
     * @since 1.0
     */
    public AmazonClient setBucket(String bucketName) {
        for(Bucket bucket : this.transport.listBuckets()) {
            if (bucket.getName().equals(bucketName)) {
                this.setBucket(bucket);
                break;
            }
        }
        return this;
    }

    /**
     * Get transport
     * @return transport
     * @since 1.0
     */
    private TransferManager getTransferManager() {
        return TransferManagerBuilder.standard()
                .withS3Client(this.transport)
                .withMultipartUploadThreshold(FILE_SIZE_WITHOUT_MULTI_UPLOAD)
                .withExecutorFactory(() -> POOLS)
                .build();
    }

    /**
     * Send file to Amazon
     *
     * @param filePath File in current system
     * @param link Path in Amazon
     * @throws IOException If file not exists
     * @throws InterruptedException If thread error
     * @since 1.0
     */
    public void send(Path filePath, String link) throws IOException, InterruptedException  {
        boolean fileExists = Files.exists(filePath) && Files.isRegularFile(filePath);
        boolean bucketExists = this.bucket != null;

        if (!fileExists) { throw new IOException("File not found"); }
        if (!bucketExists) { throw new IOException("Bucket not found"); }

        TransferManager transfer = getTransferManager();
        Upload upload = transfer.upload(this.bucket.getName(), link, filePath.toFile());

        upload.waitForCompletion();
        transfer.shutdownNow();

        this.bucket = null;
        this.transport.shutdown();
        this.POOLS.shutdown();
    }

    /**
     * Send file to Amazon
     *
     * @param filePath File in current system
     * @param link Path in Amazon
     * @throws IOException If file not exists
     * @throws InterruptedException If thread error
     * @since 1.0
     */
    public void send(Path filePath, Path link) throws IOException, InterruptedException {
        send(filePath, link.toString());
    }
}