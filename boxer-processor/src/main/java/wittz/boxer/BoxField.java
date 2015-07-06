package wittz.boxer;

import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created on 6/30/15.
 */
public class BoxField {

    private Element element;
    private String dataType;
    private String fieldName;

    public BoxField(final Element element, final ProcessingEnvironment processingEnv) throws BoxerException {
        if (!isValidModifier(element)) {
            throw new BoxerException("The field '" + element.getSimpleName().toString()
                    + "' must be public or default access and not be final");
        }

        this.element = element;

        dataType = element.asType().toString();
        fieldName = element.getSimpleName().toString();
    }

    public static boolean isValidModifier(final Element element) {
        for (Modifier modifier : element.getModifiers()) {
            if (modifier == Modifier.PRIVATE
                    || modifier == Modifier.FINAL
                    || modifier == Modifier.PROTECTED) {
                return false;
            }
        }

        return true;
    }

    public Element getElement() {
        return element;
    }

    public String getDataType() {
        return dataType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String toString() {
        return String.format("%s %s", dataType, fieldName);
    }

    public TypeName getDataTypeName() {
        return TypeName.get(element.asType());
    }

    public boolean isArray() {
        return dataType.endsWith("[]");
    }
}
