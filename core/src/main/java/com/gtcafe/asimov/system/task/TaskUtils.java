package com.gtcafe.asimov.system.task;

import com.gtcafe.asimov.framework.constants.KindConstants;

public class TaskUtils {

    // @Value("${asimov.task.thread-pool.size}")
    // private String openapiTitle;

    public static String renderCacheKey(String id) {
        // "sys.Task:823fcf9a-4900-4898-b458-c10cc280d536"
        return String.format("%s:%s", KindConstants.SYS_TASK, id);
    }
    
}
