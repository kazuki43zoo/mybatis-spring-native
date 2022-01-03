package org.mybatis.spring.nativex;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = "org.mybatis.spring.nativex", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisSpringNativeSampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybatisSpringNativeSampleApplication.class, args);
  }

  @Bean
  ApplicationRunner runner(MyMapper mapper) {
    return args -> {
      MyMapper.Param p = new MyMapper.Param();
      p.setValue(10);
      System.out.println(mapper.ping(p).getValue());
    };
  }

}
