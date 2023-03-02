package BackupS3.Clients.Mysql;

import java.util.HashMap;

/**
 * Get table count
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class TableCount extends HashMap<String, String> {

    /**
     * Get count
     *
     * @return count
     * @since 1.0
     */
    public Integer getCount() {
        return Integer.decode(this.get("count"));
    }
}
