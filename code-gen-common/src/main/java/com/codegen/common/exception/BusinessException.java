package com.codegen.common.exception;


import com.codegen.common.constants.BaseEnum;


public class BusinessException extends RuntimeException {

  private final BaseEnum msg;
  private Object data;

  public BusinessException(BaseEnum msg) {
    super(msg.getName());
    this.msg = msg;
  }

  public BusinessException(BaseEnum msg, Object data) {
    super(msg.getName());
    this.msg = msg;
    this.data = data;
  }

  public BaseEnum getMsg() {
    return msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
