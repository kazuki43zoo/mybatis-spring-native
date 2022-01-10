/*
 *    Copyright 2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.spring.nativex.sample.thymeleaf;

import org.mybatis.scripting.thymeleaf.SqlGenerator;
import org.mybatis.scripting.thymeleaf.SqlGeneratorConfig;
import org.mybatis.scripting.thymeleaf.support.TemplateFilePathProvider;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.nativex.MyBatisResourcesScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@MyBatisResourcesScan(resourceLocationPatterns = "org/mybatis/spring/nativex/sample/**/*.sql")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {

  private static final Logger log = LoggerFactory.getLogger(MybatisSpringNativeSampleApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(MybatisSpringNativeSampleApplication.class, args);
  }

  @Bean
  ApplicationRunner runner(CityMapper mapper) {
    return args -> {
      log.info("Run with mapper.");
      City newCity = new City(null, "NYC", "NY", "USA");
      mapper.insert(newCity);
      log.info("New city: {}", newCity);
      mapper.findAll().forEach(x -> log.info("{}", x));
    };
  }

  @Bean
  ApplicationRunner runnerWithJdbcOperations(NamedParameterJdbcOperations operations, SqlGenerator sqlGenerator) {
    return args -> {
      log.info("Run with jdbc operations.");
      operations.query(sqlGenerator.generate("CityMapper/CityMapper-findAll.sql", null),
          new BeanPropertyRowMapper<>(City.class)).forEach(x -> log.info("{}", x));
    };
  }

  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() {
    return configuration -> configuration.setDefaultSqlProviderType(TemplateFilePathProvider.class);
  }

  @Bean
  SqlGenerator sqlGenerator() {
    return new SqlGenerator(SqlGeneratorConfig.newInstanceWithCustomizer(
        c -> c.getTemplateFile().setBaseDir("org/mybatis/spring/nativex/sample/thymeleaf/")));
  }

}
