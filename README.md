# MyBatis integration with Spring Native feature

The experimental project that the MyBatis integration with Spring Native feature.

[![Java CI](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml)
[![Samples](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml)

## Requirements

* Java 11+
* [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases)
* [Spring Boot](https://github.com/spring-projects/spring-boot) 2.6.2+
* [Spring Native](https://github.com/spring-projects-experimental/spring-native) 0.11.2+ (not release yet, use SNAPSHOT)
* [MyBatis Spring](https://github.com/mybatis/spring) 2.0.7+ (not release yet, use SNAPSHOT)
* [MyBatis Spring Boot](https://github.com/mybatis/spring-boot-starter) 2.2.2+ (not release yet, use SNAPSHOT)

## Support features

### MyBatis core features

* Write static SQLs and dynamic SQLs(with OGNL expression) in SQL annotations(`@Select`/`@Insert`/etc...)
* Enable to detect rule based mapper xml file in classpath and load SQLs (e.g. If mapper interface FQCN is `com.example.SampleMapper`, detect `com/example/SampleMapper.xml` file)
* Enable to use SQL providers(`@SelectProvider`/`@InsertProvider`/etc...)

### MyBatis Spring features

* Scan mapper interfaces using `@MapperScan` instead of automatically scan

### MyBatis Spring Boot features

* Configure the `SqlSessionFactory` and `SqlSessionTemplate` automatically
* Scan mapper interfaces annotated `@Mapper` automatically

### MyBatis Spring Native features

* Scan type aliases, type handlers and mapper xml file using `@MyBatisResourcesScan` at build time (Alternative as configuration properties)
* Register parameter types, return types and sql provider types to native hint(reflection hint) automatically(support standard patterns only yet)


## Known Limitations

* Does not work to customize MyBatis components using configuration properties (prefixed with `mybatis.`), See https://github.com/kazuki43zoo/mybatis-spring-native/issues/13
* Does not register nested types(hold on parameter and return type) to native hint(reflection hint)
* etc ...

## Modules

### Integrating support modules

Provides general configurations for running on spring-native.

* `mybatis-spring-native-core` : Integrating module for mybatis and mybatis-spring(mybatis-spring-boot) basic features

### Sample modules

Provides examples for running the MyBatis in spring-native.

* `mybatis-spring-native-sample-simple` : Very simple sample application using annotation driven mapper (`@Select`/`@Insert`/etc...)
* `mybatis-spring-native-sample-xml` : Very simple sample application using xml file driven mapper
* `mybatis-spring-native-sample-sqlprovider` : Very simple sample application using SQL provider driven mapper (`@SelectProvider`/`@InsertProvider`/etc...)
* `mybatis-spring-native-sample-scan` : sample application using `@MapperScan` and `@MyBatisResourcesScan` annotation

> **NOTE:**
> 
> We have a plan to add the mybatis-spring-native-xxx(e.g. language driver, cache, etc ...).

## How to build

> **NOTE: Pre Conditions**
> 
> Need to following environment variables are defined.
>
> * `JAVA_HOME`
> * `GRAALVM_HOME`

### All modules

```
./mvnw -Pnative clean package
```
> **WARNING:**
>
> **Building all modules takes long time.**


### Core module and specific sample module

```
./mvnw -pl mybatis-spring-native-core,mybatis-spring-native-samples/mybatis-spring-native-sample-simple -Pnative clean package
```

> **NOTE:**
> 
> Please replace the 'simple' part  on above example to sample's suffix value(e.g. xml, sqlprovider and more) that you want to build.

## How to run

### Run with Native Image


```
./mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple
```

```
2022-01-05 17:47:15.522  INFO 2507 --- [           main] o.s.nativex.NativeListener               : AOT mode enabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 17:47:15.525  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 17.0.1 on fv-az137-109 with PID 2507 (/home/runner/work/mybatis-spring-native/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple started by runner in /home/runner/work/mybatis-spring-native/mybatis-spring-native)
2022-01-05 17:47:15.525  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 17:47:15.540  INFO 2507 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 17:47:15.544  INFO 2507 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 17:47:15.552  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 0.041 seconds (JVM running for 0.043)
2022-01-05 17:47:15.552  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : New city: City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 17:47:15.553  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-05 17:47:15.553  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-05 17:47:15.553  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-05 17:47:15.553  INFO 2507 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 17:47:15.554  INFO 2507 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 17:47:15.555  INFO 2507 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

### Run with executable jar

```
./mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar
```

```
2022-01-05 17:47:16.849  INFO 2522 --- [           main] o.s.nativex.NativeListener               : AOT mode disabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 17:47:16.987  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 17.0.1 on fv-az137-109 with PID 2522 (/home/runner/work/mybatis-spring-native/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar started by runner in /home/runner/work/mybatis-spring-native/mybatis-spring-native)
2022-01-05 17:47:16.987  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 17:47:18.174  INFO 2522 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 17:47:18.510  INFO 2522 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 17:47:18.626  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 2.323 seconds (JVM running for 2.968)
2022-01-05 17:47:18.661  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : New city: City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 17:47:18.680  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-05 17:47:18.681  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-05 17:47:18.682  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-05 17:47:18.682  INFO 2522 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 17:47:18.687  INFO 2522 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 17:47:18.695  INFO 2522 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## How to install mybatis-spring-native-core and use it on your application

Install the mybatis-spring-native-core on your local repository as follows:

```
./mvnw -pl mybatis-spring-native-core clean install
```

Specify the mybatis-spring-native-core and mybatis-spring-boot-starter on `pom.xml` as follows:

```xml
<dependencies>
  <dependency>
    <groupId>org.mybatis.spring.native</groupId>
    <artifactId>mybatis-spring-native-core</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.2-SNAPSHOT</version>
  </dependency>
</dependencies>
```

```xml
<repositories>
  <repository>
    <id>sonatype-oss-snapshots</id>
    <name>Sonatype OSS Snapshots Repository</name>
   <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>
```

## Tips

### Notice for using `@MapperScan`

If you use the `@MapperScan`, you should be specified either the `sqlSessionTemplateRef` or `sqlSessionFactoryRef` as follows:

```java
@MapperScan(basePackages = "com.example", sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```

### Programmatic configuration

Alternative as the configuration properties based configuration, you can use the programmatic based configuration as follows:

> **NOTE:**
>
> This solution can be used as workaround for https://github.com/kazuki43zoo/mybatis-spring-native/issues/13 too.

```java
// ...
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
// ...
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() { // Add bean definition for ConfigurationCustomizer
    return configuration -> {
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.getTypeAliasRegistry().registerAlias(LocalDate.class);
    };
  }
}
```

### Using @MyBatisResourcesScan

In native-image, dynamic scanning does not work at runtime.
Therefore, we support to scan type aliases, type handlers and mapper xml files at build time using Spring AOT feature.
These resources will apply to MyBatis components using `ConfigurationCustomizer` and `SqlSessionFactoryBeanCustomizer` at startup time.

```java
// ...
import org.mybatis.spring.nativex.MyBatisResourcesScan;
// ...
@MyBatisResourcesScan(typeAliasesPackages = "org.mybatis.spring.nativex.sample.scan", mapperLocationPatterns = "mapper/**/*Mapper.xml")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```

**Attributes:**

* `typeAliasesPackages` : Specify package names for scanning type aliases
* `typeAliasesSupperType` : Specify filter type(super class) for scanning type aliases
* `typeHandlerPackages` : Specify package names for scanning type handlers
* `mapperLocationPatterns` : Specify location patterns for scanning mapper xml files

## Related Links

* https://spring.io/blog/2021/12/29/go-go-graalvm-with-spring-native-my-adventures-in-native-image-ville
* https://joshlong.com/jl/blogPost/mybatis-and-spring-native.html
* https://github.com/mybatis/spring/issues/635
* https://github.com/mybatis/spring-boot-starter/issues/617
