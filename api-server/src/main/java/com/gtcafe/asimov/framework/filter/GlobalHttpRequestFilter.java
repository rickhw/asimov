package com.gtcafe.asimov.framework.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gtcafe.asimov.framework.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.framework.constants.AccessLogConstants;
import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;
import com.gtcafe.asimov.framework.context.HttpRequestContext;

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

        // log.info("doFilter::before");
        // System.out.println("doFilter::before");

        handleRequestId(req, res);
        handleRequestProtocol(req);
        handleRequestClientIp(req);

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

        // System.out.println("doFilter::after");
        // log.info("doFilter::after");
    }

    private void handleRequestId(HttpServletRequest req, HttpServletResponse res) {
        String requestId = req.getHeader(HttpHeaderConstants.X_REQUEST_ID);
        if (requestId == null || requestId.isEmpty()) {
            requestId = httpRequestContextBean.getRequestId().getRequestId();
        }
        res.setHeader(HttpHeaderConstants.X_REQUEST_ID, requestId);

        MDC.put(HttpHeaderConstants.X_REQUEST_ID, requestId);
        MDC.put(AccessLogConstants.REQUEST_URI, req.getRequestURI());
        MDC.put(AccessLogConstants.METHOD, req.getMethod());

    }

    private void handleRequestProtocol(HttpServletRequest req) {
        String proto = "";

        String scheme = req.getScheme();
        String xForwardedProto = req.getHeader("x-forwarded-proto");
        String cloudfrontForwardedProto = req.getHeader("cloudfront-forwarded-proto");
        
        log.debug("req.getScheme(): [{}], x-forwarded-proto: [{}], cloudfront-forwarded-proto: [{}]", scheme, xForwardedProto, cloudfrontForwardedProto);

        if (scheme != null && !"".equals(scheme)) {
            proto = scheme;
        } else if (xForwardedProto != null && !"".equals(xForwardedProto)) {
            proto = xForwardedProto;
        } else if (cloudfrontForwardedProto != null && !"".equals(cloudfrontForwardedProto)) {
            proto = cloudfrontForwardedProto;
        } else {
            proto = "http";
        }

        MDC.put(AccessLogConstants.PROTOCOL, proto);
    }

    private void handleRequestClientIp(HttpServletRequest req) {
        String clientIp = null;

        for (String header : HttpHeaderConstants.HEADERS_TO_TRY) {
            clientIp = req.getHeader(header);
            log.debug("{}: [{}]", header, clientIp);
            
            if (clientIp != null && clientIp.length() != 0 && !"unknown".equalsIgnoreCase(clientIp)) {
                break;
            }
        }

        if (clientIp == null || clientIp.isEmpty()) {
            log.debug("req.getRemoteAddr(): [{}]", req.getRemoteAddr());
            clientIp = req.getRemoteAddr();
        }
            
        MDC.put(AccessLogConstants.CLIENT_IP, clientIp);
    }
}