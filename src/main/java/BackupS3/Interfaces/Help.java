package BackupS3.Interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for help
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 * @since 1.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Help {
    /**
     * Name command
     * @return Name command
     * @since 1.1
     */
    String command();
    /**
     * About command
     * @return About command
     * @since 1.1
     */
    String description() default "";
}
