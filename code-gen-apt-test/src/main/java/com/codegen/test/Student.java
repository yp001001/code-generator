package com.codegen.test;


import com.codegen.common.constants.ValidStatus;
import com.codegen.processor.api.GenQueryRequest;
import com.codegen.processor.creator.GenCreator;
import com.codegen.processor.mapper.GenMapper;
import com.codegen.processor.query.GenQuery;
import com.codegen.processor.repository.GenRepository;
import com.codegen.processor.service.GenService;
import com.codegen.processor.update.GenUpdate;
import com.codegen.processor.vo.GenVo;
import com.jpa.converter.ValidStatusConverter;
import com.jpa.support.BaseJpaAggregate;
import lombok.Data;

import javax.persistence.Convert;


@Data
@GenVo(pkgName = "com.codegen.test.vo")
@GenMapper(pkgName = "com.codegen.test.mapper")
@GenService(pkgName = "com.codegen.test.service")
@GenCreator(pkgName = "com.codegen.test.creator")
@GenUpdate(pkgName = "com.codegen.test.update")
@GenQueryRequest(pkgName = "com.codegen.test.queryrequest")
@GenQuery(pkgName = "com.codegen.test.query")
@GenRepository(pkgName = "com.codegen.test.repository")
public class Student extends BaseJpaAggregate {

  @Convert(converter = ValidStatusConverter.class)
  private ValidStatus validStatus;

  public void init() {
    setValidStatus(ValidStatus.VALID);
  }

  public void valid(){
    setValidStatus(ValidStatus.VALID);
  }

  public void invalid(){
    setValidStatus(ValidStatus.INVALID);
  }

}