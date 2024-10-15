package com.codegen.utils.processor.creator;

import com.codegen.utils.BaseCodeGenProcessor;
import com.codegen.utils.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author: yp
 * @date: 2024/10/15 9:37
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenCreatorProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Creator";

    static List<TypeName> dtoIgnoreFieldTypes;

    static {
        dtoIgnoreFieldTypes = new ArrayList();
        dtoIgnoreFieldTypes.add(TypeName.get(Date.class));
        dtoIgnoreFieldTypes.add(TypeName.get(LocalDateTime.class));
    }

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = PREFIX + typeElement.getSimpleName().toString() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        String packageName = generatePackage(typeElement);
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
        Set<VariableElement> variableElements = findField(typeElement, v -> Objects.isNull(v.getAnnotation(IgnoreCreator.class)) && ignoreType(v));
        addSetterAndGetterMethod(builder, variableElements);
        genJavaFile(typeElement.getAnnotation(GenCreator.class).pkgName(), builder);
        // 创建子类
        genJavaFile(packageName, getSourceType(sourceClassName, packageName, className));
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenCreator.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenCreator.class).pkgName();
    }

    private boolean ignoreType(VariableElement variableElement){
        return dtoIgnoreFieldTypes.contains(TypeName.get(variableElement.asType())) ||
                variableElement.getModifiers().contains(Modifier.STATIC);
    }

}
