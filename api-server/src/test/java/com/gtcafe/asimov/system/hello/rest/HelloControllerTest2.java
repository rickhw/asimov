package com.gtcafe.asimov.system.hello.rest;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * HelloController 整合測試
 * 使用真實的測試資料檔案進行端到端測試
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    private static final String TEST_DATA_PATH = "src/test/resources/test-data/hello/sync";
    private static final String API_URI = "/api/v1alpha/hello";

    @Test
    void testHelloPost() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-sync.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
                    .header("X-Tenant-Id", "t-123")
                    .header("X-AppName", "asimov")
                    .header("X-RoleName", "admin")
                )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))           // 驗證任務 ID 存在
            .andExpect(jsonPath("$.creationTime", notNullValue())) // 驗證建立時間存在
            .andExpect(jsonPath("$.state", notNullValue()))        // 驗證狀態存在
            .andExpect(jsonPath("$.data.message").value("Hello, Asimov")); // 驗證訊息內容
    }

    @Test
    void testHelloPost_AbnormalCase1_EmptyPayload() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-abnormal-case1.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
                    .header("X-Tenant-Id", "t-123")
                    .header("X-AppName", "asimov")
                    .header("X-RoleName", "admin")
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void testHelloPost_AbnormalCase2_InvalidJson() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-abnormal-case2.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
                    .header("X-Tenant-Id", "t-123")
                    .header("X-AppName", "asimov")
                    .header("X-RoleName", "admin")
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void testHelloGet_SyncEndpoint() throws Exception {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(API_URI)
                    .header("X-Tenant-Id", "t-123")
                    .header("X-AppName", "asimov")
                    .header("X-RoleName", "admin")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message.message").value("Hello, World!"))  // 驗證同步回應
            .andExpect(jsonPath("$.launchTime", notNullValue()))              // 驗證時間存在
            .andExpect(jsonPath("$.launchTime", containsString("202")));      // 檢查時間格式
    }
}