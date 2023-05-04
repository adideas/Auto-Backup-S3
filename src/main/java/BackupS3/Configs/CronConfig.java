package BackupS3.Configs;

import BackupS3.Configs.Functions.ConfigUseFunction;
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
    @SerializedName("CRON") private String CRON = "* * * * *";

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    public boolean isUse() {
        return USE;
    }

    public String getCron() {
        return this.CRON;
    }
}
