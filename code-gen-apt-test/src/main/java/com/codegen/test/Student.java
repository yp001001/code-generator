package com.codegen.test;


import com.codegen.common.constants.ValidStatus;
import com.codegen.processor.mapper.GenMapper;
import com.codegen.processor.service.GenService;
import com.codegen.processor.vo.GenVo;
import com.jpa.converter.ValidStatusConverter;
import com.jpa.support.BaseJpaAggregate;
import lombok.Data;

import javax.persistence.Convert;

@Data
@GenVo(pkgName = "com.codegen.test.vo")
@GenMapper(pkgName = "com.codegen.test.mapper")
@GenService(pkgName = "com.codegen.test.service")
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