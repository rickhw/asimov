package com.gtcafe.asimov.apiserver.system.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gtcafe.asimov.apiserver.system.context.TenantContext;
import com.gtcafe.asimov.core.system.bean.RequestIdComponent;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenantContextInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String APP_NAME_HEADER = "X-AppName";
    private static final String ROLE_NAME_HEADER = "X-RoleName";
    private static final String REQUEST_ID = "X-Request-Id";

    private final RequestIdComponent requestIdComponent;

    public TenantContextInterceptor(RequestIdComponent requestIdComponent) {
        this.requestIdComponent = requestIdComponent;
        log.info("TenantContextInterceptor initialized");
    }

    @Override
    public boolean preHandle( 
        @SuppressWarnings("null") HttpServletRequest request,  
        @SuppressWarnings("null") HttpServletResponse response,  
        @SuppressWarnings("null") Object handler
    ) {

        // // 如果是 POST /api/tenants，直接放行
        // if ("POST".equals(request.getMethod()) && "/api/tenants".equals(request.getRequestURI())) {
        //     return true;
        // }

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String appName = request.getHeader(APP_NAME_HEADER);
        String roleName = request.getHeader(ROLE_NAME_HEADER);
        String requestId = request.getHeader(REQUEST_ID);

        if (!StringUtils.hasLength(requestId)) {
            requestId = requestIdComponent.getRequestId().getRequestId();
        }

        if (!StringUtils.hasLength(tenantId)) {
            throw new IllegalArgumentException("TenantId is required");
        }

        TenantContext context = TenantContext.of(tenantId, appName, roleName, requestId);
        TenantContext.setCurrentContext(context);

        MDC.put(TENANT_ID_HEADER, tenantId);
        MDC.put(APP_NAME_HEADER, appName);
        MDC.put(ROLE_NAME_HEADER, roleName);
        MDC.put(REQUEST_ID, requestId);

        return true;
    }

    @Override
    public void afterCompletion(
        @SuppressWarnings("null") HttpServletRequest request, 
        @SuppressWarnings("null") HttpServletResponse response, 
        @SuppressWarnings("null") Object handler, 
        @SuppressWarnings("null") Exception ex
    ) {
        TenantContext.clear();
    }
}