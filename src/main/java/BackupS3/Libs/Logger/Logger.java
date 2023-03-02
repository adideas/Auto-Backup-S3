package BackupS3.Libs.Logger;

import BackupS3.Libs.Logger.Column.HOSTNAME;
import BackupS3.Libs.Logger.Column.LEVEL;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * This class make message (<a href="https://www.rfc-editor.org/rfc/rfc5424">RFC 5424</a>)
 *
 * @apiNote Please read readme.md (src/main/java/BackuperS3/Libs/Logger/readme.md)
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class Logger {
    /**
     * This method generate error message
     *
     * @param hostname Hostname
     * @param level Level error (Type)
     * @param msg Message
     * @return Message error
     * @since 1.0
     */
    public static String getLine(HOSTNAME hostname, LEVEL level, String msg) {
        StringBuilder line = new StringBuilder();

        String date = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (Object column : new Object[]{date, hostname, level, " " + msg + " ", "\n"}) {
            line.append(";").append(column);
        }

        return line.toString();
    }
}
