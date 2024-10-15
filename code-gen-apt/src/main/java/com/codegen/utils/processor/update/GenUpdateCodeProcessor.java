package com.codegen.utils.processor.update;

import com.codegen.utils.BaseCodeGenProcessor;
import com.codegen.utils.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author: yp
 * @date: 2024/10/15 13:42
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenUpdateCodeProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Update";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = PREFIX + typeElement.getSimpleName().toString() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName().toString() + SUFFIX;
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Data.class)
                .addAnnotation(Schema.class)
                .addModifiers(Modifier.PUBLIC);
        Set<VariableElement> variableElements =
                findField(typeElement, v -> Objects.isNull(typeElement.getAnnotation(UpdateIgnore.class)));
        addSetterAndGetterMethod(builder, variableElements);

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        for (VariableElement ve : variableElements) {
            codeBlockBuilder.addStatement("$T.ofNullable($L()).ifPresent(v -> param.$L(v))", Optional.class,
                    "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, ve.getSimpleName().toString()),
                    "set"+CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, ve.getSimpleName().toString()));
        }
        // 添加实体类属性赋值方法
        MethodSpec.Builder updateMethodBuilder = MethodSpec.methodBuilder("update" + typeElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "param")
                .addCode(codeBlockBuilder.build())
                .returns(void.class);

        builder.addMethod(updateMethodBuilder.build());

        // 添加id
        builder.addField(FieldSpec.builder(Long.class, "id", Modifier.PRIVATE).build());
        addIdSetterAndGetter(builder);
        // 添加构造方法
        builder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED).build());

        String packageName = generatePackage(typeElement);
        genJavaFile(packageName, builder);
        genJavaFile(packageName, getSourceType(sourceClassName, packageName, className));
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenUpdate.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenUpdate.class).pkgName();
    }
}
