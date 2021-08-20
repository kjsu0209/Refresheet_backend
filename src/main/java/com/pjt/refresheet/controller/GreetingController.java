package com.pjt.refresheet.controller;

import com.pjt.refresheet.websocket.Greeting;
import com.pjt.refresheet.websocket.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class GreetingController {

    /**
     * Message Mapping annotation: /hello로 메시지가 들어왔을때 greeting을 call한다.
     * SendTo annotation: 리턴값이 /topic/greeting을 구독하는 모든 구독자에게 broadcast
     * 
     * @param message 외부에서 들어온 메시지
     * @return greeting object
     * @throws Exception
     */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception{
        log.debug("message: {}", message);
        Thread.sleep(1000); //simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}
