package com.codegen.processor.service;

import com.codegen.BaseCodeGenProcessor;
import com.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author: yp
 * @date: 2024/10/14 14:46
 * @description:
 */
@AutoService(CodeGenProcessor.class)
public class GenServiceImplProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Service";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {

    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenService.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenService.class).pkgName();
    }
}
