package com.gtcafe.asimov.platform.hello.rest;

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

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @Autowired
    // private ObjectMapper objectMapper;  // 用於解析 JSON

    private static final String TEST_DATA_PATH = "src/test/resources/test-data/hello/sync";
    private static final String API_URI = "/api/hello";


    @Test
    void testHelloPost() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-sync.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
                )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Hello, Asimov"))  // 驗證靜態部分
            .andExpect(jsonPath("$.launchTime", notNullValue()))      // 驗證時間部分存在
            .andExpect(jsonPath("$.launchTime", containsString("2024"))); // 檢查時間大致正確
            // .andExpect(content().string(expectedPayload));  // 驗證回應的內容
    }

    @Test
    void testHelloPost_AbnormalCase1() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-abnormal-case1.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
            )
            .andExpect(
                status().isBadRequest()
            );
    }

    @Test
    void testHelloPost_AbnormalCase2() throws Exception {
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-abnormal-case2.json"), StandardCharsets.UTF_8);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(API_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
            )
            .andExpect(
                status().isBadRequest()
            );
    }


}
