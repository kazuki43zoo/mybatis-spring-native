# MyBatis integration with Spring Native feature

The experimental project that the MyBatis integration with Spring Native feature.

[![Java CI](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/ci.yaml)
[![Samples](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml/badge.svg)](https://github.com/kazuki43zoo/mybatis-spring-native/actions/workflows/samples.yaml)

## Requirements

* Java 11+
* [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases)
* Spring Native 0.11.1+
* Spring Boot 2.6.2+
* MyBatis Spring 2.0.7+ (not release yet, use SNAPSHOT)
* MyBatis Spring Boot 2.2.2+ (not release yet, use SNAPSHOT)

## Support features

* Configure the `SqlSessionFactory` and `SqlSessionTemplate` automatically [^1]
* Scan mapper interfaces annotated `@Mapper` automatically [^1]
* Scan mapper interfaces using `@MapperScan` instead of automatically scan [^2]
* Write static SQLs and dynamic SQLs(with OGNL expression) in SQL annotations(`@Select`/`@Insert`/etc...) [^3]
* Register a parameter and a return types to native hint(reflection hint) automatically(support standard patterns only yet)

[^1]: feature provided by mybatis-spring-boot module
[^2]: feature provided by mybatis-spring module
[^3]: feature provided by mybatis module

## Known Limitations

* https://github.com/spring-projects-experimental/spring-native/pull/1420 (As workaround, adding [the patch class](https://github.com/kazuki43zoo/mybatis-spring-native/blob/master/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/src/main/java/org/springframework/boot/context/properties/ConfigurationPropertiesNativeConfigurationProcessor.java) on your application)
* SQL provider(`@SelectProvider`/`@InertProvider`/etc...) does not support
* XML mapper does not support(cannot specify SQLs in XML file)
* etc ...

## Modules

* mybatis-spring-native-core : Provides general configuration for running on spring-native
* mybatis-spring-native-samples: Provides examples for running the MyBatis in spring-native

> **NOTE:**
> 
> We have a plan to add the mybatis-spring-native-xxx(e.g. language driver, cache, etc ...).

## How to build

```
./mvnw -Pnative clean package
```

> **NOTE:**
> Need to following environment variables are defined.
> 
> * `JAVA_HOME`
> * `GRAALVM_HOME`

## How to run with Native Image


```
./mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple
```

```
2022-01-05 02:15:02.931  INFO 61761 --- [           main] o.s.nativex.NativeListener               : AOT mode enabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 02:15:02.933  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 11.0.13 on yyyy with PID 61761 (/Users/xxxx/git-me/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple started by xxxx in /Users/xxxx/git-me/mybatis-spring-native)
2022-01-05 02:15:02.933  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 02:15:02.948  INFO 61761 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 02:15:02.951  INFO 61761 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 02:15:02.956  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 0.039 seconds (JVM running for 0.041)
2022-01-05 02:15:02.957  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-05 02:15:02.957  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-05 02:15:02.957  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-05 02:15:02.957  INFO 61761 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 02:15:02.958  INFO 61761 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 02:15:02.958  INFO 61761 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## How to run with executable jar

```
java -jar ./mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar
```

```
2022-01-05 02:15:34.582  INFO 61778 --- [           main] o.s.nativex.NativeListener               : AOT mode disabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 02:15:34.652  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 11.0.1 on yyyy with PID 61778 (/Users/xxxx/git-me/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar started by xxxx in /Users/xxxx/git-me/mybatis-spring-native)
2022-01-05 02:15:34.653  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 02:15:35.351  INFO 61778 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 02:15:35.527  INFO 61778 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 02:15:35.617  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 1.342 seconds (JVM running for 1.714)
2022-01-05 02:15:35.658  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=1, name='San Francisco', state='CA', country='USA'}
2022-01-05 02:15:35.673  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=2, name='Boston', state='MA', country='USA'}
2022-01-05 02:15:35.673  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=3, name='Portland', state='OR', country='USA'}
2022-01-05 02:15:35.673  INFO 61778 --- [           main] s.s.MybatisSpringNativeSampleApplication : City{id=4, name='NYC', state='NY', country='USA'}
2022-01-05 02:15:35.677  INFO 61778 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 02:15:35.680  INFO 61778 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
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

## How to use @MapperScan

If you use the `@MapperScan`, you should be specified the `sqlSessionTemplateRef` or `sqlSessionFactoryRef` as follows:

```java
@MapperScan(basePackages = "com.example", sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication
public class MybatisSpringNativeSampleApplication {
  // ...
}
```

## Related Links

* https://spring.io/blog/2021/12/29/go-go-graalvm-with-spring-native-my-adventures-in-native-image-ville
* https://joshlong.com/jl/blogPost/mybatis-and-spring-native.html