package BackupS3.Clients.Mysql;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Collection database data
 * @param <T>
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class DataSet<T extends HashMap<String, String>> extends ArrayList<T> {

    /**
     * Make collection from Type
     *
     * @param type Type item
     * @param resultSet Data
     * @return collection from Type
     */
    public static <T extends HashMap<String, String>> DataSet<T> from(Class<T> type, ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        ArrayList<String> columns = DataSet.getColumnsNames(resultSet);

        DataSet<T> dataSet = new DataSet<>();

        while (resultSet.next()) {
            dataSet.add(type, resultSet, columns);
        }

        return dataSet;
    }

    /**
     * Get columns names
     *
     * @param resultSet Data
     * @return column names
     * @throws SQLException If the connection is not established
     * @since 1.0
     */
    public static ArrayList<String> getColumnsNames(ResultSet resultSet) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.add(metaData.getColumnName(i));
        }
        return columns;
    }

    /**
     * Add item to collect
     *
     * @param type Type item
     * @param resultSet Data
     * @param columns column names
     * @since 1.0
     */
    private void add(Class<T> type, ResultSet resultSet, ArrayList<String> columns) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T item = type.getConstructor().newInstance();

        for (String column : columns) {
            item.put(column, resultSet.getString(column));
        }

        this.add(item);
    }
}
