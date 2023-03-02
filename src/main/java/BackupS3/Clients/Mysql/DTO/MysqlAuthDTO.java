package BackupS3.Clients.Mysql.DTO;

import BackupS3.Libs.Files.IFile;

import java.sql.Connection;
import java.util.Set;

/**
 * Authorization data MySQL
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class MysqlAuthDTO implements IMysqlAuthDTO {
    private Connection connection = null;
    private String host = null;
    private String database = null;
    private IFile file = null;
    private Set<String> ignoreTables = null;

    /**
     * {@inheritDoc}
     *
     * @return File object
     * @since 1.0
     */
    @Override
    public IFile getFile() {
        return file;
    }

    /**
     * {@inheritDoc}
     *
     * @return SQL connection
     * @since 1.0
     */
    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * {@inheritDoc}
     *
     * @return list ignore tables
     * @since 1.0
     */
    @Override
    public Set<String> getIgnoreTables() {
        return ignoreTables;
    }

    /**
     * {@inheritDoc}
     *
     * @return host name
     * @since 1.0
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * {@inheritDoc}
     *
     * @return database name
     * @since 1.0
     */
    @Override
    public String getDatabase() {
        return database;
    }

    /**
     * {@inheritDoc}
     *
     * @param file file
     * @return Authorization data MySQL
     * @since 1.0
     */
    @Override
    public IMysqlAuthDTO setFile(IFile file) {
        this.file = file;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param connection SQL connection
     * @return Authorization data MySQL
     * @since 1.0
     */
    @Override
    public IMysqlAuthDTO setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    /**
     *
     * @param ignoreTables list tables
     * @return Authorization data MySQL
     * @since 1.0
     */
    @Override
    public IMysqlAuthDTO setIgnoreTables(Set<String> ignoreTables) {
        this.ignoreTables = ignoreTables;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param host host (localhost...)
     * @return Authorization data MySQL
     * @since 1.0
     */
    @Override
    public IMysqlAuthDTO setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param database name database
     * @return Authorization data MySQL
     * @since 1.0
     */
    @Override
    public IMysqlAuthDTO setDatabase(String database) {
        this.database = database;
        return this;
    }
}
