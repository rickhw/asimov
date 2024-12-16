// package com.gtcafe.asimov.core.system.event2;

// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.gtcafe.asimov.core.system.utils.TimeUtils;

// import lombok.Getter;
// import lombok.Setter;

// // public class Metadata<T> {
// public class Metadata {

//     @Getter
//     @Setter
//     @JsonProperty("_id")
//     private String id;

//     @Getter
//     @Setter
//     // private T state;
//     private Enum<?> state;

//     @Getter
//     @Setter
//     @JsonProperty("_creationTime")
//     private String creationTime;

//     @Getter
//     @Setter
//     @JsonProperty("_lastModified")
//     private String lastModified;

//     @Autowired
//     private TimeUtils timeUtils;

//     public Metadata() {
//         this.id = UUID.randomUUID().toString();
//         this.creationTime = timeUtils.currentTimeIso8601();
//         this.lastModified = timeUtils.currentTimeIso8601();
//     }

//     public void updateLastModified() {
//         this.lastModified = timeUtils.currentTimeIso8601();
//     }

//     public void transit(Enum<?> toState) {
//         this.state = toState;
//         updateLastModified();
//     }
// }
