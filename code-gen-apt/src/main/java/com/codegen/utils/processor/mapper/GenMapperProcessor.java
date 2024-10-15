package com.codegen.utils.processor.mapper;

import com.codegen.common.DateMapper;
import com.codegen.common.GenericEnumMapper;
import com.codegen.utils.BaseCodeGenProcessor;
import com.codegen.utils.processor.DefaultNameContext;
import com.codegen.utils.spi.CodeGenProcessor;
import com.codegen.utils.utils.StringUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @author: yp
 * @date: 2024/10/14 10:17
 * @description:
 */
@AutoService(value = CodeGenProcessor.class)
public class GenMapperProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Mapper";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        AnnotationSpec mapperAnnotationSpec = AnnotationSpec.builder(Mapper.class)
                .addMember("uses", "$T.class", DateMapper.class)
                .addMember("uses", "$T.class", GenericEnumMapper.class)
                .build();
        String className = typeElement.getSimpleName().toString() + SUFFIX;
        String packageName = typeElement.getAnnotation(GenMapper.class).pkgName();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(mapperAnnotationSpec);
        FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(packageName, className), "INSTANCE")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$T.getMapper($T.class)", Mappers.class, ClassName.get(packageName, className))
                .build();
        typeSpecBuilder.addField(fieldSpec);

        DefaultNameContext nameContext = getNameContext(typeElement);
        Optional<MethodSpec> request2Dto = request2Query(nameContext);
        request2Dto.ifPresent(m -> typeSpecBuilder.addMethod(m));

        genJavaSourceFile(generatePackage(typeElement),
                typeElement.getAnnotation(GenMapper.class).sourcePath(), typeSpecBuilder);
    }


    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenMapper.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenMapper.class).pkgName();
    }

    private Optional<MethodSpec> request2Query(DefaultNameContext nameContext) {
        String queryRequestClassName = nameContext.getQueryRequestClassName();
        String creatorClassName = nameContext.getCreatorClassName();
        if (!StringUtils.containsNull(queryRequestClassName, creatorClassName)) {
            return Optional.of(MethodSpec.methodBuilder("request2Dto")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ClassName.get(nameContext.getCreatorPackageName(), creatorClassName))
                    .addParameter(ClassName.get(nameContext.getQueryRequestPackageName(), queryRequestClassName), "request")
                    .build());
        }
        return Optional.empty();
    }

}
