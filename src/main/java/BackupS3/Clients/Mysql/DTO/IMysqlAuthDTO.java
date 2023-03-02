package BackupS3.Clients.Mysql.DTO;

import BackupS3.Libs.Files.IFile;

import java.sql.Connection;
import java.util.Set;

/**
 * Authorization data transfer interface
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public interface IMysqlAuthDTO {
    /**
     * Get file buffer
     *
     * @return File object
     * @since 1.0
     */
    IFile getFile();

    /**
     * Get SQL connection
     *
     * @return SQL connection
     * @since 1.0
     */
    Connection getConnection();

    /**
     * Get list ignore tables
     *
     * @return list ignore tables
     * @since 1.0
     */
    Set<String> getIgnoreTables();

    /**
     * Get host name
     *
     * @return host name
     * @since 1.0
     */
    String getHost();

    /**
     * Get database name
     *
     * @return database name
     * @since 1.0
     */
    String getDatabase();


    /**
     * Set file buffer
     *
     * @param buffer file
     * @return Authorization data transfer interface
     * @since 1.0
     */
    IMysqlAuthDTO setFile(IFile buffer);

    /**
     * Set SQL connection
     *
     * @param connection SQL connection
     * @return Authorization data transfer interface
     * @since 1.0
     */
    IMysqlAuthDTO setConnection(Connection connection);

    /**
     * Set ignore tables
     *
     * @param ignoreTables list tables
     * @return Authorization data transfer interface
     * @since 1.0
     */
    IMysqlAuthDTO setIgnoreTables(Set<String> ignoreTables);

    /**
     * Set host database server
     *
     * @param host host (localhost...)
     * @return Authorization data transfer interface
     * @since 1.0
     */
    IMysqlAuthDTO setHost(String host);

    /**
     * Set name database
     *
     * @param database name database
     * @return Authorization data transfer interface
     * @since 1.0
     */
    IMysqlAuthDTO setDatabase(String database);
}
