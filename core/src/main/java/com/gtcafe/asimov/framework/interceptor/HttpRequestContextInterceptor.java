package com.gtcafe.asimov.framework.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gtcafe.asimov.framework.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;
import com.gtcafe.asimov.framework.context.ApiMetadataContext;
import com.gtcafe.asimov.framework.context.HttpRequestContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpRequestContextInterceptor implements HandlerInterceptor {

    private final HttpRequestContextBean httpRequestBean;

    public HttpRequestContextInterceptor(HttpRequestContextBean httpRequestBean) {
        this.httpRequestBean = httpRequestBean;
    }

    @Override
    public boolean preHandle( 
        @SuppressWarnings("null") HttpServletRequest request,  
        @SuppressWarnings("null") HttpServletResponse response,  
        @SuppressWarnings("null") Object handler
    ) {

        handleHttpRequestContext(request);
        heandleApiMetadataContext(request);
        // handleRequestProtocol(request);
        // handleRequestClientIp(request);

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
        ApiMetadataContext.Clear();
    }

    private void handleHttpRequestContext(HttpServletRequest request) {
        String requestId = request.getHeader(HttpHeaderConstants.X_REQUEST_ID);

        if (!StringUtils.hasLength(requestId)) {
            requestId = httpRequestBean.getRequestId().getRequestId();
        }

        HttpRequestContext context = HttpRequestContext.of(requestId);
        HttpRequestContext.SetCurrentContext(context);

        // MDC.put(HttpHeaderConstants.X_REQUEST_ID, requestId);
        // MDC.put(AccessLogConstants.REQUEST_URI, request.getRequestURI());
        // MDC.put(AccessLogConstants.METHOD, request.getMethod());

    }

    private void heandleApiMetadataContext(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        log.debug("method: [{}], uri: [{}]", method, uri);

        ApiMetadataContext context = ApiMetadataContext.of(method, uri);
        ApiMetadataContext.SetCurrentContext(context);
    }

}