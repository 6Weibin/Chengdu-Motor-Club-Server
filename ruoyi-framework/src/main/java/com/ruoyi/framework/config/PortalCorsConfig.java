package com.ruoyi.framework.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 门户公开接口跨域配置
 *
 * @author AI.Coding
 */
@Configuration
public class PortalCorsConfig
{
    /**
     * 注册门户公开接口的跨域过滤器。
     * <p>
     * 修复点：`portalApi` 请求会先经过 Servlet Filter / Shiro 过滤链，
     * 仅在 MVC 层配置 CORS 可能无法给最终响应补上跨域头，因此这里前置注册过滤器统一处理。
     * </p>
     *
     * @return 跨域过滤器注册对象
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> portalCorsFilterRegistration()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        // 门户 H5 当前通过 127.0.0.1:4173 访问预览服务，这里显式放开联调来源。
        configuration.addAllowedOriginPattern("http://127.0.0.1:4173");
        configuration.addAllowedOriginPattern("http://localhost:4173");
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/portalApi/**", configuration);

        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        registrationBean.setName("portalCorsFilter");
        registrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
