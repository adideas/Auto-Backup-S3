package BackupS3.App.Tasks;

import BackupS3.App.Helpers.FlareLog;
import BackupS3.App.Loggers.MysqlLogger;
import BackupS3.Clients.Amazon.AmazonClient;
import BackupS3.Clients.Mysql.DTO.IMysqlAuthDTO;
import BackupS3.Clients.Mysql.DataSet;
import BackupS3.Clients.Mysql.DumpHeader;
import BackupS3.Clients.Mysql.Table;
import BackupS3.Configs.Env;
import BackupS3.Configs.MysqlConfig;
import BackupS3.Libs.Files.IFile;
import BackupS3.Libs.Files.TempFile;
import BackupS3.Libs.Gzip.Gzip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Task dump table in database
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class MysqlDumpTask implements Callable<Void> {
    /**
     * Iterating over tables in a database
     *
     * @param config MySQL config
     * @param function Action
     * @since 1.0
     */
    public static void eachTables(MysqlConfig config, Consumer<Table> function) {
        try {
            // Get auth data
            IMysqlAuthDTO auth = config.getAuth();
            MysqlLogger.info("Get tables [" + auth.getDatabase() + "]");

            // Get list tables
            DataSet<Table> tables = Table.getTables(auth.getConnection(), auth.getDatabase());
            Set<String> ignoreTables = auth.getIgnoreTables();

            for (Table table : tables) {
                // If tables ignore
                if (ignoreTables != null && ignoreTables.contains(table.name())) {
                    MysqlLogger.info("Tables ignore [" + table.name() + "]");
                    continue;
                }
                MysqlLogger.info("Tables use [" + table.name() + "]");
                // Action table
                function.accept(table);
            }
        } catch (Exception error) {
            MysqlLogger.info("Error get tables");
            FlareLog.message(error);
        }
    }

    private final Table table;
    private final MysqlConfig config;

    public MysqlDumpTask(MysqlConfig config, Table table) {
        this.config = config;
        this.table = table;
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
            // Get auth data
            IMysqlAuthDTO auth = config.getAuth();

            // If you connection
            if (auth.getConnection() != null) {
                IFile file = null;
                try {
                    // Make temp file and create dump
                    file = this.createDump(auth, table);
                } catch (Exception error) {
                    MysqlLogger.error("Error create dump [" + table.database() + "." + table.name() + "]");
                    FlareLog.message(error, table.database() + "." + table.name());
                }

                if (file != null) {
                    this.sendFile(file);
                } else {
                    MysqlLogger.error("Error sendFile [" + table.database() + "." + table.name() + "]");
                }
            } else {
                MysqlLogger.error("Error getConnection [" + table.database() + "." + table.name() + "]");
            }
        } catch (Exception error) {
            MysqlLogger.error("Error task dump [" + table.database() + "." + table.name() + "]");
            FlareLog.message(error, table.database() + "." + table.name());
        }
        return null;
    }

    /**
     * Create dump
     *
     * @param auth Authorization data MySQL
     * @param table Table in database
     * @return Temp File
     * @throws IOException If file not exists
     * @throws SQLException If the connection is not established
     * @since 1.0
     */
    private IFile createDump(IMysqlAuthDTO auth, Table table) throws IOException, SQLException {
        MysqlLogger.info("Create dump [" + table.database() + "." + table.name() + "]");

        IFile file = new TempFile(getRootFolder(), table.database(), table.name() + ".gz");
        file.append(new DumpHeader(auth.getHost(), auth.getDatabase()).toString());
        table.getCreateTable(auth.getConnection(), file);
        return file;
    }

    /**
     * Get root folder
     *
     * @return name root folder
     * @since 1.0
     */
    private static String getRootFolder() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }

    /**
     * File send to Amazon
     *
     * @param dump File
     * @since 1.0
     */
    private void sendFile(IFile dump) {
        // If dump is virtual file
        if (!dump.isFile()) {return;}
        Path zip;

        // Zip dump
        try {
            MysqlLogger.info("Create zip [" + dump.virtualLink() + "]");
            zip = zip(dump);
        } catch (Exception error) {
            MysqlLogger.error("Error create zip [" + dump.virtualLink() + "]");
            try { dump.remove(); } catch (Exception remove) {
                MysqlLogger.error("Error delete dump [" + dump.virtualLink() + "]");
                MysqlLogger.error(remove.getMessage());
                FlareLog.message(remove);
            }
            MysqlLogger.error(error.getMessage());
            FlareLog.message(error, dump.virtualLink());
            return;
        }

        // Send file to Amazon
        try {
            MysqlLogger.info("Send file Amazon [" + dump.virtualLink() + "]");
            new AmazonClient(Env.getAmazonAuth())
                    .setBucket(this.config.getAmazonBucketName())
                    .send(zip, config.getPathAmazon(dump));
        } catch (Exception error) {
            MysqlLogger.error("Error send file Amazon [" + dump.virtualLink() + "]");
            try { dump.remove(); } catch (Exception remove) {
                MysqlLogger.error("Error delete dump [" + dump.virtualLink() + "]");
                MysqlLogger.error(remove.getMessage());
                FlareLog.message(remove);
            }
            try { Files.deleteIfExists(zip); } catch (Exception remove) {
                MysqlLogger.error("Error delete zip [" + dump.virtualLink() + "]");
                MysqlLogger.error(remove.getMessage());
                FlareLog.message(remove);
            }
            MysqlLogger.error(error.getMessage());
            FlareLog.message(error, dump.virtualLink());
            return;
        }

        try { dump.remove(); } catch (Exception remove) {
            MysqlLogger.error("Error delete dump [" + dump.virtualLink() + "]");
            MysqlLogger.error(remove.getMessage());
            FlareLog.message(remove);
        }

        try { Files.deleteIfExists(zip); } catch (Exception remove) {
            MysqlLogger.error("Error delete zip [" + dump.virtualLink() + "]");
            MysqlLogger.error(remove.getMessage());
            FlareLog.message(remove);
        }
    }

    /**
     * Archive file (dump database)
     *
     * @param file File
     * @return Path archive
     * @throws IOException If file not exist
     * @since 1.0
     */
    private static Path zip(IFile file) throws IOException {
        Path temp = Files.createTempFile("temp_file" + Math.random(), ".gz");
        Gzip.archive(file.getPath(), temp);
        file.remove();
        return temp;
    }
}
