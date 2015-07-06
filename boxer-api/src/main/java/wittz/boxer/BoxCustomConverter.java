package wittz.boxer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 6/28/15.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface BoxCustomConverter {
    Class<? extends wittz.boxer.BoxConverter> value();
}
