package sg.nus.iss.adproject.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置 Cross-Domain Configuration
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1 设置访问源地址
        // 1 Setting the access source address
        corsConfiguration.addAllowedOrigin("*");
        // 2 设置访问源请求头
        // 2 Setting the access source request header
        corsConfiguration.addAllowedHeader("*");
        // 3 设置访问源请求方法
        // 3 Setting up the access source request method
        corsConfiguration.addAllowedMethod("*");
        // 4 对接口配置跨域设置
        // 4 Configure cross-domain settings for the interface
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
