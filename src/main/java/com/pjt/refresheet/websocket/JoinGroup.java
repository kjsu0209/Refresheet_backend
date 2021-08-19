package com.pjt.refresheet.websocket;

import lombok.Data;

/**
 * document 그룹에 connection 요청 객체
 */
@Data
public class JoinGroup {

    private String docName;
    private Boolean isOwner;

    public JoinGroup(String docName, Boolean isOwner){
        this.docName = docName;
        this.isOwner = isOwner;
    }
}
