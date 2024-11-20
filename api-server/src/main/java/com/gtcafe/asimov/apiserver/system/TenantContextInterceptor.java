package com.gtcafe.asimov.apiserver.system;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantContextInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "tenantId";
    private static final String APP_NAME_HEADER = "appName";
    private static final String ROLE_NAME_HEADER = "roleName";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String appName = request.getHeader(APP_NAME_HEADER);
        String roleName = request.getHeader(ROLE_NAME_HEADER);

        // 可以在這裡加入驗證邏輯，確保必要的 header 都存在
        if (StringUtils.isEmpty(tenantId)) {
            throw new IllegalArgumentException("TenantId is required");
        }

        TenantContext context = TenantContext.of(tenantId, appName, roleName);

        TenantContext.setCurrentContext(context);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}