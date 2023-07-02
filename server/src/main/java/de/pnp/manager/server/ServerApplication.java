package de.pnp.manager.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("200.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**String workingDirectory = System.getProperty("user.dir");
         Path currentRelativePath = Path.of("").resolve("server").resolve("src").resolve("main").resolve("resources").resolve("static");
         // String currentRelativePath = "ws://localhost:5173/";

         registry.addResourceHandler("/resources/**").addResourceLocations(currentRelativePath.toString());
         registry.addResourceHandler("{filename:\\w+\\.css}").addResourceLocations(currentRelativePath.toString());
         registry.addResourceHandler("{filename:\\w+\\.js}").addResourceLocations(currentRelativePath.toString());
         //String target =  +  + "/server/src/main/resources/static/";
         */
    }

    @Configuration
    public class WebConfiguration implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("*");
        }
    }

}