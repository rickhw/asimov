package com.gtcafe.asimov.apiserver.platform.hello;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String TEST_DATA_PATH = "src/test/resources/test-data/hello";

    // Utility method to read file content
    private String readJsonFromFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    void testHelloPost() throws Exception {
        // 讀取 request-payload.json
        String requestPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/hello-post-sync.json"), StandardCharsets.UTF_8);

        // 讀取 expected-payload.json
        String expectedPayload = Files.readString(Paths.get(TEST_DATA_PATH + "/expected-payload.json"), StandardCharsets.UTF_8).trim();

        // 模擬 POST 請求，並比較返回的內容是否符合預期
        mockMvc.perform(MockMvcRequestBuilders.post("/api/hello")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));  // 驗證回應的內容
    }


    // @Test
    // void callHelloSync() throws Exception {
    //     // Read expected response data from file
    //     String expectedResponse = readJsonFromFile(EXPECTED_PATH + "/hello-get-sync.json");

    //     // Perform GET request for the running task
    //     mockMvc.perform(get("/api/hello", "uuid-1234-5678-91011"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().json(expectedResponse));
    // }

    // @Test
    // void shouldGetTaskStatusCompleted() throws Exception {
    //     // Read expected response data from file
    //     String expectedResponse = readJsonFromFile(EXPECTED_PATH + "/task-response-completed.json");

    //     // Perform GET request for the completed task
    //     mockMvc.perform(get("/api/tasks/{taskId}", "uuid-1234-5678-91011"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().json(expectedResponse));
    // }
}
