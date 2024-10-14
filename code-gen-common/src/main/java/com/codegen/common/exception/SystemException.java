package com.codegen.common.exception;


public class SystemException extends RuntimeException {

  private String msg;

  public SystemException(String msg) {
    super(msg);
    this.msg = msg;
  }
}
