package org.mybatis.spring.nativex;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface MyMapper {
  @Select("select #{value} as value")
  Result ping(Param param);

  class Param {
    private int value;

    public void setValue(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  class Result {
    private int value;

    public void setValue(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
