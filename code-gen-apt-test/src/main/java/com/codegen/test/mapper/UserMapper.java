// --- Auto Generate by yp ---
package com.codegen.test.mapper;

import com.codegen.common.DateMapper;
import com.codegen.common.GenericEnumMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
        DateMapper.class,
        GenericEnumMapper.class
    }
)
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
