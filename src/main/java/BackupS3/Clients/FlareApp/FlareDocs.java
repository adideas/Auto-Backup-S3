package BackupS3.Clients.FlareApp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FlareDocs {
    String value() default "METHOD %s(...) {\n    throw Throwable;\n}";
}
