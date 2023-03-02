package BackupS3.Configs.Functions;

/**
 * This interface declares a method to check if this configuration is used.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public interface ConfigUseFunction {
    /**
     * This interface declares a method to check if this configuration is used.<p></p>
     * The method returns information about the use of the configuration.
     *
     * @return true - IF you use this config
     * @since 1.0
     */
    boolean isUse();
}
