package BackupS3.Configs;

import BackupS3.Configs.Functions.ConfigUseFunction;
import com.google.gson.annotations.SerializedName;

/**
 * Flare is exception tracker that helps you spend more time fixing bugs and less time uncovering them.
 * Over at Spatie we built Flare with the same amount of care and attention we put into the hundreds
 * of open source packages we maintain.
 * It's the exception tracker that we always wanted.
 *
 * @apiNote
 *  <li><i>Open project</i></li>
 *  <li><i>Settings</i></li>
 *  <li><i>Configuration</i></li>
 *  <li><i>Search you secret key</i></li>
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class FlareConfig implements ConfigUseFunction {
    @SerializedName("USE") private Boolean USE = false;
    @SerializedName("KEY") private String KEY = "***";

    /**
     * {@inheritDoc}
     * @apiNote
     *  This interface declares a method to check if this configuration is used.
     * @return true - IF you use this config
     * @since 1.0
     */
    @Override
    public boolean isUse() {
        return this.USE;
    }

    /**
     * This method return Flare secret key.
     * @return Your public key for <a href="https://flareapp.io/">Flare.app</a>
     */
    public String getKey() {
        return this.isUse() ? this.KEY : null;
    }


}
