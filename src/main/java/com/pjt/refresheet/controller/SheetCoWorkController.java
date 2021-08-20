package com.pjt.refresheet.controller;

import com.pjt.refresheet.domain.SheetData;
import com.pjt.refresheet.websocket.EditorInfo;
import com.pjt.refresheet.websocket.Greeting;
import com.pjt.refresheet.websocket.HelloMessage;
import com.pjt.refresheet.websocket.InitialInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SheetCoWorkController {

    private final SimpUserRegistry simpUserRegistry;

    private List<EditorInfo> editors = new ArrayList<>();

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
        Thread.sleep(100); //simulated delay
        EditorInfo editor = EditorInfo.builder().sheetId(sheetId).ipAddr(info.getIpAddr()).build();
        editor.setSessionId(info.getSessionId());

        editor.setName();
        List<EditorInfo> editorInfos = new ArrayList<>();
        for(EditorInfo e : editors){
            editorInfos.add(e);
        }
        editor.setCurrentEditors(editorInfos);
        editors.add(editor);
        return editor;
    }

    /**
     * 데이터 변경 시 메시지가 들어오면 해당 변경 내용을 broadcast한다.
     * @param msg 외부에서 들어온 메시지
     * @return greeting object
     * @throws Exception
     */
    @MessageMapping("/sheet/edit/{sheetId}")
    public SheetDataMsg modifySheetData(@DestinationVariable String sheetId, SheetDataMsg msg) throws Exception{
        log.debug("SheetWebSocket -modifySheetData : msg => {}", msg);

        return msg;
    }

    @Data
    @NoArgsConstructor
    public static class SheetDataMsg{
        private String editorName;

        private String dataId;

        /**
         * row
         */
        private Integer rowNo;

        /**
         * col
         */
        private Integer colNo;

        /**
         * data
         */
        private String data;


        /**
         * sheet id
         */
        private String sheetId;
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        for(int i=0;i<editors.size();i++){
            if(editors.get(i).getSessionId().equals(event.getSessionId())){
                editors.remove(i);
                break;
            }
        }
    }
}
