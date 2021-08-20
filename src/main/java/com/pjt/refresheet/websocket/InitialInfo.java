package com.pjt.refresheet.websocket;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InitialInfo {

    private String ipAddr;
    private String sessionId;

}
