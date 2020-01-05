package ru.func.weathersender.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    private static final int ONE_YEAR = 365 * 24 * 60 * 60;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/")
                .setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/assets/favicon.ico")
                .setCachePeriod(ONE_YEAR);
    }
}
