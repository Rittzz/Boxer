package wittz.boxer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created on 7/1/15.
 */
public class Utils {

    public static List<TypeElement> getTypeElementsFromAnnotation(final ProcessingEnvironment processingEnv, final Class<? extends Annotation> annotationClass, final TypeElement typeElement) {
        List<TypeElement> typeElements = new ArrayList<>();

        // Pull the classes out from "BoxClasses" annotation
        final String actionName = annotationClass.getName();
        for (AnnotationMirror am : typeElement.getAnnotationMirrors()) {
            if (actionName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("value".equals(entry.getKey().getSimpleName().toString())) {
                        final List<?> classes = (List)entry.getValue().getValue();
                        for (Object obj : classes) {
                            // Remove the ".class" at the end - There has got to be a better way to do this!
                            final String className = obj.toString().substring(0, obj.toString().length() - 6);
                            final TypeElement classElement = processingEnv.getElementUtils().getTypeElement(className);
                            typeElements.add(classElement);
                        }
                        break;
                    }
                }
            }
        }

        return typeElements;
    }

    public static String join(String separator, List<String> parts) {
        if (parts.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        result.append(parts.get(0));
        for (int i = 1; i < parts.size(); i++) {
            result.append(separator).append(parts.get(i));
        }
        return result.toString();
    }
}
