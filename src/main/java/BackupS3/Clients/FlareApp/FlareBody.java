package BackupS3.Clients.FlareApp;


import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class FlareBody {
    String exception_class = "RangeError";
    ArrayList<String> solutions = new ArrayList<>();
    ArrayList<String> glows = new ArrayList<>();
    String key = "";
    String language = "javascript";
    String message = "MESSAGE";
    String notifier = "Flare JavaScript client v1.0.12";
    String seen_at = "1676544268";
    String sourcemap_version_id = "";
    ArrayList<FlareError> stacktrace = new ArrayList<>();
    FlareContext context = new FlareContext();

    public FlareBody(String key, Throwable error) {
        this.exception_class = error.getClass().getName();
        this.key = key;
        this.message = "JAVA " + ExceptionUtils.getMessage(error) + " > " + ExceptionUtils.getRootCauseMessage(error);
        this.seen_at = String.valueOf((System.currentTimeMillis() / 1000) - 3600);
        stacktrace.add(new FlareError(error));

        for(StackTraceElement stackTrace : error.getStackTrace()) {
            stacktrace.add(new FlareError(stackTrace));
        }
    }

    public FlareBody(String key, Throwable error, String debug) {
        this.exception_class = error.getClass().getName();
        this.key = key;
        this.message = "JAVA " + ExceptionUtils.getMessage(error) + " > " + ExceptionUtils.getRootCauseMessage(error);
        this.seen_at = String.valueOf((System.currentTimeMillis() / 1000) - 3600);
        stacktrace.add(new FlareError(debug));
        stacktrace.add(new FlareError(error));

        for(StackTraceElement stackTrace : error.getStackTrace()) {
            stacktrace.add(new FlareError(stackTrace));
        }
    }

    public FlareBody(String key, String error) {
        this.exception_class = "Logger";
        this.key = key;
        this.seen_at = String.valueOf((System.currentTimeMillis() / 1000) - 3600);
        this.message = "JAVA Logger " + this.seen_at;
        stacktrace.add(new FlareError(error));
    }
}

