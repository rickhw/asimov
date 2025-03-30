package com.gtcafe.asimov.platform.hello.rest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gtcafe.asimov.system.hello.rest.HelloController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    // @Autowired
    // private MockMvc mockMvc;

    // @MockBean
    // private HelloService helloService;

    // @MockBean
    // private HelloMapper helloMapper;

    // @MockBean
    // private TimeUtils timeUtils;

    // @Autowired
    // private ObjectMapper objectMapper;

    // @Test
    // void sayHelloSync_ShouldReturnCorrectResponse() throws Exception {
    //     // Arrange
    //     String expectedTime = "2024-12-17T13:04:34.371Z";
    //     Hello mockHello = new Hello(); //Hello.builder().build();
    //     mockHello.setMessage("Hello, World!");

    //     // 模擬 HelloService 和 TimeUtils 的行為
    //     when(helloService.sayHelloSync()).thenReturn(mockHello);
    //     when(timeUtils.currentTimeIso8601()).thenReturn(expectedTime);

    //     // Act & Assert
    //     mockMvc.perform(get("/api/v1alpha/hello")
    //             .header("X-Tenant-Id", "t-123")
    //             .header("X-AppName", "asimov")
    //             .header("X-RoleName", "admin")
    //             .accept(MediaType.APPLICATION_JSON))
    //             // 驗證 HTTP Method 和 Header
    //             .andExpect(status().isOk())
    //             .andExpect(header().exists("Content-Type"))
    //             .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
    //             .andExpect(header().exists("X-Request-Id")) // 假設 X-Request-Id 是自動生成的

    //             // 驗證 JSON payload 正確性
    //             .andExpect(jsonPath("$.message.message", is("Hello, World!")))
    //             .andExpect(jsonPath("$.launchTime", is(expectedTime)));
    // }
}
