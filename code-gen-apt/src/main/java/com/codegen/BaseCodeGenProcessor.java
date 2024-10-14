package com.codegen;

import com.codegen.common.annotation.FieldDesc;
import com.codegen.common.annotation.TypeConverter;
import com.codegen.context.ProcessingEnvironmentHolder;
import com.codegen.processor.DefaultNameContext;
import com.codegen.processor.controller.GenController;
import com.codegen.processor.controller.GenControllerProcessor;
import com.codegen.processor.mapper.GenMapper;
import com.codegen.processor.mapper.GenMapperProcessor;
import com.codegen.processor.vo.GenVo;
import com.codegen.processor.vo.VoCodeGenProcessor;
import com.codegen.spi.CodeGenProcessor;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.squareup.javapoet.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author: yp
 * @date: 2024/10/12 11:43
 * @description:
 */
public abstract class BaseCodeGenProcessor implements CodeGenProcessor {

    public static final String PREFIX = "Base";

    @Override
    public void generate(TypeElement typeElement, RoundEnvironment roundEnvironment) throws Exception {
        generateClass(typeElement, roundEnvironment);
    }

    /**
     * 生成class类
     *
     * @param typeElement
     * @param roundEnvironment
     */
    protected abstract void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment);


    /**
     * 获取名称默认上下文
     *
     * @param typeElement
     * @return
     */
    public DefaultNameContext getNameContext(TypeElement typeElement) {
        DefaultNameContext context = new DefaultNameContext();
        String controllerName = typeElement.getSimpleName() + GenControllerProcessor.CONTROLLER_SUFFIX;
        String mapperName = typeElement.getSimpleName() + GenMapperProcessor.SUFFIX;
        String voName = typeElement.getSimpleName() + VoCodeGenProcessor.SUFFIX;

        context.setControllerClassName(controllerName);
        context.setMapperClassName(mapperName);
        context.setVoClassName(voName);

        Optional.ofNullable(typeElement.getAnnotation(GenController.class)).ifPresent(anno -> {
            context.setControllerPackageName(anno.pkgName());
        });
        Optional.ofNullable(typeElement.getAnnotation(GenMapper.class)).ifPresent(anno -> {
            context.setMapperPackageName(anno.pkgName());
        });
        Optional.ofNullable(typeElement.getAnnotation(GenVo.class)).ifPresent(anno -> {
            context.setVoPackageName(anno.pkgName());
        });

        return context;
    }


    /**
     * 过滤属性
     *
     * @param typeElement
     * @param predicate
     * @return
     */
    protected Set<VariableElement> findField(TypeElement typeElement, Predicate<VariableElement> predicate) {
        // 获取对应类/接口的元素
        List<? extends Element> fileTypes = typeElement.getEnclosedElements();
        Set<VariableElement> variableElements = new LinkedHashSet<>();
        for (VariableElement variableElement : ElementFilter.fieldsIn(fileTypes)) {
            if (predicate.test(variableElement)) {
                variableElements.add(variableElement);
            }
        }
        return variableElements;
    }


    /**
     * 获取父类
     *
     * @param typeElement
     * @return
     */
    protected TypeElement getSuperClass(TypeElement typeElement) {
        TypeMirror parent = typeElement.getSuperclass();
        if (parent instanceof DeclaredType) {
            Element elt = ((DeclaredType) parent).asElement();
            if (elt instanceof TypeElement) {
                return (TypeElement) elt;
            }
        }

        return null;
    }

    protected String getFieldDesc(VariableElement ve) {
        return Optional.ofNullable(ve.getAnnotation(FieldDesc.class))
                .map(m -> m.name()).orElse(ve.getSimpleName().toString());
    }

    protected String getFieldDefaultName(VariableElement ve) {
        return ve.getSimpleName().toString().substring(0, 1).toUpperCase() +
                ve.getSimpleName().toString().substring(1);
    }

    /**
     * 生成set和get方法
     *
     * @param builder
     * @param variableElements
     */
    protected void addSetterAndGetterMethod(TypeSpec.Builder builder, Set<VariableElement> variableElements) {
        for (VariableElement ve : variableElements) {
            TypeName typeName = TypeName.get(ve.asType());
            FieldSpec.Builder filedSpec = FieldSpec.builder(typeName, ve.getSimpleName().toString(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Schema.class)
                            .addMember("title", "$S", getFieldDesc(ve))
                            .build());
            builder.addField(filedSpec.build());
            String filedName = getFieldDefaultName(ve);

            MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get" + filedName)
                    .returns(typeName)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", ve.getSimpleName().toString());

            MethodSpec.Builder setMethod = MethodSpec.methodBuilder("set" + filedName)
                    .returns(void.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeName, ve.getSimpleName().toString())
                    .addStatement("this.$L = $L", ve.getSimpleName().toString(), ve.getSimpleName().toString());

            builder.addMethod(getMethod.build());
            builder.addMethod(setMethod.build());
        }
    }

    /**
     * 应用转化器
     *
     * @param builder
     * @param variableElements
     */
    protected void addSetterAndGetterMethodWithConverter(TypeSpec.Builder builder, Set<VariableElement> variableElements) {
        for (VariableElement ve : variableElements) {
            TypeName typeName;
            if (Objects.nonNull(ve.getAnnotation(TypeConverter.class))) {
                //这里处理下泛型的情况，比如List<String> 这种，TypeConverter FullName 用逗号分隔"java.lang.List
                String fullName = ve.getAnnotation(TypeConverter.class).toTypeFullName();
                Iterable<String> classes = Splitter.on(",").split(fullName);
                int size = Iterables.size(classes);
                if (size > 1) {
                    typeName = ParameterizedTypeName.get(ClassName.bestGuess(Iterables.get(classes, 0)),
                            ClassName.bestGuess(Iterables.get(classes, 1)));
                } else {
                    typeName = ClassName.bestGuess(ve.getAnnotation(TypeConverter.class).toTypeFullName());
                }
            } else {
                typeName = TypeName.get(ve.asType());
            }

            FieldSpec.Builder filedSpec = FieldSpec.builder(typeName, ve.getSimpleName().toString(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Schema.class)
                            .addMember("title", "$s", getFieldDesc(ve))
                            .build());
            builder.addField(filedSpec.build());
            String filedName = getFieldDefaultName(ve);

            MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get" + filedName)
                    .returns(typeName)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", ve.getSimpleName().toString());

            MethodSpec.Builder setMethod = MethodSpec.methodBuilder("set" + filedName)
                    .returns(Void.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeName, ve.getSimpleName().toString())
                    .addStatement("this.$L = $L", ve.getSimpleName().toString(), ve.getSimpleName().toString());

            builder.addMethod(getMethod.build());
            builder.addMethod(setMethod.build());
        }
    }

    protected void addIdSetterAndGetter(TypeSpec.Builder builder) {
        MethodSpec.Builder getMethod = MethodSpec.methodBuilder("getId")
                .returns(ClassName.get(Long.class))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", "id");
        MethodSpec.Builder setMethod = MethodSpec.methodBuilder("setId")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.LONG, "id")
                .addStatement("this.$L = $L", "id", "id");
        builder.addMethod(getMethod.build());
        builder.addMethod(setMethod.build());
    }

    public void genJavaSourceFile(String packageName, String pathStr, TypeSpec.Builder typeSpecBuilder) {
        TypeSpec typeSpec = typeSpecBuilder.build();
        JavaFile javaFile = JavaFile
                .builder(packageName, typeSpec)
                .addFileComment("--- Auto Generate by yp ---")
                .build();
        String packagePath = packageName.replace(".", File.separator) + File.separator + typeSpec.name + ".java";
        try {
            Path path = Paths.get(pathStr);
            File file = new File(path.toFile().getAbsolutePath());
            if (!file.exists()) {
                return;
            }
            String sourceFileName = path.toFile().getAbsolutePath() + File.separator + packagePath;
            File sourceFile = new File(sourceFileName);
            if (!sourceFile.exists()) {
                javaFile.writeTo(file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public TypeSpec.Builder getSourceType(String sourceName, String packageName, String superClassName){
        TypeSpec.Builder sourceBuilder = TypeSpec.classBuilder(sourceName)
                .superclass(ClassName.get(packageName, superClassName))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
        return sourceBuilder;
    }

    public TypeSpec.Builder getSourceTypeWithConstruct(TypeElement e, String sourceName,
                                                       String packageName, String superClassName){
        TypeSpec.Builder sourceBuilder = TypeSpec.classBuilder(sourceName)
                .superclass(ClassName.get(packageName, superClassName))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);

        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(e.asType()), "source")
                .addModifiers(Modifier.PUBLIC);

        constructorSpecBuilder.addStatement("super(source)");

        sourceBuilder.addMethod(constructorSpecBuilder.build());
        sourceBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build());

        return sourceBuilder;
    }

    protected void genJavaFile(String packageName, TypeSpec.Builder typeSpecBuilder) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpecBuilder.build())
                .addFileComment("---Auto Generated by yp ---").build();
        try {
            javaFile.writeTo(ProcessingEnvironmentHolder.getEnvironment().getFiler());
        } catch (IOException e) {
            ProcessingEnvironmentHolder.getEnvironment().getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

}
