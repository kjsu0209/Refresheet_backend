package com.pjt.refresheet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class SheetData {

    /**
     * data ID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "data_id")
    private String dataId;

    /**
     * row
     */
    @Column(nullable = false, name="row_no")
    private Integer rowNo;

    /**
     * col
     */
    @Column(nullable = false, name="col_no")
    private Integer colNo;

    /**
     * data
     */
    @Column(nullable = false)
    private String data;


    /**
     * sheet id
     */
    @Column(nullable = false, name="sheet_id")
    private String sheetId;

}
