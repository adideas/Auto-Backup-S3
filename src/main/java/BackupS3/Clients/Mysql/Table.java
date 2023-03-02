package BackupS3.Clients.Mysql;

import BackupS3.Libs.Files.IFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Mysql interface table
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class Table extends HashMap<String, String>{

    /**
     * Raw SQL
     * @param database Name database
     * @return SQL script
     * @since 1.0
     */
    private static String getSqlCode(String database) {
        return "SELECT table_name, table_type, table_schema "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE table_schema = '" + database + "';";
    }

    /**
     * Get list tables
     *
     * @param connection Mysql connection
     * @param database Name database
     * @return list tables
     * @throws SQLException If the connection is not established
     * @since 1.0
     */
    public static DataSet<Table> getTables(Connection connection, String database) throws SQLException {
        Statement statement = connection.createStatement();

        try {
            return DataSet.from(Table.class, statement.executeQuery(getSqlCode(database)));
        } catch (Exception ignore) {
            return new DataSet<>();
        }
    }

    /**
     * Table name
     *
     * @return table name
     * @since 1.0
     */
    public String name() {
        return this.get("TABLE_NAME");
    }

    /**
     * Database name
     *
     * @return database name
     * @since 1.0
     */
    public String database() {
        return this.get("TABLE_SCHEMA");
    }

    /**
     * Check if base table
     * @since 1.0
     */
    public boolean isBaseTable() {
        return this.get("TABLE_TYPE").equals("BASE TABLE");
    }

    /**
     * Get sql script create table
     *
     * @param connection Mysql connection
     * @param buffer file
     * @throws SQLException If the connection is not established
     * @throws IOException If file not exists
     * @since 1.0
     */
    public void getCreateTable(Connection connection, IFile buffer) throws SQLException, IOException {
        buffer.append(TableSchema.getSchema(connection, this));

        if (this.isBaseTable()) {
            TableInsert.getInsert(connection, this, buffer);
        }
    }
}

