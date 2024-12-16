package com.gtcafe.asimov.core.system.constants;

public class HttpHeaderConstants {
    // Tenant Context
    public static final String X_TENANT_ID = "X-Tenant-Id";
    public static final String X_APP_NAME = "X-AppName";
    public static final String X_ROLE_NAME = "X-RoleName";
    
    // Request: Async Protocol
    public final static String X_REQUEST_MODE = "X-Request-Mode";   // async, sync

    public final static String ASYNC_MODE = "async";
    public final static String SYNC_MODE = "sync";

    public final static String X_CALLBACK_URL = "X-Callback-Url";   //
    public final static String X_CALLBACK_TOKEN = "X-Callback-Token";   //

    public final static String X_DATA_SIGNATURE = "X-Data-Signature";
    // public final static String X_AUTH_TOKEN = "X-Auth-Token";
    // public final static String X_DATA_SIGNATURE = "X-Data-Signature";

    // HTTP
    public final static String X_REQUEST_ID = "X-Request-Id";

    public final static String CLIENT_IP = "ClientIP";
    public final static String REQUEST_URI = "RequestURI";
    public final static String PROTOCOL = "Protocol";
    public final static String METHOD = "Method";


    // Response
}
