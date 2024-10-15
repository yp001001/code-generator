package com.codegen.utils.processor;

import com.codegen.utils.context.ProcessingEnvironmentHolder;
import com.codegen.utils.register.CodeGenProcessorRegistry;
import com.codegen.utils.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author: yp
 * @date: 2024/10/12 9:52
 * @description:
 */
//E:\JavaProject\code-generator\code-gen-apt-test\src\main\java\com\codegen\test\mapper
//E:\JavaProject\code-generator\code-gen-apt-test\src\main\java\com\codegen\test
@AutoService(Processor.class)
public class DefaultCodeGenProcessor extends AbstractProcessor {

    // 定义了注解处理的逻辑，该方法会在每一轮注解处理的过程中被调用，用于扫描，处理，生成代码等
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream().forEach(annotation -> {
            // 获取被注解标注的元素
            Set<? extends Element> typeElements = roundEnv.getElementsAnnotatedWith(annotation);
            // 筛选出所有的TypeElement（类，接口等元素类型）
            Set<TypeElement> types = ElementFilter.typesIn(typeElements);
            for (TypeElement typeElement : types) {
                CodeGenProcessor codeGenProcessor = CodeGenProcessorRegistry.find(annotation.getQualifiedName().toString());
                try{
                    codeGenProcessor.generate(typeElement, roundEnv);
                }catch (Exception e){
                    ProcessingEnvironmentHolder.getEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR,"代码生成异常:" + e.getMessage());
                }
            }
        });
        return false;
    }

    /**
     * 注解处理器初始化时调用，注入ProcessingEnvironment对象，提供与编译器交互的环境
     *
     * @param processingEnv environment to access facilities the tool framework
     *                      provides to the processor
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 设置上下文
        ProcessingEnvironmentHolder.setEnvironment(processingEnv);
        // SPI机制初始化CodeGenProcessor
        CodeGenProcessorRegistry.initProcessors();
    }

    // 支持的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return CodeGenProcessorRegistry.getSupportedAnnotations();
    }

    // JDK版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
