package org.mybatis.spring.nativex;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.BeanFactoryNativeConfigurationProcessor;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeProxyEntry;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Finds and registers reflection hints for all the MyBatis-annotated mappers in the beanFactory.
 */
public class MyBatisMapperNativeConfigurationProcessor implements BeanFactoryNativeConfigurationProcessor {

  @Override
  public void process(ConfigurableListableBeanFactory beanFactory, NativeConfigurationRegistry registry) {
    TypeAccess[] values = TypeAccess.values();
    String[] beanNames = beanFactory.getBeanNamesForType(MapperFactoryBean.class);
    for (String beanName : beanNames) {
      BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName.substring(1));
      PropertyValue getMapperInterface = beanDefinition.getPropertyValues().getPropertyValue("mapperInterface");
      if (getMapperInterface.getValue() != null) {
        Class<?> interfaceForMapper = (Class<?>) getMapperInterface.getValue();
        registry.reflection().forType(interfaceForMapper).withAccess(values).build();
        registry.proxy().add(NativeProxyEntry.ofInterfaces(interfaceForMapper));
        registerMapperRelationships(values, interfaceForMapper, registry);
      }
    }
  }

  private void registerMapperRelationships(TypeAccess[] typeAccesses, Class<?> mapperInterfaceType, NativeConfigurationRegistry registry) {
    Method[] methods = ReflectionUtils.getDeclaredMethods(mapperInterfaceType);
    for (Method m : methods) {
      ReflectionUtils.makeAccessible(m);
      registry.reflection().forType(m.getReturnType()).withAccess(typeAccesses).build();
      for (Class<?> parameterClass : m.getParameterTypes()) {
        registry.reflection().forType(parameterClass).withAccess(typeAccesses).build();
      }
    }
  }
}
