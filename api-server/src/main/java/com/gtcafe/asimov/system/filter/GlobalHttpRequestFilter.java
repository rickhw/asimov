package com.gtcafe.asimov.system.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gtcafe.asimov.system.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.system.constants.HttpHeaderConstants;

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
        log.debug("MdcHttpRequestFilter created");
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
        // handleRequestClientIp(req, res);
        // handleRequestProtocol(req, res);
        // handleRequestMethod(req, res);
        // handleRequestUri(req, res);

        chain.doFilter(request, response);

        // TODO, handle response
    }

    private void handleRequestId(HttpServletRequest req, HttpServletResponse res) {
        String requestId = req.getHeader(HttpHeaderConstants.X_REQUEST_ID);
        if (requestId == null || requestId.isEmpty()) {
            requestId = httpRequestContextBean.getRequestId().getRequestId();
        }
        res.setHeader(HttpHeaderConstants.X_REQUEST_ID, requestId);

        MDC.put(HttpHeaderConstants.X_REQUEST_ID, requestId);
    }

    // TODO: X-Forwarded-For
    // TODO: ClientIps
    // X-Real-IP
    // private void handleRequestClientIp(HttpServletRequest req, HttpServletResponse res) {
    //     String clientIp = null;

    //     for (String header : HEADERS_TO_TRY) {
    //         clientIp = req.getHeader(header);
    //         log.debug("{}: [{}]", header, clientIp);
            
    //         if (clientIp != null && clientIp.length() != 0 && !"unknown".equalsIgnoreCase(clientIp)) {
    //             break;
    //         }
    //     }

    //     if (clientIp == null || clientIp.isEmpty()) {
    //         log.debug("req.getRemoteAddr(): [{}]", req.getRemoteAddr());
    //         clientIp = req.getRemoteAddr();
    //     }
            
    //     MDC.put(HttpHeaderConstants.CLIENT_IP, clientIp);
    // }

    // private void handleRequestProtocol(HttpServletRequest req, HttpServletResponse res) {
    //     String proto = "";

    //     String scheme = req.getScheme();
    //     String xForwardedProto = req.getHeader("x-forwarded-proto");
    //     String cloudfrontForwardedProto = req.getHeader("cloudfront-forwarded-proto");
        
    //     log.debug("req.getScheme(): [{}], x-forwarded-proto: [{}], cloudfront-forwarded-proto: [{}]", scheme, xForwardedProto, cloudfrontForwardedProto);

    //     if (scheme != null && !"".equals(scheme)) {
    //         proto = scheme;
    //     } else if (xForwardedProto != null && !"".equals(xForwardedProto)) {
    //         proto = xForwardedProto;
    //     } else if (cloudfrontForwardedProto != null && !"".equals(cloudfrontForwardedProto)) {
    //         proto = cloudfrontForwardedProto;
    //     } else {
    //         proto = "http";
    //     }

    //     MDC.put(HttpHeaderConstants.PROTOCOL, proto);
    // }

    // private void handleRequestMethod(HttpServletRequest req, HttpServletResponse res) {
    //     String method = req.getMethod();

    //     MDC.put(HttpHeaderConstants.METHOD, method);
    // }

    // private void handleRequestUri(HttpServletRequest req, HttpServletResponse res) {
    //     MDC.put(HttpHeaderConstants.REQUEST_URI, req.getRequestURI());
    // }


}