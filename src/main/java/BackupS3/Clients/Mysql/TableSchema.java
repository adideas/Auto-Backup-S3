package BackupS3.Clients.Mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class TableSchema extends HashMap<String, String> {

    private static String getSqlCode(Table table) {
        return "SHOW CREATE TABLE " + table.database() + "." + table.name();
    }

    private static StringBuilder getHeader(Table table) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("--\r\n");

        stringBuilder.append("-- Table structure for table ")
                .append(table.database()).append(".").append(table.name())
                .append("\r\n");

        stringBuilder.append("--\r\n\r\n");

        return stringBuilder;
    }

    public static String getSchema(Connection connection, Table table) throws SQLException {
        StringBuilder stringBuilder = TableSchema.getHeader(table);

        Statement statement = connection.createStatement();

        try {
            DataSet<TableSchema> dataSet = DataSet.from(
                    TableSchema.class,
                    statement.executeQuery(getSqlCode(table))
            );

            if (dataSet.size() == 1) {
                stringBuilder.append(dataSet.get(0).getSQL());
            }

        } catch (Exception ignore) {}

        return stringBuilder.toString();
    }

    private String getSQL() {
        return this.get("Create Table") + ";\r\n";
    }
}
