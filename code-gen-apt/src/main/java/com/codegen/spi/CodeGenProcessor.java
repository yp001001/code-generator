package com.codegen.spi;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author: yp
 * @date: 2024/10/12 10:27
 * @description:
 */
public interface CodeGenProcessor {

    /**
     * 需要解析的类上的注解
     * @return
     */
    Class<? extends Annotation> getAnnotation();

    /**
     * 获取生成的包路径
     * @param typeElement
     * @return
     */
    String generatePackage(TypeElement typeElement);

    /**
     * 代码生成逻辑
     * @param typeElement
     * @param roundEnvironment
     * @throws Exception
     */
    void generate(TypeElement typeElement, RoundEnvironment roundEnvironment) throws Exception;

}
