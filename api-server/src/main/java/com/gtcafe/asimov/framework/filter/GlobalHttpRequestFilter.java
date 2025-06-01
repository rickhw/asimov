package com.gtcafe.asimov.framework.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.framework.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.framework.constants.AccessLogConstants;
import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class GlobalHttpRequestFilter implements Filter {

    private final HttpRequestContextBean httpRequestContextBean;

    public GlobalHttpRequestFilter(HttpRequestContextBean httpRequestContextBean) {
        this.httpRequestContextBean = httpRequestContextBean;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException 
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        handleRequestId(req, res);
        // handleHttpRequestMetadata(req, res);

        long startTime = System.currentTimeMillis(); // 記錄開始時間
        MDC.put(AccessLogConstants.START_TIME, String.valueOf(startTime));


        try {
            chain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis(); // 記錄結束時間
            long duration = endTime - startTime;
            
            MDC.put(AccessLogConstants.END_TIME, String.valueOf(endTime));
            MDC.put(AccessLogConstants.DURATION, String.valueOf(duration));

            // Access Log
            log.info("");   
        }
    }

    private void handleRequestId(HttpServletRequest req, HttpServletResponse res) {
        String requestId = req.getHeader(HttpHeaderConstants.X_REQUEST_ID);
        if (requestId == null || requestId.isEmpty()) {
            requestId = httpRequestContextBean.getRequestId().getRequestId();
        }
        res.setHeader(HttpHeaderConstants.X_REQUEST_ID, requestId);

        MDC.put(HttpHeaderConstants.X_REQUEST_ID, requestId);
    }


}