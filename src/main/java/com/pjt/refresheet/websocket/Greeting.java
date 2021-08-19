package com.pjt.refresheet.websocket;

import lombok.Data;

@Data
public class Greeting {
    private String content;

    public Greeting(String content){
        this.content = content;
    }
}
