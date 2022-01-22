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

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.BeanDefinitionPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

class MapperFactoryBeanPostProcessor implements BeanDefinitionPostProcessor, BeanFactoryAware {

  private static final String MAPPER_FACTORY_BEAN = "org.mybatis.spring.mapper.MapperFactoryBean";

  private ConfigurableBeanFactory beanFactory;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (ConfigurableBeanFactory) beanFactory;
  }

  @Override
  public void postProcessBeanDefinition(String beanName, RootBeanDefinition beanDefinition) {
    if (ClassUtils.isPresent(MAPPER_FACTORY_BEAN, this.beanFactory.getBeanClassLoader())) {
      resolveRepositoryFactoryBeanTypeIfNecessary(beanDefinition);
    }
  }

  private void resolveRepositoryFactoryBeanTypeIfNecessary(RootBeanDefinition beanDefinition) {
    if (!beanDefinition.hasBeanClass() || !MapperFactoryBean.class.isAssignableFrom(beanDefinition.getBeanClass())) {
      return;
    }
    if (beanDefinition.getResolvableType().hasUnresolvableGenerics()) {
      Class<?> mapperInterface = getMapperInterface(beanDefinition);
      if (mapperInterface != null) {
        beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(
                beanDefinition.getBeanClass(), mapperInterface));
      }
    }
  }

  private Class<?> getMapperInterface(RootBeanDefinition beanDefinition) {
    try {
      return (Class<?>) beanDefinition.getPropertyValues().get("mapperInterface");
    } catch (Exception ex) {
      return null;
    }
  }

}
