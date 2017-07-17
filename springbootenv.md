# [fit] Setting up the Spring environment
Presented by IMWSoftware LLC

---

## Download and Install JDK 8
http://www.oracle.com/technetwork/java/javase/downloads

```bash

$ java -version

$ java -version
java version "1.8.0_73"

```

---

## Download and install Maven
https://maven.apache.org/download.cgi -> (must be version 3 or greater)

```bash
$ mvn --version
Apache Maven 3.3.3
```

---

## Install SDK Man (optional)

[SDKMAN](http://sdkman.io/install.html)

```
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk version
```

---

## Install Spring Boot CLI (optional)
[Installing Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html)

*Spring Boot CLI is a command line interface for creating production ready Spring Boot projects via CLI*

---

## Spring Boot CLI: Install via SDKMAN

```bash
$ sdk install springboot
```

---

## Spring Boot CLI: Manual installation  
**Note:** *versions may change: find the latest version here:*  
<http://repo.spring.io/release/org/springframework/boot/spring-boot-cli/>

<http://repo.spring.io/release/org/springframework/boot/spring-boot-cli/1.5.4.RELEASE/spring-boot-cli-1.5.4.RELEASE-bin.zip>
<http://repo.spring.io/release/org/springframework/boot/spring-boot-cli/1.5.4.RELEASE/spring-boot-cli-1.5.4.RELEASE-bin.tar.gz>

* Unzip/untar Spring Boot CLI
* add Spring Boot CLI to the system path

---

## Spring Boot CLI: Test installation

```bash
$ spring --version
Spring Boot v1.5.4.RELEASE
```

---

## Install Docker (optional)
https://docs.docker.com/engine/installation/

---

## Creating a Spring Boot project
- Using the Spring Initializr website
- Using the Spring Command Line Interface (CLI)
- Creating a Spring Boot application with the CLI
- Creating a Spring Boot application with Spring STS

---

## Spring Initializr -> https://start.spring.io/
Click on: Switch to the full version
Select or type in the 'Search for dependencies'

1. Web
1. Actuator Docs
1. MongoDB
1. Actuator

---

## Spring Initializr

Define Project Metadata -> Artifact coordinates

- Group, Artifact, Name, Description, Package Name, Packaging (Jar or War)
Java Version (1.8)

---

## Spring Initializr
- Click generate project
Download the generated zip file, unzip it and import it into your IDE or bulid from the command line using Maven

---

## Using the Spring Command Line Interface (CLI)
```bash
$ spring help
$ spring shell
```

To see available commands type 'help'
You can list the capabilities of the service using the --list flag

```bash
$ init --list
```

---

## Simple Groovy App

```groovy
@RestController
class RestApp {
	@RequestMapping("/")
	String index() {
		"Success!"
	}
}
```

```bash
$ spring run RestApp.groovy
```

Navigate to: <http://localhost:8080/>

---

# [fit] Creating a Spring Boot application with the CLI

---

## Creating a Spring Boot application with the CLI
```bash
spring init --build=maven \
    --groupId=com.imwsoftware \
    --artifactId=sdjug.mongo \
    --description="Spring MongoDB Demo Project" \
    --version=1.0.0-SNAPSHOT \
    --packaging=jar \
    --java-version=1.8 \
    --dependencies=web,actuator,actuator-docs,data-mongodb \
    --boot-version=1.5.4.RELEASE \
    ~/sdjug-demo
```

---


#### Build via Maven
mvn package - compiles code and packages it to a Jar file  

- the spring-boot-maven-plugin repackages the jar into a single executable jar

```xml
  <build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

---

```
More info on Spring Boot Maven Plugin

$ mvn help:describe -Dplugin=spring-boot
This plugin has 6 goals:

spring-boot:build-info
  Description: Generate a build-info.properties file based the content of the
    current MavenProject.

spring-boot:help
  Description: Display help information on spring-boot-maven-plugin.
    Call mvn spring-boot:help -Ddetail=true -Dgoal=<goal-name> to display
    parameter details.

spring-boot:repackage
  Description: Repackages existing JAR and WAR archives so that they can be
    executed from the command line using java -jar. With layout=NONE can also
    be used simply to package a JAR with nested dependencies (and no main
    class, so not executable).

spring-boot:run
  Description: Run an executable archive application.

spring-boot:start
  Description: Start a spring application. Contrary to the run goal, this
    does not block and allows other goal to operate on the application. This
    goal is typically used in integration test scenario where the application
    is started before a test suite and stopped after.

spring-boot:stop
  Description: Stop a spring application that has been started by the 'start'
    goal. Typically invoked once a test suite has completed.
```

---

## Repackage
```
mvn package spring-boot:repackage
```
Add exec to configuration to create a slim jar (if used in other pojects as a dependency) and an exec jar
```
<configuration>
	<classifier>exec</classifier>
</configuration>
```

---


## build-info
```
$ mvn spring-boot:build-info
```
Output: target/classes/META-INF/build-info.properties

```
#Properties
#Sat Jul 15 20:46:33 PDT 2017
build.time=2017-07-15T20\:46\:33-0700
build.artifact=sdjug.mongo
build.group=com.imwsoftware
build.name=z.sdjug.mongo
build.version=1.0.0-SNAPSHOT
```

---

## build-info
This can be added to the current pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>1.5.4.RELEASE</version>
            <executions>
                <execution>
                    <goals>
                        <goal>build-info</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---


## Run via command line
```
mvn spring-boot:run
```

---

# [fit] The End
