package wittz.boxer;

import android.os.Parcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import wittz.boxer.converters.GeneratedBoxConverter;


/**
 * Created on 6/29/15.
 */
public class BoxClass {

    private final ProcessingEnvironment processingEnv;

    private TypeElement classElement;
    private List<BoxField> poodleFields = new ArrayList<>();

    public BoxClass(final ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void setClassElement(TypeElement classElement) {
        this.classElement = classElement;
    }

    public TypeElement getClassElement() {
        return classElement;
    }

    public List<BoxField> getFields() {
        return poodleFields;
    }

    private boolean hasSuperClass() {
        return getSuperClass() != null;
    }

    private TypeElement getSuperClass() {
        return BoxClassFactory.getSuperClass(classElement, processingEnv);
    }

    private void addSuperConverter(MethodSpec.Builder methodBuilder) {
        final TypeName typeName = TypeName.get(getSuperClass().asType());

        final TypeName converterClass = typeName instanceof ParameterizedTypeName ? ((ParameterizedTypeName) typeName).rawType : typeName;
        final ParameterizedTypeName ptn = ParameterizedTypeName.get(ClassName.get(GeneratedBoxConverter.class), converterClass);

        methodBuilder.addStatement("$T superConverter = ($T)$T.getConverterForClass($T.class)",
                ptn, ptn, Boxer.class, converterClass);
    }

    private MethodSpec createBaseClass() {
        final ClassName className = ClassName.get(classElement);

        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getBaseClass")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return $T.class", className)
                .returns(Class.class);

        return methodBuilder.build();
    }

    private MethodSpec createNewModel() {
        final ClassName className = ClassName.get(classElement);

        if (!classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("newModel")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement("return new $T()", className)
                    .returns(className);

            return methodBuilder.build();
        }
        else {
            return MethodSpec.methodBuilder("newModel")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement("return null")
                    .returns(className)
                    .build();
        }
    }

    private MethodSpec createFromParcel() {
        final ClassName className = ClassName.get(classElement);

        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("populateFromParcel")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(className, "data")
                .addParameter(Parcel.class, "parcel")
                .returns(void.class);

        if (hasSuperClass()) {
            addSuperConverter(methodBuilder);
            methodBuilder.addStatement("superConverter.populateFromParcel($N, $N)", "data", "parcel");
        }

        for (BoxField field : poodleFields) {
            ParcelHelper.createFromParcel(methodBuilder, field);
        }

        return methodBuilder.build();
    }

    private MethodSpec createWriteToParcel() {
        final ClassName className = ClassName.get(classElement);

        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("writeToParcel")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(className, "object")
                .addParameter(Parcel.class, "parcel")
                .returns(void.class);

        if (hasSuperClass()) {
            addSuperConverter(methodBuilder);
            methodBuilder.addStatement("superConverter.writeToParcel($N, $N)", "object", "parcel");
        }

        for (BoxField field : poodleFields) {
            ParcelHelper.writeToParcel(methodBuilder, field);
        }

        return methodBuilder.build();
    }

    public void generate(final Filer filer) throws IOException {
        final ClassName interfaceClassName = ClassName.get(GeneratedBoxConverter.class);
        final ClassName className = ClassName.get(classElement);

        final TypeName superClass = ParameterizedTypeName.get(interfaceClassName, className);

        final String simpleClassName = Utils.join("$", className.simpleNames());
        final ClassName wrapperType = ClassName.get(className.packageName(), simpleClassName + Boxer.POSTFIX);

        final FieldSpec fieldSpec = FieldSpec.builder(wrapperType, "INSTANCE", Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                .initializer("new $T()", wrapperType)
                .build();

        final TypeSpec typeSpecBuilder = TypeSpec.classBuilder(wrapperType.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(superClass)
                .addMethod(createBaseClass())
                .addMethod(createNewModel())
                .addMethod(createFromParcel())
                .addMethod(createWriteToParcel())
                .addField(fieldSpec)
                .build();

        final JavaFile javaFile = JavaFile.builder(className.packageName(), typeSpecBuilder)
                .build();

        javaFile.writeTo(filer);
    }

}
