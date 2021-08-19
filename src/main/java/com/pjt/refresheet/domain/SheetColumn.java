package com.pjt.refresheet.domain;

import com.pjt.refresheet.enumtype.ColumnDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SheetColumn {

    /**
     * 컬럼 넘버
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_no")
    private Integer colNo;

    /**
     * 컬럼 이름
     */
    @Column(nullable = false, name="col_name")
    private String colName;

    /**
     * 컬럼 데이터 타입
     */
    @Column(nullable = false, name="data_type")
    private ColumnDataType dataType;

    /**
     * sheet Id
     */
    @Column(nullable = false, name="sheet_id")
    private String sheetId;
}
