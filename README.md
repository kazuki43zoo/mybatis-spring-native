# MyBatis integration with Spring Native feature

The experimental project that the MyBatis integration with Spring Native feature.

## Requirements

* Java 11+
* [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases)
* Spring Native 0.11.1+
* Spring Boot 2.6+
* MyBatis Spring 2.0.7+ (not release yet, use SNAPSHOT)
* MyBatis Spring Boot 2.2.2+ (not release yet, use SNAPSHOT)

## Support features

* Configure the `SqlSessionFactory` and `SqlSessionTemplate` automatically
* Scan mapper interfaces annotated `@Mapper` automatically
* Scan mapper interfaces using `@MapperScan` instead of automatically scan
* Register a parameter and a return types to native hint(reflection hint) automatically(support standard patterns only)
* Write static SQLs and dynamic SQLs(with OGNL expression) in SQL annotations(`@Select`/`@Insert`/etc...)

## Known Limitations

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
2022-01-05 01:36:08.922  INFO 61022 --- [           main] o.s.nativex.NativeListener               : AOT mode enabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 01:36:08.924  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 11.0.13 on yyyy with PID 61022 (/Users/xxxx/git-me/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple started by xxxx in /Users/xxxx/git-me/mybatis-spring-native)
2022-01-05 01:36:08.924  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 01:36:08.939  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 0.034 seconds (JVM running for 0.035)
2022-01-05 01:36:08.940  INFO 61022 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 01:36:08.942  INFO 61022 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 01:36:08.943  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=10}
2022-01-05 01:36:08.944  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=10}
2022-01-05 01:36:08.944  INFO 61022 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=200}
2022-01-05 01:36:08.944  INFO 61022 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 01:36:08.944  INFO 61022 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## How to run with executable jar

```
java -jar ./mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar
```

```
2022-01-05 01:38:38.464  INFO 61044 --- [           main] o.s.nativex.NativeListener               : AOT mode disabled

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.2)

2022-01-05 01:38:38.528  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : Starting MybatisSpringNativeSampleApplication v0.0.1-SNAPSHOT using Java 11.0.1 on yyyy with PID 61044 (/Users/xxxx/git-me/mybatis-spring-native/mybatis-spring-native-samples/mybatis-spring-native-sample-simple/target/mybatis-spring-native-sample-simple-0.0.1-SNAPSHOT-exec.jar started by xxxx in /Users/xxxx/git-me/mybatis-spring-native)
2022-01-05 01:38:38.529  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : No active profile set, falling back to default profiles: default
2022-01-05 01:38:39.304  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : Started MybatisSpringNativeSampleApplication in 1.124 seconds (JVM running for 1.48)
2022-01-05 01:38:39.319  INFO 61044 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-01-05 01:38:39.504  INFO 61044 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-01-05 01:38:39.544  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=10}
2022-01-05 01:38:39.591  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=10}
2022-01-05 01:38:39.594  INFO 61044 --- [           main] s.s.MybatisSpringNativeSampleApplication : Ping Result: Result{value=200}
2022-01-05 01:38:39.598  INFO 61044 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-01-05 01:38:39.601  INFO 61044 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## How to install mybatis-spring-native-core and use it on your application

Install the mybatis-spring-native-core on your local repository as follows:

```
./mvnw -pl mybatis-spring-native-core clean install
```

Specify the mybatis-spring-native-core on `pom.xml` as follows:

```xml
<dependency>
  <groupId>org.mybatis.spring.native</groupId>
  <artifactId>mybatis-spring-native-core</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```