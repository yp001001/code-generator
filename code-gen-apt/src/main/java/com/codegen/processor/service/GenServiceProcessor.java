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
        Optional<MethodSpec> createMethod = createMethod(typeElement, nameContext);
        createMethod.ifPresent(m -> builder.addMethod(m));
        Optional<MethodSpec> updateMethod = updateMethod(typeElement, nameContext);
        updateMethod.ifPresent(m -> builder.addMethod(m));
        Optional<MethodSpec> validMethod = validMethod(typeElement);
        validMethod.ifPresent(m -> builder.addMethod(m));
        Optional<MethodSpec> invaildMethod = invaildMethod(typeElement);
        invaildMethod.ifPresent(m -> builder.addMethod(m));
        Optional<MethodSpec> findByPageMethod = findByPageMethod(nameContext);
        findByPageMethod.ifPresent(m -> builder.addMethod(m));

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

    private Optional<MethodSpec> createMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        String createClassName = nameContext.getCreateClassName();
        if (!StringUtils.containsNull(createClassName)) {
            return Optional.of(MethodSpec.methodBuilder("create" + typeElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ClassName.get(nameContext.getCreatorPackageName(), createClassName), "creator")
                    .addJavadoc("create")
                    .returns(Long.class)
                    .build());

        }
        return Optional.empty();
    }

    private Optional<MethodSpec> updateMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        String updateClassName = nameContext.getUpdateClassName();
        if (!StringUtils.containsNull(updateClassName)) {
            return Optional.of(MethodSpec.methodBuilder("update" + typeElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ClassName.get(nameContext.getUpdatePackageName(), updateClassName), "update")
                    .addJavadoc("update")
                    .returns(void.class)
                    .build());
        }
        return Optional.empty();
    }


    private Optional<MethodSpec> validMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec.methodBuilder("vaild" + typeElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Long.class, "id")
                .addJavadoc("vaild")
                .returns(void.class).build());
    }

    private Optional<MethodSpec> invaildMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec.methodBuilder("invaild" + typeElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Long.class, "id")
                .addJavadoc("invaild")
                .returns(void.class).build());
    }

    private Optional<MethodSpec> findByPageMethod(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getQueryPackageName(),
                nameContext.getVoPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec.methodBuilder("findByPage")
                    .addParameter(ParameterizedTypeName.get(ClassName.get(PageRequestWrapper.class),
                                    ClassName.get(nameContext.getQueryPackageName(), nameContext.getQueryClassName())),
                            "query")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc("findByPage")
                    .returns(ParameterizedTypeName.get(ClassName.get(Page.class),
                            ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName())))
                    .build());
        }
        return Optional.empty();
    }

}
