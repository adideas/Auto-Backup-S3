package BackupS3;

import BackupS3.App.Cron;
import BackupS3.App.Loggers.CronLogger;
import BackupS3.Configs.Env;
import BackupS3.Interfaces.Help;
import BackupS3.Configs.Cli.TypeTest;
import BackupS3.Libs.ResourceLoader.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Main {
    private static final HashMap<String, Consumer<String[]>> actions = new HashMap<>(){{
        put("help", Main::help);
        put("service", Main::service);
        put("install", Main::install);
        // OPTIONS
        put("only-db", Main::onlyDB);
        put("only-fs", Main::onlyFS);
    }};

    public static TypeTest type = TypeTest.ALL;

    public static void main(String[] args) {
        Env.createIfNotExists();

        if (args.length > 0) {
            call(args);
        } else {
            help(new String[0]);
        }
    }

    private static void call(String[] args) {
        if (args.length > 0) {
            if (actions.containsKey(args[0])) {
                actions.get(args[0]).accept(Arrays.copyOfRange(args, 1, args.length));
            } else {
                System.err.println("Command not allowed! For help -> help");
            }
        }
    }

    /**
     * Service start
     */
    @Help(command = "service", description = "Service start")
    private static void service(String[] args) {
        Cron.start();
        if (args.length > 0) { call(args); }
    }

    /**
     * Print example systemd service
     */
    @Help(command = "install", description = "Install service")
    private static void install(String[] args) {
        if (args.length > 0) {
            System.err.println("Options include in center [app {options} command]");
        }

        Path folder = Paths.get(System.getProperty("user.dir"));
        String jar = "___";

        try {
            jar = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (Exception ignore) {}

        if (!jar.endsWith("jar")) jar = folder.resolve("???.jar").toString();

        String finalJar = jar;
        System.out.println(
                Resource.read(
                        Main.class.getResource("/service.form"),
                        new HashMap<>() {{
                            put("{{USER}}", System.getProperty("user.name"));
                            put("{{WORKING_DIRECTORY}}", folder.toString());
                            put("{{JAR_NAME}}", finalJar);
                            put("{{ST_OUTPUT}}", folder.resolve("system_service_output.log").toString());
                            put("{{ST_ERROR}}", folder.resolve("system_service_error.log").toString());
                        }}
                )
        );
    }

    /**
     * Get help
     */
    @Help(command = "help", description = "Get help")
    private static void help(String[] args) {
        if (args.length > 0) {
            System.err.println("Options include in center [app {options} command]");
        }

        System.out.println("Config file: " + Env.path() + "\n");

        System.out.println(Resource.read(Main.class.getResource("/help.form")));
    }

    /**
     * Configuration for test [Only Data Bases]
     */
    @Help(command = "only-db", description = "Configuration for test [Only Data Bases]")
    private static void onlyDB(String[] args)
    {
        CronLogger.warn("ONLY_DB");
        type = TypeTest.ONLY_DB;
        if (args.length > 0) { call(args); }
    }

    /**
     * Configuration for test [Only File System]
     */
    @Help(command = "only-db", description = "Configuration for test [Only File System]")
    private static void onlyFS(String[] args)
    {
        CronLogger.warn("ONLY_FS");
        type = TypeTest.ONLY_FS;
        if (args.length > 0) { call(args); }
    }
}
