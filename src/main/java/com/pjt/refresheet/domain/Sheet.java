package com.pjt.refresheet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Sheet {

    /**
     * Sheet ID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "sheet_id")
    private String sheetId;

    /**
     * Sheet 이름
     */
    @Column(name = "sheet_name", nullable = false)
    private String sheetName;


    /**
     * 컬럼 수
     */
    @Column(name = "col_num", nullable = false)
    private Integer colNum;

    /**
     * 로우 수
     */
    @Column(name = "row_num", nullable = false)
    private Integer rowNum;

    /**
     * 최종 수정 일시
     */
    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @PrePersist
    public void createdAt() {
        this.lastModified = LocalDateTime.now();
    }

    /**
     * 컬럼 리스트
     */
    @OneToMany
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="sheet_id")
    private List<SheetColumn> sheetColumns;

    /**
     * 데이터 리스트
     */
    @OneToMany()
    @NotFound(action= NotFoundAction.IGNORE)
    @JoinColumn(name="sheet_id")
    private List<SheetData> sheetDataList;

}
