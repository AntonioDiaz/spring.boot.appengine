# Spring boot running on Google App Engine
## Links
https://spring.io/guides/gs/intellij-idea/

https://spring.io/guides/gs/spring-boot/

https://medium.com/google-cloud/getting-started-with-google-app-engine-and-spring-boot-in-5-steps-2d0f8165c89

## Create project
1. Download project: https://start.spring.io/  
Select: dependences > web, java version > 8.  
![start spring](https://antoniodiaz.github.io/images/spring_boot/start_springboot.jpg)
1. Run local server: tomcat  
   ```bash
   mvn spring-boot:run
   ```
1. Make app compatible with App Engine. Update **pom.xml**.   
   * Comment tomcat started.
       ```xml
       <!-- Remove spring-boot-starter-tomcat dependency 
       <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-tomcat</artifactId>
          <scope>provided</scope>
       </dependency>
       -->
       ```
   * Add servlet-api
       ```xml
       <!-- add following dependency under dependencies section -->
       <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>javax.servlet-api</artifactId>
          <version>3.1.0</version>
          <scope>provided</scope>
       </dependency>
       ```
   * Add appengine plugin
       ```xml
       <plugin>
          <groupId>com.google.cloud.tools</groupId>
          <artifactId>appengine-maven-plugin</artifactId>
          <version>1.3.2</version>
       </plugin>
       ```
1. Run local server: jetty
   ```bash
   $ mvn appengine:run
   ```
1. Import to Intellij. Google Cloud plugin needed.

## Persistence: Objectify

## Thymeleaf

## Security

## Testing





