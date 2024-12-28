package com.gtcafe.asimov.conifg;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gtcafe.asimov.system.interceptor.HttpRequestContextInterceptor;
import com.gtcafe.asimov.system.interceptor.TenantContextInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TenantContextInterceptor tenantContextInterceptor;
    private final HttpRequestContextInterceptor httpRequestContextInterceptor;

    public WebConfig(TenantContextInterceptor tenantContextInterceptor, HttpRequestContextInterceptor httpRequestContextInterceptor) {
        this.tenantContextInterceptor = tenantContextInterceptor;
        this.httpRequestContextInterceptor = httpRequestContextInterceptor;
    }

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        // 需要產生 TenantContextInterceptor 的 Bean
        registry.addInterceptor(tenantContextInterceptor)
                
                // 底下是不需要 API Key 的 URI
                // 1. operation
                .excludePathPatterns("/health", "/metrics", "/favicon.ico")
                // 2. metadata, tenant context
                .excludePathPatterns("/apimeta", "/version", "/tenant-context")
                // 3. swagger
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**")
                // 4. 開放申請, 不需要 API Key. @TODO: 要處理 POST /api/tenants
                .excludePathPatterns("/api/v1alpha/tenants/**")
                .excludePathPatterns("/api/v1alpha/regions/**")
                .addPathPatterns("/**")  // 攔截所有請求
            ;

        // 需要產生 HttpRequestContextInterceptor 的 Bean
        registry.addInterceptor(httpRequestContextInterceptor)
                .addPathPatterns("/**");

    }
}