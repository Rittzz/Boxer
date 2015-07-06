package wittz.boxer;

import com.squareup.javapoet.ClassName;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created on 6/29/15.
 */
public class BoxerProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnv;

    private List<String> generatedClasses = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        generatedClasses.clear();
    }

    private void log(final String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.OTHER, msg);
    }

    private static String getStacktrace(final Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        return sw.toString();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final List<BoxClass> boxClasses = new ArrayList<>();

        try {
            for (Element element : roundEnv.getElementsAnnotatedWith(Box.class)) {
                if (element.getKind() != ElementKind.CLASS) {
                    throw new IllegalArgumentException("The non-class " + element.getSimpleName() + "  is annotated with @Box");
                }

                // We can cast it, because we know that it is of ElementKind.CLASS
                final TypeElement typeElement = (TypeElement) element;
                boxClasses.addAll(BoxClassFactory.create(typeElement, processingEnv));
            }

            for (Element element : roundEnv.getElementsAnnotatedWith(BoxClasses.class)) {
                if (element.getKind() != ElementKind.CLASS) {
                    throw new IllegalArgumentException("The non-class " + element.getSimpleName() + "  is annotated with @BoxClasses");
                }

                // We can cast it, because we know that it is of ElementKind.CLASS
                final TypeElement typeElement = (TypeElement) element;
                boxClasses.addAll(BoxClassFactory.createFromList(typeElement, processingEnv));
            }

            // Create the source files
            for (BoxClass boxClass : boxClasses) {
                final String className = ClassName.get(boxClass.getClassElement()).toString();

                if (!generatedClasses.contains(className)) {
                    generatedClasses.add(className);
                    boxClass.generate(processingEnv.getFiler());
                }
            }
        }
        catch (BoxerException |IOException ex) {
            log(getStacktrace(ex));
            throw new RuntimeException(ex);
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> annotations = new HashSet<>();
        annotations.add(Box.class.getCanonicalName());
        annotations.add(BoxClasses.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
