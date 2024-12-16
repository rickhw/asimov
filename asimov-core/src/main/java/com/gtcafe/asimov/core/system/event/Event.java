// package com.gtcafe.asimov.core.system.event;

// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.gtcafe.asimov.core.system.utils.TimeUtils;

// import lombok.Getter;
// import lombok.Setter;

// public class Event<T> {

//     @Getter
//     @Setter
//     private String id;

//     // @Getter
//     // @Setter
//     // private TaskState state;

//     @Getter
//     @Setter
//     @JsonProperty("_creationTime")
//     private String creationTime;

//     @Getter
//     @Setter
//     @JsonProperty("_lastModified")
//     private String lastModified;

//     @Getter
//     @Setter
//     private T data;

//     @Autowired
//     private TimeUtils timeUtils;

//     public Event() {
//         this.id = UUID.randomUUID().toString();
//         // this.state = TaskState.PENDING;
//         this.creationTime = timeUtils.currentTimeIso8601();
//         this.lastModified = timeUtils.currentTimeIso8601();
//     }

//     public void updateLastModified() {
//         this.lastModified = timeUtils.currentTimeIso8601();
//     }

//     // public void transit(TaskState toState) {
//     //     this.state = toState;
//     //     updateLastModified();
//     // }
// }
