package com.gtcafe.asimov.system.hello.rest;

import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtcafe.asimov.framework.constants.HttpHeaderConstants;
import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.rest.domain.hello.HelloController;
import com.gtcafe.asimov.rest.domain.hello.request.SayHelloRequest;
import com.gtcafe.asimov.rest.domain.hello.response.HelloTaskResponse;
import com.gtcafe.asimov.system.hello.HelloMapper;
import com.gtcafe.asimov.system.hello.domain.HelloService;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;
import com.gtcafe.asimov.system.task.schema.TaskState;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HelloController.class)
@Import(com.gtcafe.asimov.config.HelloTestConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureMockMvc(addFilters = false)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HelloService helloService;

    @MockBean
    private HelloMapper helloMapper;

    @MockBean
    private TimeUtils timeUtils;

    @Test
    void testSayHelloSync() throws Exception {
        // Arrange
        Hello hello = new Hello();
        hello.setMessage("Hello, World!");
        String expectedTime = "2025-07-27T12:00:00Z";
        when(helloService.sayHelloSync()).thenReturn(hello);
        when(timeUtils.currentTimeIso8601()).thenReturn(expectedTime);

        // Act & Assert
        mockMvc.perform(get("/api/v1alpha/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message.message").value("Hello, World!"))
                .andExpect(jsonPath("$.launchTime").value(expectedTime));
    }

    @Test
    void testSayHelloAsync() throws Exception {
        // Arrange
        SayHelloRequest request = new SayHelloRequest();
        request.setMessage("Hello, Asimov!");
        Hello hello = new Hello();
        hello.setMessage("Hello, Asimov!");
        HelloEvent event = new HelloEvent();
        event.setId(UUID.randomUUID().toString());
        event.setCreationTime(String.valueOf(System.currentTimeMillis()));
        event.setState(TaskState.PENDING);
        event.setData(hello);
        HelloTaskResponse response = new HelloTaskResponse();
        response.setId(event.getId());
        response.setCreationTime(event.getCreationTime());
        response.setState(event.getState());
        response.setData(event.getData());

        when(helloMapper.mapRequestToDomain(any(SayHelloRequest.class))).thenReturn(hello);
        when(helloService.sayHelloAsync(any(Hello.class))).thenReturn(event);
        when(helloMapper.mapHelloEventToResponse(any(HelloEvent.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1alpha/hello")
                .header(HttpHeaderConstants.X_REQUEST_MODE, HttpHeaderConstants.V__ASYNC_MODE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(event.getId().toString()))
                .andExpect(jsonPath("$.state").value(TaskState.PENDING.toString()))
                .andExpect(jsonPath("$.data.message").value("Hello, Asimov!"));
    }

    @Test
    void testSayHelloAsync_InvalidRequest() throws Exception {
        // Arrange
        SayHelloRequest request = new SayHelloRequest();
        request.setMessage(""); // Invalid empty message

        // Act & Assert
        mockMvc.perform(post("/api/v1alpha/hello")
                .header(HttpHeaderConstants.X_REQUEST_MODE, HttpHeaderConstants.V__ASYNC_MODE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}