package com.codegen.utils.context;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author: yp
 * @date: 2024/10/12 10:21
 * @description:工具类上下文holder
 */
public class ProcessingEnvironmentHolder {

    public static final ThreadLocal<ProcessingEnvironment> environment = new ThreadLocal<>();

    public static void setEnvironment(ProcessingEnvironment pe){
        environment.set(pe);
    }

    public static ProcessingEnvironment getEnvironment(){
        return environment.get();
    }

}
