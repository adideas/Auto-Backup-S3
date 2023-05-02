package BackupS3.Configs.Cli;

/**
 * Enum for configuration
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 * @since 1.1
 */
public enum TypeTest {
    // Run only DB
    ONLY_DB(0x1),
    // Run only FS
    ONLY_FS(0x2),
    // Default
    ALL(0x1 | 0x2);

    private final int value;

    TypeTest(int value) {
        this.value = value;
    }

    public boolean isOnlyDB() {
        return (this.value & TypeTest.ONLY_FS.value) >= 1;
    }

    public boolean isOnlyFS() {
        return (this.value & TypeTest.ONLY_FS.value) >= 1;
    }
}
