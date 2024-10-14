package com.codegen.test;


import com.codegen.processor.mapper.GenMapper;
import com.codegen.processor.service.GenService;
import com.codegen.processor.vo.GenVo;
import com.jpa.support.BaseJpaAggregate;
import lombok.Data;

/**
 * @author gim 2022/1/11 10:53 下午
 */
//
@Data
@GenService(pkgName = "com.codegen.test.service")
@GenVo(pkgName = "com.codegen.test.vo")
@GenMapper(pkgName = "com.codegen.test.mapper")
public class User extends BaseJpaAggregate {

  private String username;

  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void valid() {
  }

  public void invalid() {
  }
}
