package BackupS3.Configs;

import com.google.gson.annotations.SerializedName;

/**
 * Scheduler Config. A daemon used to execute tasks periodically at a specified time.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class SchedulerConfig {
    @SerializedName("MAX_THREAD") private Integer MAX_THREAD = 10;

    /**
     * This method gets the maximum number of threads
     * @return Maximum number of threads
     * @since 1.0
     */
    public Integer getMaxThread() {
        return this.MAX_THREAD;
    }
}
