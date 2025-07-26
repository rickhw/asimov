package com.gtcafe.asimov.system.hello.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.rest.domain.hello.HelloController;
import com.gtcafe.asimov.system.hello.HelloMapper;
import com.gtcafe.asimov.system.hello.domain.HelloService;
import com.gtcafe.asimov.system.hello.model.Hello;

/**
 * HelloController 單元測試
 * 測試 Hello API 的各種情境
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService helloService;

    @MockBean
    private HelloMapper helloMapper;

    @MockBean
    private TimeUtils timeUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sayHelloSync_ShouldReturnCorrectResponse() throws Exception {
        // Arrange
        String expectedTime = "2024-12-17T13:04:34.371Z";
        Hello mockHello = new Hello();
        mockHello.setMessage("Hello, World!");

        // 模擬 HelloService 和 TimeUtils 的行為
        when(helloService.sayHelloSync()).thenReturn(mockHello);
        when(timeUtils.currentTimeIso8601()).thenReturn(expectedTime);

        // Act & Assert
        mockMvc.perform(get("/api/v1alpha/hello")
                .header("X-Tenant-Id", "t-123")
                .header("X-AppName", "asimov")
                .header("X-RoleName", "admin")
                .accept(MediaType.APPLICATION_JSON))
                // 驗證 HTTP Method 和 Header
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Type"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                
                // 驗證 JSON payload 正確性
                .andExpect(jsonPath("$.message.message", is("Hello, World!")))
                .andExpect(jsonPath("$.launchTime", is(expectedTime)));
    }
}