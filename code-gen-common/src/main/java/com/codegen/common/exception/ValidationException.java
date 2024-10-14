package com.codegen.common.exception;

import com.codegen.common.model.ValidateResult;
import lombok.Getter;

import java.util.List;


public class ValidationException extends RuntimeException {
  @Getter
  private List<ValidateResult> result;
  public ValidationException(List<ValidateResult> list){
    super();
    this.result = list;
  }
}
