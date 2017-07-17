# [fit] Spring Boot Introduction
Presented by IMWSoftware LLC

---
## Topics

- Defining a Simple RestController
- Configuring Spring Boot applications and working with properties
- Developer tools, debugging and hot swapping
- Managing and monitoring with Spring Boot actuator
- Logging in Spring

---

# [fit] Defining a Simple RestController

---

```java
@RestController
public class SimpleController {

	@Value("${my.property}")
	private String myProperty;

	@RequestMapping("/")
	public String index() {
		return "Started application with property: " + myProperty;
	}

}
```

---

# Access the app

## <http://localhost:8080>

---

## Configuration and Properties

- Configuration and Auto Configurations
- Properties and Property files
- Profiles

---

```java
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

---

```java
@SpringBootApplication
```
- Convenience annotation that wraps configuration enabled auto configuration and component scan annotation

```java
@Configuration
```
- states that a class contains methods with the bean annotation that will register with the application context upon startup

---

```java
@EnableAutoConfiguration
```
- allows your application to try and automatically discover default configurations based on dependencies that were added
- if you start your application with the --debug flag you can see what auto configurations it matches

```java
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
```
- Excludes Spring Security Auto Configuration

---

# [fit] Adding a simple config class

---

```java
@Configuration
public class SimpleConfig {

	@Value("${my.property}")
	private String myProperty;

	private SimpleProperties simpleProperties;

	@PostConstruct
	private void postConstruct() {
		System.out.println(myProperty);
		System.out.println(simpleProperties.getDescription());
		System.out.println(simpleProperties.isEnabled());
	}
}
```

---

# Common Spring Boot application properties

## [Appendix A](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

---

### Changing common application properties

```
server.port=8888
```

### Creating your own properties

```
my.property=This is a test
```

```java
@Value("${my.property}")
private String myProperty;
```

---

## Adding a collection of custom properties

```java
@Component
@ConfigurationProperties(prefix = "simple")
@Validated
public class SimpleProperties {

	@NotNull
	private String name;
	private String description;
	private boolean enabled;

  // .. getters and setters
}
```

```
simple.name=Simple properties
simple.enabled=true
simple.description=${simple.name} is a collection of properties
# You can use placeholders in properties
```

---

# [fit] Profiles

---

## Profiles

- Allow you to assign configurations to a specific environment
- Profiles can be set in several ways such as in the spring boot maven plugin configuration, the command line, programmatically or in the properties file itself
- We will set it in the application properties file using YML as an example this time for setting up properties
- First we define a development properties file (in YML) where we redefine some properties to show development profile
- The dev properties are displayed in the startup phase

---

### Activate development profile

```
# equivalent to --spring.profiles=development
spring.profiles.active=development
```

---

### application-development.yml

```YML
my:
  property: Development Property

simple:
  name: Simple development properties
  enabled: false
  description: ${simple.name} is a collection of properties

server:
  port: 8989
```

---

# [fit] DevTools -> debugging and hot swapping

---

### DevTools Features

- Automatic server restarts
- Live reloads (locally and remotely)
- Remote debugging

---

### DevTools Dependency

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <scope>runtime</scope>
    </dependency>
```

---

### DevTools: Automatic restarts

- Done by monitoring the classpath for updates which then triggers a restart
- Because SpringBoot uses two separate class loaders; one for the underlying application infrastructure and one for your code: restart performance is better
- IDE Dependent: Simply enable "Build project automatically" for this feature to take effect
  * Simpler in Eclipse/Spring STS
  * IntelliJ "compiler.automake.allow.when.app.running"


---

### DevTools: Ignore changes somewhere in the classpath

<https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html#using-boot-devtools-restart-additional-paths>
```
spring.devtools.restart.exclude=com/imwsoftware/mongo/model/**
```

- The files in this package will no longer trigger a restart

---

### DevTools: Include something that is not in the classpath

<https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html#using-boot-devtools-restart-additional-paths>

spring.devtools.restart.additional-paths=../submodule_1,../submodule_2

- If you make a change in those folders, a restart is triggered


---

### DevTools: Include a trigger file
##### If you work with an IDE that continuously compiles changed files, you might prefer to trigger restarts only at specific times. To do this you can use a “trigger file”, which is a special file that must be modified when you want to actually trigger a restart check. Changing the file only triggers the check and the restart will only occur if DevTools has detected it has to do something. The trigger file could be updated manually, or via an IDE plugin.

To use a trigger file use the `spring.devtools.restart.trigger-file` property.
```
spring.devtools.restart.trigger-file=../trigger.txt
```

---

### DevTools: Disable automatic restarts

```
spring.devtools.restart.enabled=false
```

---

# [fit] Spring Boot Actuator

---
## Spring Boot Actuator

Spring Boot includes a number of additional features to help you monitor and manage your application when it’s pushed to production. You can choose to manage and monitor your application using HTTP endpoints, with JMX or even by remote shell (SSH or Telnet). Auditing, health and metrics gathering can be automatically applied to your application.

Actuator HTTP endpoints are only available with a Spring MVC-based application. In particular, it will not work with Jersey [unless you enable Spring MVC as well](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-actuator.html#howto-use-actuator-with-jersey).

---

## Spring Boot Actuator Features
- Actuator Spring Rest Docs
- Health indicators
- Metrics
- Ability to create a custom endpoint

---

## Spring Boot Actuator: dependencies

```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
```

Enable /docs endpoint to see documentation and curl examples

```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-actuator-docs</artifactId>
  </dependency>
```

---

## Spring Boot Actuator: endpoints

[Enpoints List](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html)
Endpoints can be disabled from application properties
```
endpoints.health.enabled=false
endpoints.docs.enabled=false
```
Once disabled, an endpoint is no longer accessible
Enable sensitive endpoints and disable security
```
endpoints.sensitive=true
management.security.enabled=false
```

---

## Spring Boot Actuator: endpoints
/health - Shows application health information

```javascript
{
  "status": "UP",
  "diskSpace": {
    "status": "UP",
    "total": 999234338816,
    "free": 514356998144,
    "threshold": 10485760
  },
  "mongo": {
    "status": "UP",
    "version": "3.4.2"
  },
  "refreshScope": {
    "status": "UP"
  },
  "hystrix": {
    "status": "UP"
  }
}
```
---

Spring Boot Actuator: change default context

```
management.context-path=/manage
```
`/health`

becomes

`/manage/health`

---

## Spring Boot Actuator : show all endpoints
/actuator

- Provides a hypermedia-based “discovery page” for the other endpoints. Requires Spring HATEOAS to be on the classpath.

---

## Spring Boot Actuator: custom endpoints
```java
public class CustomEndpoint extends AbstractEndpoint<String> {

	public CustomEndpoint(String id) {
		super(id);
	}

	public CustomEndpoint(String id, boolean sensitive) {
		super(id, sensitive);
	}

	public CustomEndpoint(String id, boolean sensitive, boolean enabled) {
		super(id, sensitive, enabled);
	}

	@Override
	public String invoke() {
		return "CustomEndpoint invoked!";
	}

}
```

---

## Spring Boot Actuator: custom endpoints: register
```java
@Configuration
public class CustomEndpointsConfig {

	@Bean
	public CustomEndpoint customEndpoint() {
		return new CustomEndpoint("custom", false);
	}
}
```
---

# [fit] Spring Logging

---

Topics

- Log formats
- Configuring logs with properties
- Creating custom log configurations

---

## Spring Logging: Default log format
- Date
- Time of the event
- Detail level (TRACE, DEBUG, INFO, WARN, ERROR)
- Process ID
- Thread Name
- Class where the event occurred
- Message

---
## Spring Logging: enable debug

```
debug=true
```
---

## Spring Logging: enable ansi console colors

```
spring.output.ansi.enabled=always
```
---

## Spring Logging: logging file and logging path

```
# Logging file - in the project root directory
logging.file=mylog.log

# Logging path
logging.path=
```

---

## Spring Logging: log levels

```
# Root level is info
logging.level.root=info

# Set individual packages to other log levels
logging.level.com.com.imwsoftware.util=debug
```

---

# [fit] The End
