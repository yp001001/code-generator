package com.codegen.repository;

import com.codegen.BaseCodeGenProcessor;
import com.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.jpa.support.BaseRepository;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author: yp
 * @date: 2024/10/15 16:28
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenRepositoryProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Repository";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = typeElement.getSimpleName().toString() + SUFFIX;
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(BaseRepository.class), ClassName.get(typeElement), ClassName.get(Long.class)))
                .addModifiers(Modifier.PUBLIC);
        genJavaSourceFile(generatePackage(typeElement), typeElement.getAnnotation(GenRepository.class).sourcePath(), typeSpecBuilder);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenRepository.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenRepository.class).pkgName();
    }

}
