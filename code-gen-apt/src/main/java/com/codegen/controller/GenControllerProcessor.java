package com.codegen.controller;

import com.codegen.BaseCodeGenProcessor;
import com.codegen.processor.controller.GenController;
import com.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author: yp
 * @date: 2024/10/12 15:23
 * @description:
 */
@AutoService(value = CodeGenProcessor.class)
public class GenControllerProcessor extends BaseCodeGenProcessor {

    public static final String CONTROLLER_SUFFIX = "Controller";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {

    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return com.codegen.processor.controller.GenController.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenController.class).pkgName();
    }
}
