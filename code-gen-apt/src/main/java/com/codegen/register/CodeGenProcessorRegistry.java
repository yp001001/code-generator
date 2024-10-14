package com.codegen.register;

import com.codegen.sort.Order;
import com.codegen.spi.CodeGenProcessor;
import org.aspectj.weaver.ast.Or;

import java.util.*;


/**
 * @author: yp
 * @date: 2024/10/12 10:32
 * @description:通过SPI机制加载所有CodeGenProcessor，识别要处理的annotation标记类
 */
public class CodeGenProcessorRegistry {

    private static Map<String, ? extends CodeGenProcessor> PROCESSORS;

    private CodeGenProcessorRegistry() {
        throw new UnsupportedOperationException();
    }


    public static Set<String> getSupportedAnnotations() {
        return PROCESSORS.keySet();
    }

    public static CodeGenProcessor find(String annotationName) {
        return PROCESSORS.get(annotationName);
    }


    /**
     * 初始化所有CodeGenProcessor
     */
    public static void initProcessors() {
        final Map<String, CodeGenProcessor> map = new LinkedHashMap<>();
        ServiceLoader<CodeGenProcessor> processors = ServiceLoader.load(CodeGenProcessor.class, CodeGenProcessor.class.getClassLoader());
        Iterator<CodeGenProcessor> iterator = processors.iterator();
        while (iterator.hasNext()) {
            CodeGenProcessor processor = iterator.next();
            map.put(processor.getAnnotation().getName(), processor);
        }

        PROCESSORS = map;
    }


}
