# MyBatis integration with Spring Native feature

The experimental project that the MyBatis integration with Spring Native feature.

[![Java CI](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml)
[![Samples](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml)

## Requirements

* Java 11+
* [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases)
* [Spring Boot](https://github.com/spring-projects/spring-boot) 2.6.3+
* [Spring Native](https://github.com/spring-projects-experimental/spring-native) 0.11.2+
* [MyBatis Spring](https://github.com/mybatis/spring) 2.0.7+
* [MyBatis Spring Boot](https://github.com/mybatis/spring-boot-starter) 2.2.2+

## Support features

### MyBatis core features

* Write static SQLs and dynamic SQLs(with OGNL expression) in SQL annotations(`@Select`/`@Insert`/etc...)
* Detect rule based mapper xml file in classpath and load SQLs (e.g. If mapper interface FQCN is `com.example.SampleMapper`, detect `com/example/SampleMapper.xml` file)
* Use SQL providers(`@SelectProvider`/`@InsertProvider`/etc...)
* Use built-in 2nd cache feature(in-memory 2nd cache)

### MyBatis Spring features

* Scan mapper interfaces using `@MapperScan` instead of automatically scan

### MyBatis Spring Boot features

* Configure the `SqlSessionFactory` and `SqlSessionTemplate` automatically
* Scan mapper interfaces annotated `@Mapper` automatically

### MyBatis Extension module features

* Support to integrate with [mybatis-thymeleaf](https://github.com/mybatis/thymeleaf-scripting)
* Support to integrate with [mybatis-velocity](https://github.com/mybatis/velocity-scripting)
* Support to integrate with [mybatis-freemarker](https://github.com/mybatis/freemarker-scripting)
* Support to integrate with [mybatis-dynamic-sql](https://github.com/mybatis/mybatis-dynamic-sql)

### MyBatis Spring Native features

* Scan type aliases, type handlers and mapper xml file using `@MyBatisResourcesScan` at build time (Alternative as configuration properties)
* Scan any classes as reflection hint using `@MyBatisResourcesScan` at build time
* Scan any resources as resource hint using `@MyBatisResourcesScan` at build time
* Register parameter types, return types and sql provider types to native hint(reflection hint) automatically(support standard patterns only yet)

## Known Limitations

* May not work if you use a subclass of `MapperFactoryBean`, See https://github.com/kazuki43zoo/mybatis-spring-native/pull/32
* Does not register nested types(hold on parameter and return type) to native hint(reflection hint)
* Fail bean initializing when specify `@Transactional` on mapper interface, See https://github.com/kazuki43zoo/mybatis-spring-native/issues/29
* etc ...

## Modules

### Integrating support modules

Provides general configurations for running on spring-native.

| Name                               | Description                                                                                                                                     |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `mybatis-spring-native-core`       | Integrating module for `mybatis` and `mybatis-spring`(`mybatis-spring-boot-starter`) module basic features                                      |
| `mybatis-spring-native-extensions` | Integrating module for extension module(using `mybatis-thymeleaf`, `mybatis-velocity`, `mybatis-freemarker` and `mybatis-dynamic-sql`) features |

### Sample modules

Provides examples for running the MyBatis in spring-native.

| Name                                                  | Description                                                                                                                      |
|-------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| `mybatis-spring-native-sample-simple`                 | The very simple sample application using annotation driven mapper (`@Select`/`@Insert`/etc...)                                   |
| `mybatis-spring-native-sample-xml`                    | The very simple sample application using xml file driven mapper                                                                  |
| `mybatis-spring-native-sample-sqlprovider`            | The very simple sample application using SQL provider driven mapper (`@SelectProvider`/`@InsertProvider`/etc...)                 |
| `mybatis-spring-native-sample-scan`                   | The sample application using `@MapperScan` and `@MyBatisResourcesScan` annotation                                                |
| `mybatis-spring-native-sample-dao`                    | The sample application with DAO pattern (without mapper interface)                                                               |
| `mybatis-spring-native-sample-thymeleaf`              | The sample application using `mybatis-thymeleaf`                                                                                 |
| `mybatis-spring-native-sample-thymeleaf-sqlgenerator` | The sample application using `SqlGenerator` provided by `mybatis-thymeleaf` without `mybatis` and `mybatis-spring` module        |
| `mybatis-spring-native-sample-velocity`               | The sample application using `mybatis-velocity`                                                                                  |
| `mybatis-spring-native-sample-freemarker`             | The sample application using `mybatis-freemarker`                                                                                |
| `mybatis-spring-native-sample-cache`                  | The sample application with built-in 2nd cache feature                                                                           |
| `mybatis-spring-native-sample-configuration`          | The sample application with customizing MyBatis's configuration using configuration properties feature(`application.properties`) |
| `mybatis-spring-native-sample-dynamic-sql`            | The sample application using `mybatis-dynamic-sql`                                                                               |

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
./mvnw -pl core,samples/simple -Pnative clean package
```

> **NOTE:**
> 
> Please replace the 'simple' part  on above example to sample's suffix value(e.g. xml, sqlprovider and more) that you want to build.

### Extension module and specific sample module

```
./mvnw -pl core,extensions,samples/thymeleaf -Pnative clean package
```

## How to run

### Run with Native Image


```
./samples/simple/target/mybatis-spring-native-sample-simple
```

```
2022-01-22 13:45:10.453  INFO 2265 --- [           main] o.s.nativex.NativeListener               : AOT mode enabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.3)

2022-01-22 13:45:10.455  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 17.0.1 on fv-az136-971 with PID 2265 (/home/runner/work/mybatis-spring-native/mybatis-spring-native/samples/simple/target/mybatis-spring-native-sample-simple started by runner in /home/runner/work/mybatis-spring-native/mybatis-spring-native)
2022-01-22 13:45:10.455  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-22 13:45:10.487  INFO 2265 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-22 13:45:10.491  INFO 2265 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-22 13:45:10.496  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 0.054 seconds (JVM running for 0.056)
2022-01-22 13:45:10.497  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : New city: City{id=4, name='NYC', state='NY', country='USA'}
2022-01-22 13:45:10.497  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-22 13:45:10.497  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-22 13:45:10.497  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-22 13:45:10.497  INFO 2265 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-22 13:45:10.498  INFO 2265 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-22 13:45:10.499  INFO 2265 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

### Run with executable jar

```
./samples/simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar
```

```
2022-01-22 13:45:14.191  INFO 2305 --- [           main] o.s.nativex.NativeListener               : AOT mode disabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.3)

2022-01-22 13:45:14.295  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 17.0.1 on fv-az136-971 with PID 2305 (/home/runner/work/mybatis-spring-native/mybatis-spring-native/samples/simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar started by runner in /home/runner/work/mybatis-spring-native/mybatis-spring-native)
2022-01-22 13:45:14.296  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-22 13:45:15.349  INFO 2305 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-22 13:45:15.578  INFO 2305 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-22 13:45:15.718  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 1.892 seconds (JVM running for 2.406)
2022-01-22 13:45:15.774  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : New city: City{id=4, name='NYC', state='NY', country='USA'}
2022-01-22 13:45:15.804  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-22 13:45:15.805  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-22 13:45:15.805  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-22 13:45:15.806  INFO 2305 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-22 13:45:15.816  INFO 2305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-22 13:45:15.823  INFO 2305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## How to install integrating modules and use it on your application

Install the `mybatis-spring-native-core` (and `mybatis-spring-native-extensions`) on your local repository as follows:

```
./mvnw -pl core,extensions clean install
```

Specify the `mybatis-spring-native-core` and `mybatis-spring-boot-starter` on `pom.xml` as follows:

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

If you use other extension modules provided by mybatis, please specify the `mybatis-spring-native-extensions` instead of `mybatis-spring-native-core`.

```xml
<dependencies>
  <dependency>
    <groupId>org.mybatis.spring.native</groupId>
    <artifactId>mybatis-spring-native-extensions</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Add snapshot repository when use MyBatis's snapshot modules. 

```xml
<repositories>
  <repository>
    <id>sonatype-oss-snapshots</id>
    <name>Sonatype OSS Snapshots Repository</name>
   <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>
```

## Notices

### Using `@MapperScan`

If you use the `@MapperScan`, you should be specified either the `sqlSessionTemplateRef` or `sqlSessionFactoryRef` as follows:

```java
@MapperScan(basePackages = "com.example.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```

### Using 2nd cache

If you use the 2nd cache feature, you need to configure serialization hints.
And we recommend defining the [JEP-290 serial filter](https://docs.oracle.com/en/java/javase/11/core/serialization-filtering1.html).  

> **IMPORTANT:**
>
> Please consider adding definition of JEP-290 serial filter when following warning log will output.
>
> ```
> 2022-01-16 13:18:21.045  WARN 21917 --- [           main] o.apache.ibatis.io.SerialFilterChecker   : As you are using functionality that deserializes object streams, it is recommended to define the JEP-290 serial filter. Please refer to https://docs.oracle.com/pls/topic/lookup?ctx=javase15&id=GUID-8296D8E8-2B93-4B9A-856E-0A65AF9B8C66
> ```

#### How to configure serialization hints

Configure using `@SerializationHint`.

```java
@NativeHint(serializables = @SerializationHint(types = { ArrayList.class, City.class, String.class, Integer.class,
    Number.class })) // Adding @SerializationHint
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```
#### How to define JEP-290 serial filter

Define `-Djdk.serialFilter`(system properties) on `buildArgs` of `native-maven-plugin` at `pom.xml`.

e.g.)

```xml
<plugin>
  <groupId>org.graalvm.buildtools</groupId>
  <artifactId>native-maven-plugin</artifactId>
  <version>${native-buildtools.version}</version>
  <extensions>true</extensions>
  <configuration>
    <buildArgs>
      <arg>-Djdk.serialFilter=org.mybatis.spring.nativex.sample.cache.*;java.util.*;java.lang.*;!*</arg> <!-- Adding definition -->
    </buildArgs>
  </configuration>
  <!-- ... -->
</plugin>
```

## Tips

### Using @MyBatisResourcesScan

In native-image, dynamic scanning does not work at runtime.
Therefore, we support to scan type aliases, type handlers and mapper xml files at build time using Spring AOT feature.
These resources will apply to MyBatis components using `ConfigurationCustomizer` and `SqlSessionFactoryBeanCustomizer` at startup time.

```java
// ...
import org.mybatis.spring.nativex.MyBatisResourcesScan;
// ...
@MyBatisResourcesScan(typeAliasesPackages = "com.example.entity", mapperLocationPatterns = "mapper/**/*Mapper.xml")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```

**Attributes:**

| Attribute | Description |
| --------- | ----------- |
| `typeAliasesPackages` | Specify package names for scanning type aliases |
| `typeAliasesSupperType` | Specify filter type(super class) for scanning type aliases |
| `typeHandlerPackages` | Specify package names for scanning type handlers |
| `mapperLocationPatterns` | Specify location patterns for scanning mapper xml files |
| `reflectionTypePackages` | Specify package names for adding as reflection hint type |
| `reflectionTypeSuperType` | Specify filter type(super class) for scanning reflection type |
| `typeAccesses` | Specify access scopes for applying scanned classes to reflection hint |
| `resourceLocationPatterns` | Specify location patterns for adding as resource hint file |

## Related Links

* https://spring.io/blog/2021/12/29/go-go-graalvm-with-spring-native-my-adventures-in-native-image-ville
* https://joshlong.com/jl/blogPost/mybatis-and-spring-native.html
* https://github.com/mybatis/spring/issues/635
* https://github.com/mybatis/spring-boot-starter/issues/617

## Special Thanks

Thanks for helping this project creation!!

* Josh Long([@joshlong](https://github.com/joshlong))  
  Josh provided [the first sample application](https://github.com/joshlong/mybatis-spring-native/tree/mybatis-spring).


* Stéphane Nicoll([@snicoll](https://github.com/snicoll))  
  Stéphane resolved and helped some issues for running MyBatis on native image. 
