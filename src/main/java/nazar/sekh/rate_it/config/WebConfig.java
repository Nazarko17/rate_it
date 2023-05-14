package nazar.sekh.rate_it.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathToFolder = System.getProperty("user.home") + File.separator + "avatars" + File.separator;
        registry.addResourceHandler("/ava/**").addResourceLocations("file:///" + pathToFolder);

        String pathToFolder2 = System.getProperty("user.home") + File.separator + "images" + File.separator;
        registry.addResourceHandler("/images/**").addResourceLocations("file:///" + pathToFolder2);
    }
}
