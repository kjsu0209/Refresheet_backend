package com.pjt.refresheet.websocket;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentSheetInfo {

    private List<EditorInfo> editors;
    private String sheetId;

}
