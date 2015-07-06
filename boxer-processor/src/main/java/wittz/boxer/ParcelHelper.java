package wittz.boxer;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Created on 6/30/15.
 */
class ParcelHelper {

    private static TypeMirror getClassForAnnotation(final BoxCustomConverter annotation) {
        // This is a stupid stupid hack
        try {
            annotation.value();
            throw new RuntimeException("Not possible");
        }
        catch (MirroredTypeException ex) {
            return ex.getTypeMirror();
        }
    }

    public static void pullConverterFromRegistry(final MethodSpec.Builder methodBuilder, final TypeName typeName, final String converterPostFix) {
        final TypeName converterClass = typeName instanceof ParameterizedTypeName ? ((ParameterizedTypeName) typeName).rawType : typeName;
        final ParameterizedTypeName ptn = ParameterizedTypeName.get(ClassName.get(BoxConverter.class), converterClass);
        methodBuilder.addStatement("$T converter"+converterPostFix+" = $T.getConverterForClass($T.class)",
                ptn, Boxer.class, converterClass);
    }

    public static void pullConverterFromRegistry(final MethodSpec.Builder methodBuilder, final TypeName typeName) {
        pullConverterFromRegistry(methodBuilder, typeName, "");
    }

    public static void writeToParcel(final MethodSpec.Builder methodBuilder, final BoxField field) {
        final TypeName typeName = field.getDataTypeName();

        if (typeName.isPrimitive()) {
            final String converterPostFix = "_" + field.getFieldName();
            pullConverterFromRegistry(methodBuilder, typeName.box(), converterPostFix);
            methodBuilder.addStatement("converter"+converterPostFix+".writeToParcel(object.$N, parcel)", field.getFieldName());
            return;
        }

        // Fallback to class time generation
        methodBuilder.beginControlFlow("if (object.$N != null)", field.getFieldName());
        methodBuilder.addStatement("parcel.writeByte((byte)1)");

        if (field.getElement().getAnnotation(BoxCustomConverter.class) != null) {
            final TypeName customConverterClass =
                    TypeName.get(getClassForAnnotation(field.getElement().getAnnotation(BoxCustomConverter.class)));

            methodBuilder.addStatement("new $T().writeToParcel(object.$N, parcel)", customConverterClass, field.getFieldName());
        }
        else {
            pullConverterFromRegistry(methodBuilder, field.getDataTypeName());
            methodBuilder.addStatement("converter.writeToParcel(object.$N, parcel)", field.getFieldName());

            //methodBuilder.addStatement("parcel.writeParcelable($T.wrap(object.$N), 0);", Boxer.class, field.getFieldName());
        }

        methodBuilder.nextControlFlow("else");
        methodBuilder.addStatement("parcel.writeByte((byte)0)");
        methodBuilder.endControlFlow();
    }

    public static void createFromParcel(final MethodSpec.Builder methodBuilder, final BoxField field) {
        final TypeName typeName = field.getDataTypeName();

        if (typeName.isPrimitive()) {
            final String converterPostFix = "_" + field.getFieldName();
            pullConverterFromRegistry(methodBuilder, typeName.box(), converterPostFix);
            methodBuilder.addStatement("data.$N = converter"+converterPostFix+".fromParcel(parcel)", field.getFieldName());
            return;
        }

        // Fallback to class time generation
        methodBuilder.beginControlFlow("if (parcel.readByte() == 1)");

        if (field.getElement().getAnnotation(BoxCustomConverter.class) != null) {
            final TypeName customConverterClass =
                    TypeName.get(getClassForAnnotation(field.getElement().getAnnotation(BoxCustomConverter.class)));
            methodBuilder.addStatement("data.$N = new $T().fromParcel(parcel)", field.getFieldName(), customConverterClass);
        }
        else {
            pullConverterFromRegistry(methodBuilder, field.getDataTypeName());
            methodBuilder.addStatement("data.$N = converter.fromParcel(parcel)", field.getFieldName());
//
//            methodBuilder.addStatement("$T parcelWrapper = parcel.readParcelable($T.class.getClassLoader())",
//                    BoxWrapperParcelable.class, Boxer.class);
//            methodBuilder.addStatement("data.$N = $T.unwrap(parcelWrapper)", field.getFieldName(), Boxer.class);
        }

        methodBuilder.endControlFlow();
    }
}
