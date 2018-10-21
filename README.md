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
Thanks to: https://github.com/takemikami/spring-boot-objectify-sample  
Steps:
1. Add dependende to pom.xml
   ```xml
   <dependency>
      <groupId>com.googlecode.objectify</groupId>
      <artifactId>objectify</artifactId>
      <version>6.0</version>
   </dependency>
   ```
1. Create Entity
   ```java
   import com.googlecode.objectify.annotation.Cache;
   import com.googlecode.objectify.annotation.Entity;
   import com.googlecode.objectify.annotation.Id;

   @Cache(expirationSeconds=600)
   @Entity
   public class Item {

       public Item() { }

       public Item(String id, String name, String description) {
           this.id = id;
           this.name = name;
           this.description = description;
       }

       @Id
       private String id;
       public String getId() {return id;}
       public void setId(String id) {this.id = id;}

       private String name;
       public String getName() {return name;}
       public void setName(String name) {this.name = name;}

       private String description;
       public String getDescription() {return description;}
       public void setDescription(String description) {this.description = description;}
   }
   ```
1. Config Objectify and register entity.
   ```java
   import com.adiaz.springboot.entities.Item;
   import com.google.cloud.datastore.DatastoreOptions;
   import com.googlecode.objectify.ObjectifyFactory;
   import com.googlecode.objectify.ObjectifyFilter;
   import com.googlecode.objectify.ObjectifyService;
   import org.springframework.boot.web.servlet.FilterRegistrationBean;
   import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;

   import javax.servlet.ServletContextEvent;
   import javax.servlet.ServletContextListener;
   import javax.servlet.annotation.WebListener;

   @Configuration
   public class ObjectifyConfig {

       @Bean
       public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
           final FilterRegistrationBean<ObjectifyFilter> registration = new FilterRegistrationBean<>();
           registration.setFilter(new ObjectifyFilter());
           registration.addUrlPatterns("/*");
           registration.setOrder(1);
           return registration;
       }

       @Bean
       public ServletListenerRegistrationBean<ObjectifyListener> listenerRegistrationBean() {
           ServletListenerRegistrationBean<ObjectifyListener> bean =
                   new ServletListenerRegistrationBean<>();
           bean.setListener(new ObjectifyListener());
           return bean;
       }

       @WebListener
       public class ObjectifyListener implements ServletContextListener {

           public ObjectifyListener() {
           }

           @Override
           public void contextInitialized(ServletContextEvent sce) {
               if (System.getenv("SPRING_PROFILES_ACTIVE") == null) {
                   // local without memcache (gradle bootRun)
                   ObjectifyService.init(new ObjectifyFactory(
                           DatastoreOptions.newBuilder()
                                   .setHost("http://localhost:8484")
                                   .setProjectId("my-project")
                                   .build()
                                   .getService()
                   ));
               }  else {
                   // on appengine
                   ObjectifyService.init(new ObjectifyFactory(
                           DatastoreOptions.getDefaultInstance().getService()
                   ));
               }
               ObjectifyService.register(Item.class);
           }

           @Override
           public void contextDestroyed(ServletContextEvent sce) { }
       }
   }
   ```
1. Install and run emulator
   ```bash
   gcloud components install cloud-datastore-emulator
   gcloud beta emulators datastore start --host-port=localhost:8484
   ```
1. Start application
1. Test save and query object
   ```java
   import com.adiaz.springboot.entities.Item;
   import com.googlecode.objectify.ObjectifyService;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;

   @RestController
   @RequestMapping("/")
   public class HelloController {
       @RequestMapping("/put")
       public String put() {
           ObjectifyService.ofy().save().entity(new Item("001", "name", "desc"));
           return "put";
       }

       @RequestMapping("/get")
       public String get() {
           Item e = ObjectifyService.ofy().cache(true).load().type(Item.class).id("001").now();
           return String.format("%s: %s - %s", e.getId(), e.getName(), e.getDescription());
       }
   }
   ```

## Thymeleaf
thanks to: https://www.mkyong.com/spring-boot/spring-boot-hello-world-example-thymeleaf/
1. pom.xml: add dependence.  
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    ```
1. Add template to /src/main/resources/templates/templates.html
    ```html
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Spring Boot Thymeleaf Hello World Example</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <!-- Popper JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    
        <!-- Latest compiled JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-inverse">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="/home">Home</a><br>
                    <a class="navbar-brand" href="/home/put">Put datastore</a><br>
                    <a class="navbar-brand" href="/home/get">Get datastore</a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="#">Home</a></li>
                        <li><a href="#about">About</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container">
            <div class="starter-template">
                <h1>Spring Boot Web Thymeleaf Example</h1>
                <h2>
                    <span th:text="'Message: ' + ${message}"></span>
                </h2>
            </div>
        </div>
    </body>
    </html>
    ```
1. Create css in /src/main/resources/static/main.css
    ```css
    h1{ color:#0000FF; }
    h2 { color:#FF0000; }
    ```
1. Create controller
    ```java
    package com.adiaz.springboot.controllers;
    
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    import java.util.Map;
    
    @Controller
    public class ThymeleafController {
    
        // inject via application.properties
        @Value("${welcome.message:test}")
        private String message = "Hello World";
    
        @RequestMapping("/")
        public String welcome(Map<String, Object> model) {
            model.put("message", this.message);
            return "welcome";
        }
    
    }
    ```
1.Create src/main/resources/application.properties
    ```
    welcome.message: Hello my friend!!!
    ``` 

## Security

## Testing





