package nk.service.NStory.config;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.interceptor.GlobalInterceptor;
import nk.service.NStory.service.impl.BoardInfoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final BoardInfoService boardInfoService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new GlobalInterceptor(boardInfoService)).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
