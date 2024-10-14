package com.codegen.processor.service;

import com.codegen.BaseCodeGenProcessor;
import com.codegen.common.model.PageRequestWrapper;
import com.codegen.processor.DefaultNameContext;
import com.codegen.spi.CodeGenProcessor;
import com.codegen.utils.StringUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.data.domain.Page;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author: yp
 * @date: 2024/10/14 14:47
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenServiceProcessor extends BaseCodeGenProcessor {

    public static final String SERVICE_SUFFIX = "Service";

    public static final String SERVICE_PREFIX = "I";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = SERVICE_PREFIX + typeElement.getSimpleName().toString() + SERVICE_SUFFIX;
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC);
        DefaultNameContext nameContext = getNameContext(typeElement);
        Optional<MethodSpec> findByIdMethod = findByIdMethod(typeElement, nameContext);
        findByIdMethod.ifPresent(m -> builder.addMethod(m));
        // TODO
        genJavaSourceFile(generatePackage(typeElement),
                typeElement.getAnnotation(GenService.class).sourcePath(), builder);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenService.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenService.class).pkgName();
    }

    private Optional<MethodSpec> findByIdMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        String voClassName = nameContext.getVoClassName();
        if (!StringUtils.containsNull(voClassName)) {
            return Optional.of(MethodSpec.methodBuilder("findById")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(Long.class, "id")
                    .addJavadoc("findById")
                    .returns(ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()))
                    .build());
        }
        return Optional.empty();
    }

}
