package cn.com.ctsi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// // 允许跨域的域名
                .allowedOriginPatterns(CorsConfiguration.ALL)// 允许所有域
                .allowedHeaders(CorsConfiguration.ALL) // 允许任何请求头
                .allowCredentials(true)//// 允许证书、cookie
                .allowedMethods(RequestMethod.GET.name(), RequestMethod.HEAD.name(), RequestMethod.POST.name()
                        , RequestMethod.PUT.name(), RequestMethod.PATCH.name(), RequestMethod.DELETE.name()
                        , RequestMethod.OPTIONS.name(), RequestMethod.TRACE.name())// 允许方法（post、get等）
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);//maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
    }

}
