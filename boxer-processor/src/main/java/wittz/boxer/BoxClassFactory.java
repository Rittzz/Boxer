package wittz.boxer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 6/29/15.
 */
public class BoxClassFactory {

    public static List<BoxClass> create(final TypeElement element, final ProcessingEnvironment processingEnv) throws BoxerException {
        final List<BoxClass> boxClasses = new ArrayList<>();

        TypeElement currentClass = element;

        while (currentClass != null) {
            final BoxClass boxClass = new BoxClass(processingEnv);
            boxClass.setClassElement(currentClass);

            for (Element enclosed : currentClass.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.FIELD && !enclosed.getModifiers().contains(Modifier.STATIC)) {

                    if (BoxField.isValidModifier(enclosed)) {
                        boxClass.getFields().add(new BoxField(enclosed, processingEnv));
                    }
                    else {
                        final String fieldName = currentClass.getQualifiedName() + "." + enclosed.getSimpleName();
                        throw new BoxerException(
                                "Cannot box '" + fieldName + "'.  Boxer only supports non-final package(default) and public fields.");
                    }
                }
            }

            boxClasses.add(boxClass);

            currentClass = getSuperClass(currentClass, processingEnv);
        }

        return boxClasses;
    }

    public static List<BoxClass> createFromList(final TypeElement typeElement, final ProcessingEnvironment processingEnv) throws BoxerException {
        final List<BoxClass> boxClasses = new ArrayList<>();

        for (TypeElement te : Utils.getTypeElementsFromAnnotation(processingEnv, BoxClasses.class, typeElement)) {
            boxClasses.addAll(create(te, processingEnv));
        }

        return boxClasses;
    }

    private static String[] PACKAGE_BLACKLIST = new String[] {
            "java", "android"
    };

    public static TypeElement getSuperClass(final TypeElement classElement, ProcessingEnvironment processingEnv) {
        TypeMirror superClassMirror = classElement.getSuperclass();
        if (superClassMirror.getKind() == TypeKind.NONE) {
            throw new IllegalArgumentException("No super class");
        }

        TypeElement superClassType = (TypeElement) processingEnv.getTypeUtils().asElement(superClassMirror);
        final String fullClassName = superClassType.getQualifiedName().toString();

        for (String pkg : PACKAGE_BLACKLIST) {
            if (fullClassName.startsWith(pkg)) {
                return null;
            }
        }

        return superClassType;
    }
}
