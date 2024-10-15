package com.codegen.processor.query;

import com.codegen.BaseCodeGenProcessor;
import com.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * @author: yp
 * @date: 2024/10/15 15:19
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenQueryCodeProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Query";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = PREFIX + typeElement.getSimpleName().toString() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        String packageName = generatePackage(typeElement);
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
        Set<VariableElement> variableElements = findField(typeElement, v -> Objects.nonNull(v.getAnnotation(QueryItem.class)));
        addSetterAndGetterMethod(builder, variableElements);
        genJavaFile(typeElement.getAnnotation(GenQuery.class).pkgName(), builder);
        // 创建子类
        genJavaFile(packageName, getSourceType(sourceClassName, packageName, className));
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenQuery.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenQuery.class).pkgName();
    }
}
