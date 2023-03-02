package BackupS3.Clients.Mysql;

import BackupS3.Libs.Files.IFile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Get insert data
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class TableInsert extends HashMap<String, String> {

    private static final int MAX_COUNT_INSERT = 1000;

    private static String getSqlCode(Table table) {
        return "SELECT COUNT(*) as count FROM " + table.database() + "." + table.name();
    }

    public static void getInsert(Connection connection, Table table, IFile buffer) {
        try {
            Statement statement = connection.createStatement();
            DataSet<TableCount> dataSet = DataSet.from(
                    TableCount.class,
                    statement.executeQuery(getSqlCode(table))
            );

            if (dataSet.size() == 1) {
                TableCount tableCount = dataSet.get(0);
                int count = tableCount.getCount();

                if (count > 0) {

                    buffer.append(""
                            + "LOCK TABLES `" + table.name() + "` WRITE;\r\n"
                            + "/*!40000 ALTER TABLE `" + table.name() + "` DISABLE KEYS */;\r\n\r\n"
                    );

                    for (int offset = 0; offset < count; offset = offset + MAX_COUNT_INSERT) {
                        buffer.append(TableInsert.getInsert(statement, table, MAX_COUNT_INSERT, offset));
                    }

                    buffer.append(""
                            + "/*!40000 ALTER TABLE `" + table.name() + "` ENABLE KEYS */;\r\n"
                            + "UNLOCK TABLES;\r\n"
                    );
                }
            }
        } catch (Exception ignore) {
        }
    }

    private static String getSqlCode(Table table, int limit, int offset) {
        return "SELECT * FROM " + table.database() + "." + table.name()
                + " LIMIT " + limit + " OFFSET " + offset + ";";
    }

    public static String getInsert(Statement statement, Table table, int limit, int offset) {
        try {
            ResultSet resultSet = statement.executeQuery(getSqlCode(table, limit, offset));
            ArrayList<String> columns = DataSet.getColumnsNames(resultSet);

            ArrayList<String> VALUES = TableInsert.resultToInsert(resultSet, columns.size());


            return ""
                    + "INSERT INTO `" + table.name() + "` "
                    + "(" + TableInsert.getColumns(columns, "`") + ") VALUES\r\n"
                    + String.join(",\r\n", VALUES) + ";\r\n\r\n";
        } catch (Exception ignore) {
        }

        return "";
    }

    public static String getColumns(ArrayList<String> columns, String separator) {
        return separator + String.join("`, `", columns) + separator;
    }

    private static String serialize(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("'", "\\'");
    }

    public static ArrayList<String> resultToInsert(ResultSet resultSet, int countRows) throws SQLException {
        ArrayList<String> items = new ArrayList<>();
        while (resultSet.next()) {
            String[] columns = new String[countRows];
            for (int i = 0; i < countRows; i++) {
                Object row = resultSet.getObject(i + 1);
                String string = resultSet.getString(i + 1);

                if (row instanceof Number || row == null || row instanceof Boolean) {
                    columns[i] = row != null ? serialize(string) : "NULL";
                } else {
                    columns[i] = "'" + serialize(string) + "'";
                }
            }
            items.add("(" + String.join(", ", columns) + ")");
        }
        return items;
    }
}

