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
package org.mybatis.spring.nativex;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.TypeAccess;

/**
 * The annotation that indicates scan rules of resources for running on native-image.
 *
 * @author Kazuki Shimizu
 */
@Import(MyBatisScannedResourcesHolder.Registrar.class)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyBatisResourcesScan.List.class)
public @interface MyBatisResourcesScan {

  /**
   * Return package names for scanning type aliases.
   * <P>
   * Default is none.
   * </P>
   *
   * @return package names for scanning type aliases
   */
  String[] typeAliasesPackages() default {};

  /**
   * Return the filter type(super class) for scanning type aliases.
   * <P>
   * Default is none.
   * </P>
   *
   * @return the filter type for scanning type aliases
   */
  Class<?> typeAliasesSupperType() default void.class;

  /**
   * Return package names for scanning type handler.
   * <P>
   * Default is none.
   * </P>
   *
   * @return package names for scanning type handler
   */
  String[] typeHandlerPackages() default {};

  /**
   * Return location patterns for scanning mapper xml file under classpath.
   * <P>
   * Default is none.
   * </P>
   *
   * @return location patterns for scanning mapper xml file under classpath
   */
  String[] mapperLocationPatterns() default {};

  /**
   * Return package names for scanning reflection type.
   * <P>
   * Default is none.
   * </P>
   *
   * @return package names for scanning reflection type
   */
  String[] reflectionTypePackages() default {};

  /**
   * Return the filter type(super class) for scanning reflection type.
   * <P>
   * Default is none.
   * </P>
   *
   * @return the filter type for scanning reflection type
   */
  Class<?> reflectionTypeSupperType() default void.class;

  /**
   * Return access scopes for applying scanned classes to reflection hint.
   * <P>
   * Default is all scope.
   * </P>
   *
   * @return access scopes for applying scanned classes to reflection hint
   */
  TypeAccess[] typeAccesses() default {};

  @Import(MyBatisScannedResourcesHolder.RepeatableRegistrar.class)
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    MyBatisResourcesScan[] value() default {};
  }

}
