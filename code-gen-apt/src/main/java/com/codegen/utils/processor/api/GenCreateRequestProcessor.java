package com.codegen.utils.processor.api;

import com.codegen.common.model.Request;
import com.codegen.utils.BaseCodeGenProcessor;
import com.codegen.utils.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.xml.validation.Schema;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author: yp
 * @date: 2024/10/15 9:42
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenCreateRequestProcessor extends BaseCodeGenProcessor {

    public static final String CREATE_REQUEST_SUFFIX = "CreateRequest";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = typeElement.getSimpleName().toString() + CREATE_REQUEST_SUFFIX;
        String packageName = typeElement.getAnnotation(GenCreatorRequest.class).pkgName();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addAnnotation(Schema.class)
                .addSuperinterface(Request.class)
                .addModifiers(Modifier.PUBLIC);
        Set<VariableElement> variableElements = findField(typeElement, (v) -> null == v.getAnnotation(IgnoreCreatorRequest.class));
        addSetterAndGetterMethodWithConverter(typeSpecBuilder, variableElements);
        genJavaSourceFile(generatePackage(typeElement), packageName, typeSpecBuilder);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenCreatorRequest.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenCreatorRequest.class).pkgName();
    }
}
