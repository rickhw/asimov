package com.gtcafe.asimov.apiserver.platform.tenant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // // Utility method to read file content
    // private String readJsonFromFile(String filePath) throws Exception {
    //     return new String(Files.readAllBytes(Paths.get(filePath)));
    // }

    // @Test
    // void shouldApplyNewTenant() throws Exception {
    //     // Read request data from file
    //     String requestBody = readJsonFromFile("src/test/resources/test-data/tenant-request.json");
    //     // Read expected response data from file
    //     String expectedResponse = readJsonFromFile("src/test/resources/expected-data/tenant-response-accepted.json");

    //     mockMvc.perform(post("/api/tenants")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(requestBody))
    //             .andExpect(status().isAccepted())
    //             .andExpect(content().json(expectedResponse));
    // }
}
