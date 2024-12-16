package com.gtcafe.asimov.core.system.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gtcafe.asimov.core.system.context.ApiMetadataContext;
import com.gtcafe.asimov.core.system.context.TenantContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiMetadataContextInterceptor implements HandlerInterceptor {

    public ApiMetadataContextInterceptor() {
        log.info("ApiMetadataContextInterceptor initialized");
    }

    @Override
    public boolean preHandle( 
        @SuppressWarnings("null") HttpServletRequest request,  
        @SuppressWarnings("null") HttpServletResponse response,  
        @SuppressWarnings("null") Object handler
    ) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info("method: [{}], uri: [{}]", method, uri);

        ApiMetadataContext context = ApiMetadataContext.of(method, uri);
        ApiMetadataContext.SetCurrentContext(context);

        // MDC.put(TenantContext.X_TENANT_ID, tenantId);
        // MDC.put(TenantContext.X_APP_NAME, appName);
        // MDC.put(TenantContext.X_ROLE_NAME, roleName);
        // MDC.put(TenantContext.X_REQUEST_ID, requestId);

        return true;
    }

    @Override
    public void afterCompletion(
        @SuppressWarnings("null") HttpServletRequest request, 
        @SuppressWarnings("null") HttpServletResponse response, 
        @SuppressWarnings("null") Object handler, 
        @SuppressWarnings("null") Exception ex
    ) {
        TenantContext.Clear();
    }
}