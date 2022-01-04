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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.ibatis.reflection.TypeParameterResolver;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.BeanFactoryNativeConfigurationProcessor;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeProxyEntry;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.util.ReflectionUtils;

/**
 * Finds and registers reflection hints for all scanned mappers in the beanFactory.
 *
 * @author Kazuki Shimizu
 * @author Josh Long
 */
public class MyBatisMapperNativeConfigurationProcessor implements BeanFactoryNativeConfigurationProcessor {

  /**
   * {@inheritDoc}
   */
  @Override
  public void process(ConfigurableListableBeanFactory beanFactory, NativeConfigurationRegistry registry) {
    TypeAccess[] typeAccesses = TypeAccess.values();
    String[] beanNames = beanFactory.getBeanNamesForType(MapperFactoryBean.class);
    for (String beanName : beanNames) {
      BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName.substring(1));
      PropertyValue mapperInterface = beanDefinition.getPropertyValues().getPropertyValue("mapperInterface");
      if (mapperInterface != null && mapperInterface.getValue() != null) {
        Class<?> mapperInterfaceType = (Class<?>) mapperInterface.getValue();
        registerReflectionType(mapperInterfaceType, typeAccesses, registry);
        registry.proxy().add(NativeProxyEntry.ofInterfaces(mapperInterfaceType));
        registerMapperRelationships(typeAccesses, mapperInterfaceType, registry);
      }
    }
  }

  private void registerMapperRelationships(TypeAccess[] typeAccesses, Class<?> mapperInterfaceType,
      NativeConfigurationRegistry registry) {
    Method[] methods = ReflectionUtils.getAllDeclaredMethods(mapperInterfaceType);
    for (Method m : methods) {
      ReflectionUtils.makeAccessible(m);
      Type resolvedReturnType = TypeParameterResolver.resolveReturnType(m, mapperInterfaceType);
      Class<?> returnType = typeToClass(resolvedReturnType, m.getReturnType());
      registerReflectionType(returnType, typeAccesses, registry);
      for (Type resolvedParameterType : TypeParameterResolver.resolveParamTypes(m, mapperInterfaceType)) {
        Class<?> parameterType = typeToClass(resolvedParameterType, Object.class);
        registerReflectionType(parameterType, typeAccesses, registry);
      }
    }
  }

  private void registerReflectionType(Class<?> type, TypeAccess[] typeAccesses, NativeConfigurationRegistry registry) {
    if (type != Object.class) {
      registry.reflection().forType(type).withAccess(typeAccesses).build();
    }
  }

  private Class<?> typeToClass(Type src, Class<?> fallback) {
    Class<?> result = null;
    if (src instanceof Class<?>) {
      if (((Class<?>) src).isArray()) {
        result = ((Class<?>) src).getComponentType();
      } else {
        result = (Class<?>) src;
      }
    } else if (src instanceof ParameterizedType) {
      Type actualType = ((ParameterizedType) src).getActualTypeArguments()[0];
      result = typeToClass(actualType, fallback);
    }
    if (result == null) {
      result = fallback;
    }
    return result;
  }

}
