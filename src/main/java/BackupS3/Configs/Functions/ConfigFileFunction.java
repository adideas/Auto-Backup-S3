package BackupS3.Configs.Functions;

import java.io.File;

/**
 * This interface declares a method TO_FILE.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public interface ConfigFileFunction {
    /**
     * Get file object (if not exist then make file)
     *
     * @param file Path to file
     * @return File Object
     * @since 1.0
     */
    default File TO_FILE(String file) {
        File object = new File(file);
        return object.exists() ? object : new File(System.getProperty("user.dir"), object.getName());
    }
}
