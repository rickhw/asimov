// package com.gtcafe.asimov.platform.hello.domain;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.gtcafe.asimov.core.system.utils.TimeUtils;

// import lombok.Getter;
// import lombok.Setter;

// public class Hello {

//     @Getter
//     @Setter
//     private String message;

//     @Getter
//     @Setter
//     private String timestamp;

//     @Autowired
//     private TimeUtils timeUtils;

//     public Hello(String message) {
//         this.message = message;
//         this.timestamp = timeUtils.currentTimeIso8601();
//     }

//     public Hello() {
//         this("Hello, World!");
//     }

// }
