# Spring boot running on Google App Engine
## Links
https://spring.io/guides/gs/intellij-idea/

https://spring.io/guides/gs/spring-boot/

https://medium.com/google-cloud/getting-started-with-google-app-engine-and-spring-boot-in-5-steps-2d0f8165c89

## Create project
1. Download project: https://start.spring.io/ <br>   
Select: dependences > web, java version > 8.  
![start spring](https://antoniodiaz.github.io/images/spring_boot/start_springboot.jpg)
2. Run local server: tomcat  
```bash
mvn spring-boot:run
```

3. Make app compatible with App Engine. Update **pom.xml**.   
    * Comment tomcat started.
    * Add servlet-api
    * Add appengine plugin
```xml
<dependencies>
   <!-- Remove spring-boot-starter-tomcat dependency 
  <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-tomcat</artifactId>
       <scope>provided</scope>
  </dependency>
  -->
   <!-- add following dependency under dependencies section -->
   <dependency>
       <groupId>javax.servlet</groupId>
       <artifactId>javax.servlet-api</artifactId>
       <version>3.1.0</version>
       <scope>provided</scope>
   </dependency>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
  </dependency>
</dependencies>
<plugins>
    <plugin>
       <groupId>com.google.cloud.tools</groupId>
       <artifactId>appengine-maven-plugin</artifactId>
       <version>1.3.2</version>
    </plugin>
</plugins>
```
1. Run local server: jetty
```
$ mvn appengine:run
```
1. Import to Intellij. Google Cloud plugin needed.

## Persistence: Objectify

## Thymeleaf

## Security

## Testing





