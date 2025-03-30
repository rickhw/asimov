package com.gtcafe.asimov.framework.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;
import com.gtcafe.asimov.framework.context.TenantContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenantContextInterceptor implements HandlerInterceptor {

    public TenantContextInterceptor() {
        log.info("TenantContextInterceptor initialized");
    }

    @Override
    public boolean preHandle( 
        @SuppressWarnings("null") HttpServletRequest request,  
        @SuppressWarnings("null") HttpServletResponse response,  
        @SuppressWarnings("null") Object handler
    ) {
        
        // String uri = request.getRequestURI();
        // log.debug("uri: {}", uri);

        // if (uri.startsWith("/health") || uri.startsWith("/metrics") 
        //     || uri.equals("/")
        //     || uri.equals("/version")
        // ) {
        //     return true;
        // }

        String tenantId = request.getHeader(HttpHeaderConstants.X_TENANT_ID);
        String appName = request.getHeader(HttpHeaderConstants.X_APP_NAME);
        String roleName = request.getHeader(HttpHeaderConstants.X_ROLE_NAME);
        // String requestId = request.getHeader(TenantContext.X_REQUEST_ID);

        // if (!StringUtils.hasLength(requestId)) {
        //     requestId = httpRequestBean.getRequestId().getRequestId();
        // }

        // @TODO
        // if (!StringUtils.hasLength(tenantId)) {
        //     throw new IllegalArgumentException("TenantId is required");
        // }

        TenantContext context = TenantContext.of(tenantId, appName, roleName);
        TenantContext.SetCurrentContext(context);

        MDC.put(HttpHeaderConstants.X_TENANT_ID, tenantId);
        MDC.put(HttpHeaderConstants.X_APP_NAME, appName);
        MDC.put(HttpHeaderConstants.X_ROLE_NAME, roleName);

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