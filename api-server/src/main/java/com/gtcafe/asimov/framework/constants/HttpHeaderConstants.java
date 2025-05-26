package com.gtcafe.asimov.framework.constants;

public class HttpHeaderConstants {
    // Tenant Context
    public static final String X_TENANT_ID = "X-Tenant-Id";
    public static final String X_APP_NAME = "X-AppName";
    public static final String X_ROLE_NAME = "X-RoleName";
    
    // Request: Async Protocol
    public final static String X_REQUEST_MODE = "X-Request-Mode";   // async, sync

    public final static String V__ASYNC_MODE = "async";
    public final static String V__SYNC_MODE = "sync";

    public final static String X_CALLBACK_URL = "X-Callback-Url";   //
    public final static String X_CALLBACK_TOKEN = "X-Callback-Token";   //

    public final static String X_DATA_SIGNATURE = "X-Data-Signature";
    // public final static String X_AUTH_TOKEN = "X-Auth-Token";
    // public final static String X_DATA_SIGNATURE = "X-Data-Signature";

    public static final String[] HEADERS_TO_TRY = {
        "X-Forwarded-For",
        "X-Real-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR" 
    };


    // HTTP
    public final static String X_REQUEST_ID = "X-Request-Id";

    public final static String CLIENT_IP = "ClientIP";
    public final static String REQUEST_URI = "RequestURI";
    public final static String PROTOCOL = "Protocol";
    public final static String METHOD = "Method";


    // Response
}
