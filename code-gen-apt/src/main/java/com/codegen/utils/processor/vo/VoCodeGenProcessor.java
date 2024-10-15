package com.codegen.utils.processor.vo;

import com.codegen.utils.BaseCodeGenProcessor;
import com.codegen.utils.sort.Order;
import com.codegen.utils.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * @author: yp
 * @date: 2024/10/12 15:34
 * @description:vo代码生成器
 */
@Order(sort = Integer.MAX_VALUE)
@AutoService(value = CodeGenProcessor.class)
public class VoCodeGenProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "VO";


    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        Set<VariableElement> variableElements =
                findField(typeElement, (ele -> Objects.isNull(ele.getAnnotation(IgnoreVO.class))));

        String className = PREFIX + typeElement.getSimpleName() + SUFFIX;

        TypeSpec.Builder sourceBuilder = TypeSpec.classBuilder(className)
                .superclass(AbstractBaseJpaVO.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(Schema.class);
        addSetterAndGetterMethod(sourceBuilder, variableElements);

        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(typeElement.asType()), "source")
                .addModifiers(Modifier.PUBLIC);
        constructorSpecBuilder.addStatement("super(source)");
        variableElements.stream().forEach(f -> {
            constructorSpecBuilder.addStatement("this.set$L(source.get$L())", getFieldDefaultName(f),
                    getFieldDefaultName(f));
        });
        sourceBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .build());
        sourceBuilder.addMethod(constructorSpecBuilder.build());
        String packageName = generatePackage(typeElement);

        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        genJavaFile(packageName, sourceBuilder);
        genJavaFile(packageName, getSourceTypeWithConstruct(typeElement,sourceClassName, packageName, className));
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenVo.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenVo.class).pkgName();
    }
}
