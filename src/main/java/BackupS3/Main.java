package BackupS3;

import BackupS3.App.Cron;
import BackupS3.Configs.Env;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Main {
    private static final HashMap<String, Consumer<String[]>> actions = new HashMap<>(){{
        put("service", Main::service);
        put("make-env", Main::makeEnv);
        put("help", Main::help);
        put("make-systemd", Main::makeSystemd);
    }};

    public static void main(String[] args) {
        if (args.length > 0 && actions.containsKey(args[0])) {
            actions.get(args[0]).accept(Arrays.copyOfRange(args, 1, args.length));
        } else {
            help(new String[0]);
        }
    }

    private static void service(String[] args) {
        makeEnv(new String[0]);
        Cron.start();

        if (args.length > 0) { main(args); }
    }

    private static void makeEnv(String[] args) {
        if (args.length > 0) {
            System.out.println("make-env without args");
        }
        Env.createIfNotExists();
    }

    private static void makeSystemd(String[] args) {
        Path folder = Paths.get(System.getProperty("user.dir"));
        String jar = "___";

        try {
            jar = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (Exception ignore) {}

        if (!jar.endsWith("jar")) jar = folder.resolve("???.jar").toString();

        String[] lines = new String[]{
                "",
                "",
                "[Unit]",
                "Description=Backup S3 AWS",
                "After=network.target",
                "",
                "[Service]",
                "SuccessExitStatus=143",
                "Type=simple",
                "Restart=on-failure",
                "RestartSec=10",
                "",
                "User=" + System.getProperty("user.name"),
                "WorkingDirectory=" + folder,
                "ExecStart=/bin/java -Xms128m -Xmx256m -jar " + jar + " service",
                "StandardOutput=file:" + folder.resolve("system_service_output.log"),
                "StandardError=file:" + folder.resolve("system_service_error.log"),
                "",
                "[Install]",
                "WantedBy=multi-user.target",
                "",
                ""
        };

        System.out.println(String.join("\n", lines));
    }

    private static void help(String[] args) {
        if (args.length > 0) {
            System.out.println("help without args");
        }

        System.out.println("Config file: " + Env.path() + "\n");

        System.out.println("""
| Command                        | About                         |
|--------------------------------|-------------------------------|
| help                           | Get help                      |
| make-env                       | Create default env            |
| service                        | Service start                 |
| make-systemd                   | Print example systemd service |
| make-systemd > example.service | Make example systemd service  |
                """);
    }
}
