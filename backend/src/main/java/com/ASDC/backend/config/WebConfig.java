package com.ASDC.backend.config;

import com.ASDC.backend.filter.Interceptor;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Interceptor interceptor;

    @Value("${api.include.endpoints.file}")
    private String includeEndpointsFile;

    @Value("${api.exclude.endpoints.file}")
    private String excludeEndpointsFile;

    @PostConstruct
    public void init() throws IOException {
        String includePathsString = readApiEndpoints(includeEndpointsFile);
        String excludePathsString = readApiEndpoints(excludeEndpointsFile);

        System.setProperty("interceptor.include.paths", includePathsString);
        System.setProperty("interceptor.exclude.paths", excludePathsString);
    }

    private String readApiEndpoints(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            List<String> endpoints = reader.lines().collect(Collectors.toList());
            return String.join(",", endpoints);
        }
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "DELETE", "POST", "GET");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String includePaths = System.getProperty("interceptor.include.paths");
        String excludePaths = System.getProperty("interceptor.exclude.paths");

        String[] includePathsArray = includePaths != null ? includePaths.split(",") : new String[]{};
        String[] excludePathsArray = excludePaths != null ? excludePaths.split(",") : new String[]{};

        registry.addInterceptor(interceptor)
                .addPathPatterns(includePathsArray)
                .excludePathPatterns(excludePathsArray);
    }
}