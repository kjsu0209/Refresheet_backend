package com.pjt.refresheet.controller;

import com.pjt.refresheet.websocket.EditorInfo;
import com.pjt.refresheet.websocket.Greeting;
import com.pjt.refresheet.websocket.HelloMessage;
import com.pjt.refresheet.websocket.InitialInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class SheetCoWorkController {

    /**
     * Message Mapping annotation: /init로 메시지가 들어왔을때 initConnection을 call한다.
     * SendTo annotation: 리턴값이 /topic/sheet/{sheetId}를 구독하는 모든 구독자에게 broadcast
     * 
     * @param info 외부에서 들어온 메시지
     * @return greeting object
     * @throws Exception
     */
    @MessageMapping("/sheet/init/{sheetId}")
    public EditorInfo initConnection(@DestinationVariable String sheetId, InitialInfo info) throws Exception{
        log.debug("SheetWebSocket -initConnection : info => {}", info);
        Thread.sleep(1000); //simulated delay
        EditorInfo editor = EditorInfo.builder().sheetId(sheetId).ipAddr(info.getIpAddr()).build();
        editor.setName();
        return editor;
    }

}
