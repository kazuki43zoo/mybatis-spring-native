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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class MyBatisScannedResourcesHolder {

  private Set<Class<?>> typeAliasesClasses;
  private Set<Class<?>> typeHandlerClasses;
  private Set<String> mapperLocations;

  /**
   * Return class list of scanned type aliases.
   *
   * @return class list of scanned type aliases
   */
  public Set<Class<?>> getTypeAliasesClasses() {
    return typeAliasesClasses;
  }

  /**
   * Set class list of scanned type aliases.
   *
   * @param typeAliasClasses
   *          class list of scanned type aliases
   */
  public void setTypeAliasesClasses(Set<Class<?>> typeAliasClasses) {
    this.typeAliasesClasses = typeAliasClasses;
  }

  /**
   * Return class list of scanned type handler.
   *
   * @return class list of scanned type handler
   */
  public Set<Class<?>> getTypeHandlerClasses() {
    return typeHandlerClasses;
  }

  /**
   * Set class list of scanned type handler.
   *
   * @param typeHandlerClasses
   *          class list of scanned type handler
   */
  public void setTypeHandlerClasses(Set<Class<?>> typeHandlerClasses) {
    this.typeHandlerClasses = typeHandlerClasses;
  }

  /**
   * Return location list of scanned mapper xml file.
   *
   * @return location list of scanned mapper xml file
   */
  public Set<String> getMapperLocations() {
    return mapperLocations;
  }

  /**
   * Set location list of scanned mapper xml file.
   *
   * @param mapperLocations
   *          location list of scanned mapper xml file
   */
  public void setMapperLocations(Set<String> mapperLocations) {
    this.mapperLocations = mapperLocations;
  }

  @Override
  public String toString() {
    return "MyBatisScannedResourcesHolder{" + "typeAliasesClasses=" + typeAliasesClasses + ", typeHandlerClasses="
        + typeHandlerClasses + ", mapperLocations=" + mapperLocations + '}';
  }

  static class Registrar implements ImportBeanDefinitionRegistrar {

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
      AnnotationAttributes annoAttrs = AnnotationAttributes
          .fromMap(importingClassMetadata.getAnnotationAttributes(MyBatisResourcesScan.class.getName()));
      registerBeanDefinitions(annoAttrs, registry);
    }

    protected void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
      try {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(MyBatisScannedResourcesHolder.class);
        builder.addPropertyValue("typeAliasesClasses",
            scanClasses(annoAttrs.getStringArray("typeAliasesPackages"), annoAttrs.getClass("typeAliasesSupperType"))
                .stream().filter(clazz -> !clazz.isAnonymousClass()).filter(clazz -> !clazz.isInterface())
                .filter(clazz -> !clazz.isMemberClass()).collect(Collectors.toSet()));
        builder.addPropertyValue("typeHandlerClasses",
            scanClasses(annoAttrs.getStringArray("typeHandlerPackages"), TypeHandler.class).stream()
                .filter(clazz -> !clazz.isAnonymousClass()).filter(clazz -> !clazz.isInterface())
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers())).collect(Collectors.toSet()));
        builder.addPropertyValue("mapperLocations", scanResources(annoAttrs.getStringArray("mapperLocationPatterns")));
        registry.registerBeanDefinition(MyBatisScannedResourcesHolder.class.getName() + "#" + annoAttrs.hashCode(),
            builder.getBeanDefinition());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    private Set<Class<?>> scanClasses(String[] packagePatterns, Class<?> assignableType) throws IOException {
      Set<Class<?>> classes = new HashSet<>();
      for (String packagePattern : packagePatterns) {
        Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
        for (Resource resource : resources) {
          try {
            ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
            Class<?> clazz = Resources.classForName(classMetadata.getClassName());
            if (assignableType == null || assignableType == void.class || assignableType.isAssignableFrom(clazz)) {
              classes.add(clazz);
            }
          } catch (ClassNotFoundException e) {
            // NOP
          }
        }
      }
      return classes;
    }

    public Set<String> scanResources(String[] mapperLocationPatterns) {
      try {
        String baseUri = new ClassPathResource("/").getURI().toString();
        return Stream.of(Optional.ofNullable(mapperLocationPatterns).orElseGet(() -> new String[0]))
            .flatMap(location -> Stream.of(getResources(location))).map(x -> toPath(x, baseUri))
            .collect(Collectors.toSet());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    private Resource[] getResources(String locationPattern) {
      try {
        return RESOURCE_PATTERN_RESOLVER.getResources(locationPattern);
      } catch (IOException e) {
        return new Resource[0];
      }
    }

    private String toPath(Resource resource, String baseUri) {
      try {
        String uri = resource.getURI().toString();
        String path = uri;
        if (uri.startsWith(baseUri)) {
          path = uri.replace(baseUri, "");
        } else if (uri.contains(".jar!")) {
          path = uri.replaceFirst(".*\\.jar!/", "");
        }
        return path;
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  static class RepeatingRegistrar extends Registrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
      AnnotationAttributes mapperScansAttrs = AnnotationAttributes
          .fromMap(importingClassMetadata.getAnnotationAttributes(MyBatisResourcesScan.List.class.getName()));
      if (mapperScansAttrs != null) {
        for (AnnotationAttributes annoAttrs : mapperScansAttrs.getAnnotationArray("value")) {
          this.registerBeanDefinitions(annoAttrs, registry);
        }
      }
    }
  }

}
