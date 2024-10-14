package com.codegen.common.constants;

import java.util.Optional;

public enum ValidStatus implements BaseEnum<ValidStatus> {
  /**
   * 有效
   */
  VALID(1, "valid"),
  /**
   * 无效
   */
  INVALID(0, "invalid");
  private Integer code;
  private String name;

  ValidStatus(Integer code, String msg) {
    this.code = code;
    this.name = msg;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }

  public static Optional<ValidStatus> of(Integer code) {
    return Optional.ofNullable(BaseEnum.parseByCode(ValidStatus.class, code));
  }
}