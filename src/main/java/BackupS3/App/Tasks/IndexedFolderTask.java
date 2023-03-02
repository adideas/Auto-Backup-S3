package BackupS3.App.Tasks;

import BackupS3.App.Helpers.FlareLog;
import BackupS3.App.Loggers.IndexedFolderLogger;
import BackupS3.App.Stories.IndexFileStore;
import BackupS3.Clients.Amazon.AmazonClient;
import BackupS3.Configs.Env;
import BackupS3.Configs.IndexedFolderConfig;
import BackupS3.Libs.BindFilesystem.BindFilesystem;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Task indexed files
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class IndexedFolderTask implements Callable<Void> {
    private final IndexedFolderConfig config;

    public IndexedFolderTask(IndexedFolderConfig indexedFolderConfig) {
        this.config = indexedFolderConfig;
    }

    /**
     * {@inheritDoc}
     *
     * <p> Method run task </p>
     * @return void
     * @since 1.0
     */
    @Override
    public Void call() {
        try {
            IndexedFolderLogger.info("Indexed folder - run task [" + config.getPathString() + "]");

            // Load store
            IndexFileStore store = new IndexFileStore(config.getCacheFile());
            store.init();

            // Indexed files
            BindFilesystem.sync(config.getPath(), store::existOrAdd, this::sendFile);

            IndexedFolderLogger.info("Indexed folder - end task [" + config.getPathString() + "]");
        } catch (Exception error) {
            IndexedFolderLogger.error("Indexed folder - error task [" + config.getPathString() + "]");
            FlareLog.message(error);
        }

        return null;
    }

    /**
     * File send to Amazon
     *
     * @param fileFrom File
     * @return If send file then return true
     */
    private boolean sendFile(Path fileFrom) {
        // Get relative address Amazon
        Path fileLink = config.getPathAmazon(config.getPath().relativize(fileFrom));

        try {
            IndexedFolderLogger.info("Indexed folder - Amazon send file [" + fileFrom + " > " + fileLink + "]");

            // Start send file
            new AmazonClient(Env.getAmazonAuth()).setBucket(this.config.getAmazonBucketName()).send(fileFrom, fileLink);
            return true;
        } catch (Exception error) {
            IndexedFolderLogger.info("Indexed folder - Amazon error send file [" + fileLink + "]");
            IndexedFolderLogger.error(error.getMessage());
            FlareLog.message(error, fileLink.toString());
        }
        return false;
    }
}
