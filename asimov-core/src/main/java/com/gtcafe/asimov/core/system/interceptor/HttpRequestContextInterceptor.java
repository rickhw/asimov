package com.gtcafe.asimov.core.system.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gtcafe.asimov.core.system.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.core.system.constants.HttpHeaderConstants;
import com.gtcafe.asimov.core.system.context.HttpRequestContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpRequestContextInterceptor implements HandlerInterceptor {

    private final HttpRequestContextBean httpRequestBean;

    public HttpRequestContextInterceptor(HttpRequestContextBean httpRequestBean) {
        this.httpRequestBean = httpRequestBean;
        log.info("HttpRequestContextInterceptor initialized");
    }

    @Override
    public boolean preHandle( 
        @SuppressWarnings("null") HttpServletRequest request,  
        @SuppressWarnings("null") HttpServletResponse response,  
        @SuppressWarnings("null") Object handler
    ) {
        String requestId = request.getHeader(HttpHeaderConstants.X_REQUEST_ID);

        if (!StringUtils.hasLength(requestId)) {
            requestId = httpRequestBean.getRequestId().getRequestId();
        }

        HttpRequestContext context = HttpRequestContext.of(requestId);
        HttpRequestContext.SetCurrentContext(context);

        MDC.put(HttpHeaderConstants.X_REQUEST_ID, requestId);

        return true;
    }

    @Override
    public void afterCompletion(
        @SuppressWarnings("null") HttpServletRequest request, 
        @SuppressWarnings("null") HttpServletResponse response, 
        @SuppressWarnings("null") Object handler, 
        @SuppressWarnings("null") Exception ex
    ) {
        HttpRequestContext.Clear();
    }
}