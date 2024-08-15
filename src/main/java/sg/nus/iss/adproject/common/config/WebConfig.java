package sg.nus.iss.adproject.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sg.nus.iss.adproject.common.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/create")
                .excludePathPatterns("/api/stocks/**")
                .excludePathPatterns("/api/recommendations/**")
                .excludePathPatterns("/api/learn/terminology")
                .excludePathPatterns("/api/mobile/learn/**")
                .excludePathPatterns("/api/web/**")
                .excludePathPatterns("/files/**")
                .excludePathPatterns("/static/**");
    }
}
