package BackupS3.Clients.FlareApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class FlareError {
    // class
    String class_name = "JVM";
    HashMap<Integer, String> code_snippet = new HashMap<>();
    Integer column_number = 1;
    String file = "System.error";
    Integer line_number = 1;
    String method = "cli";
    Integer trimmed_column_number = 500;

    public FlareError(Throwable error) {
        int index = 0;
        for (String text : getMessage(error).split("\n")) {
            code_snippet.put(++index, text);
        }
    }

    public FlareError(String error) {
        method = "<log>" + System.currentTimeMillis();
        file = "<log>" + System.currentTimeMillis();
        class_name = "<log>" + System.currentTimeMillis();
        int index = 0;
        for (String text : error.split("\n")) {
            code_snippet.put(++index, text);
        }
    }

    public FlareError(StackTraceElement stackTrace) {
        method = stackTrace.getMethodName();
        file = stackTrace.getFileName();
        class_name = stackTrace.getClassName();

        try {
            Class<?> clazz = Class.forName(class_name);
            FlareDocs flareDocs = null;
            if (method.equals("<init>")) {
                flareDocs = clazz.getDeclaredAnnotation(FlareDocs.class);
            } else {
                flareDocs = clazz.getDeclaredMethod(method).getAnnotation(FlareDocs.class);
            }

            int index = 0;
            for (String text : String.format(flareDocs.value(), method).split("\n")) {
                code_snippet.put(++index, text);
            }
        } catch (Exception ignore) {
            int index = 0;
            for (String text : String.format("METHOD %s(...) {\n    throw Throwable;\n}", method).split("\n")) {
                code_snippet.put(++index, text);
            }
        }
    }

    private static String getMessage(Throwable error) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(stream);
        error.printStackTrace(output);
        output.close();
        String msg = stream.toString();
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }
}
