package BackupS3.Libs.Logger;

/**
 * Provider error message
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings({"unused"})
public class Column {
    /**
     * The logger is hostname
     * @version 1.0
     */
    public enum HOSTNAME {
        MAIN ("MAIN "), CRON ("CRON "), MYSQL("MYSQL"), FILES("FILES");
        private final String name;
        HOSTNAME(String name) { this.name = name; }

        @Override
        public String toString() { return this.name; }
    }

    /**
     * The logger provides the seven logging levels
     * defined in <a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>:
     * <li>debug</li>
     * <li>info</li>
     * <li>notice</li>
     * <li>warning</li>
     * <li>error</li>
     * <li>critical</li>
     * <li>alert</li>
     * @version 1.0
     */
    public enum LEVEL {
        ERROR(" ERROR"), DEBUG(" DEBUG"), WARN(" WARN "), NOTICE("NOTICE"), ALERT(" ALERT"), CRITICAL("CRITICAL"), INFO(" INFO ");
        private final String type;
        LEVEL(String type) { this.type = type; }

        @Override
        public String toString() { return this.type; }
    }
}
