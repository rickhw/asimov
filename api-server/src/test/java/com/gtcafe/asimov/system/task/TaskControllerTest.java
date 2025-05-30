package com.gtcafe.asimov.system.task;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Utility method to read file content
    private String readJsonFromFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // @Test
    // void shouldGetTaskStatusRunning() throws Exception {
    //     // Read expected response data from file
    //     String expectedResponse = readJsonFromFile(EXPECTED_PATH + "/task-response-running.json");

    //     // Perform GET request for the running task
    //     mockMvc.perform(get("/api/tasks/{taskId}", "uuid-1234-5678-91011"))
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
