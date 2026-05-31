package lab;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyEdit {
    boolean readOnly() default false;

    boolean visible() default true;
}
