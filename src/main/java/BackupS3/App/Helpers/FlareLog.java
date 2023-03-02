package BackupS3.App.Helpers;

import BackupS3.Clients.FlareApp.FlareApp;
import BackupS3.Configs.Env;
import BackupS3.Configs.FlareConfig;

/**
 * This class is helper for {@link FlareApp}
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class FlareLog {
    String key = null;

    /**
     * Set Flare key {@link FlareConfig#getKey()}
     *
     * @param key This method return Flare secret key.
     */
    private FlareLog(String key) {
        this.key = key;
    }

    /**
     * Send error in Flare app {@link FlareApp#post(String, String)}
     *
     * @apiNote
     *  <b>params:</b>
     *  <li> {@link Exception} </li>
     *  <li> {@link String} </li>
     *
     *
     * @param objects params
     * @since 1.0
     */
    public void msg(Object... objects) {
        if (key != null) {
            FlareApp.post(key, FlareApp.getBody(key, objects));
        }
    }

    /**
     * Send error in Flare app {@link FlareApp#post(String, String)}
     *
     * @apiNote
     *  <b>params:</b>
     *  <li> {@link Exception} </li>
     *  <li> {@link String} </li>
     *
     *
     * @param objects params
     * @since 1.0
     */
    public static void message(Object... objects) {
        String key = Env.getFlareKey();
        if (key != null) {
            new FlareLog(key).msg(objects);
        }
    }
}
