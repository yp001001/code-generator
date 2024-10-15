package com.codegen.test;


import com.codegen.processor.api.GenQueryRequest;
import com.codegen.processor.creator.GenCreator;
import com.codegen.processor.mapper.GenMapper;
import com.codegen.processor.query.GenQuery;
import com.codegen.processor.repository.GenRepository;
import com.codegen.processor.service.GenService;
import com.codegen.processor.update.GenUpdate;
import com.codegen.processor.vo.GenVo;
import com.jpa.support.BaseJpaAggregate;
import lombok.Data;

/**
 * @author gim 2022/1/11 10:53 下午
 */
//
@Data
@GenVo(pkgName = "com.codegen.test.vo")
@GenMapper(pkgName = "com.codegen.test.mapper")
@GenService(pkgName = "com.codegen.test.service")
@GenCreator(pkgName = "com.codegen.test.creator")
@GenUpdate(pkgName = "com.codegen.test.update")
@GenQueryRequest(pkgName = "com.codegen.test.queryrequest")
@GenQuery(pkgName = "com.codegen.test.query")
@GenRepository(pkgName = "com.codegen.test.repository")
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
