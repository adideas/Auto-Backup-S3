package BackupS3.Configs;

import BackupS3.Configs.Functions.ConfigUseFunction;
import BackupS3.Libs.Cron.Schedule;
import com.google.gson.annotations.SerializedName;

/**
 * Task scheduler configuration
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class CronConfig implements ConfigUseFunction {
    @SerializedName("USE") private Boolean USE = false;
    @SerializedName("TYPE-{DAILY|EVERY}") private Schedule TYPE = Schedule.DAILY;
    @SerializedName("DAILY_HOUR") private Long DAILY_HOUR = -1L;
    @SerializedName("DAILY_MINUTE") private Long DAILY_MINUTE = -1L;

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    public boolean isUse() {
        if (TYPE != Schedule.DAILY) return USE;
        if (this.getDailyHour() >= 0 && this.getDailyMinute() >= 0) return USE;
        return false;
    }

    /**
     * Get an enumeration of the execution type of a command.
     *
     * @return Execution type. DAILY or EVERY
     * @since 1.0
     */
    public Schedule getType() {
        return this.USE == true ? this.TYPE : null;
    }

    /**
     * Get execution hour
     *
     * @apiNote
     *  hour MOD 24
     * @return Get execution hour
     * @since 1.0
     */
    public Long getDailyHour() {
        return this.DAILY_HOUR % 24;
    }

    /**
     * Get execution minute
     *
     * @apiNote
     *  minute MOD 60
     * @return Get execution minute
     * @since 1.0
     */
    public Long getDailyMinute() {
        return this.DAILY_MINUTE % 60;
    }
}
