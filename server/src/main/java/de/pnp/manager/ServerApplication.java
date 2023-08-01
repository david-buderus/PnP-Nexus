package de.pnp.manager;

import de.pnp.manager.server.EJvmFlag;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main entrypoint to PnP-Nexus.
 */
@SpringBootApplication
public class ServerApplication implements WebMvcConfigurer {

    /**
     * Starts the Server.
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("200.html");
    }

    /**
     * Configuration of the server.
     */
    @Configuration
    public static class WebConfiguration implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(@NotNull CorsRegistry registry) {
            if (EJvmFlag.DEV_MODE.isEnabled()) {
                registry.addMapping("/**").allowedMethods("*");
            }
        }
    }
}