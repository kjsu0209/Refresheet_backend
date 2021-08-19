package com.pjt.refresheet.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.pjt.refresheet.domain.Sheet;
import com.pjt.refresheet.domain.SheetColumn;
import com.pjt.refresheet.domain.SheetData;
import com.pjt.refresheet.enumtype.ColumnDataType;
import com.pjt.refresheet.repository.SheetColumnRepository;
import com.pjt.refresheet.repository.SheetDataRepository;
import com.pjt.refresheet.repository.SheetRepository;
import javassist.NotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SheetService {

    private final SheetRepository sheetRepository;
    private final SheetColumnRepository sheetColumnRepository;
    private final SheetDataRepository sheetDataRepository;

    /**
     * Sheet 생성
     * @param requestDto SheetRequestDto
     */
    @Transactional
    public String createSheet(SheetRequestDto requestDto) throws NotFoundException {
        log.debug("SheetService - createSheet requestDto : {}", requestDto);
        // save sheet
        if(ObjectUtils.isEmpty(requestDto.getSheetName())){
            requestDto.setSheetName("Sheet Title");
        }
        Sheet sheet = Sheet.builder().sheetName(requestDto.getSheetName()).colNum(0).rowNum(0).build();
        sheet = sheetRepository.save(sheet);

        // convert colDTO to col
        if(!ObjectUtils.isEmpty(requestDto.sheetColumns)){
            List<SheetColRequestDto> sheetColumns
                    = requestDto.getSheetColumns().stream().map(item -> {
                SheetColRequestDto col;
                col = SheetColRequestDto.builder()
                        .colName(item.getColName())
                        .dataType(item.getDataType())
                        .colNo(item.getColNo()).build();
                return col;
            }).collect(Collectors.toList());

            // save cols
            for(int i=0;i<sheetColumns.size();i++){
                this.createColumn(sheet.getSheetId(), sheetColumns.get(i));
            }
        }else{
            // default data 추가
            List<SheetColRequestDto> sheetColumns = new ArrayList<>();
            SheetColRequestDto col1 =
                    SheetColRequestDto.builder().colName("text").dataType(ColumnDataType.TEXT).build();
            SheetColRequestDto col2 =
                    SheetColRequestDto.builder().colName("number").dataType(ColumnDataType.NUMBER).build();
            sheetColumns.add(col1);
            sheetColumns.add(col2);

            for(int i=0;i<sheetColumns.size();i++){
                this.createColumn(sheet.getSheetId(), sheetColumns.get(i));
            }
        }

        return sheet.getSheetId();
    }
    @Data
    @Builder
    @AllArgsConstructor
    public static class SheetRequestDto{
        private String sheetName;
        private List<SheetColRequestDto> sheetColumns;
    }
    @Data
    @Builder
    @AllArgsConstructor
    public static class SheetColRequestDto{
        private Integer colNo;
        private String colName;
        private ColumnDataType dataType;
    }

    /**
     * 데이터 조회
     * @param sheetId String
     * @return Sheet
     */
    public SheetVO findSheetById(String sheetId) throws NotFoundException {
        Optional<Sheet> sheet = sheetRepository.findById(sheetId);
        if(sheet.isEmpty()){
            throw new NotFoundException("해당 sheet id를 찾을 수 없습니다.");
        }

        return SheetVO.fromSheet(sheet.get());
    }

    /**
     * 데이터 수정
     */
    public void updateSheet(String sheetId, SheetRequestDto request) throws NotFoundException {
        log.debug("SheetService - updateSheet requestDto : {}", request);
        // save sheet
        Sheet sheet = new Sheet();
        SheetVO sheetVO = this.findSheetById(sheetId);
        BeanUtils.copyProperties(sheetVO, sheet);
        sheet.setSheetId(sheetId);
        BeanUtils.copyProperties(request, sheet);

        sheetRepository.save(sheet);
    }

    /**
     * 데이터 전부 조회
     * @return List
     */
    public List<SheetVO> findSheetAll(){
        List<Sheet> sheets = sheetRepository.findAll(Sort.by(Sort.Direction.DESC, "lastModified"));

        return sheets.stream()
                .map(SheetVO::fromSheet)
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SheetVO{
        String sheetId;
        String sheetName;
        Integer colNum;
        Integer rowNum;
        LocalDateTime lastModified;
        List<SheetColumn> sheetColumns;
        List<SheetData> sheetDataList;

        public static SheetVO fromSheet(Sheet sheet){
            SheetVO sheetVO = new SheetVO();
            BeanUtils.copyProperties(sheet, sheetVO);

            return sheetVO;
        }
    }

    /**
     * 데이터 업데이트
     * @param requestDto sheetDataRequestDto
     * @return created row count
     */
    @Transactional
    public Optional<String> upsertSheetData(SheetDataRequestDto requestDto) throws NotFoundException {
        SheetData sheetData = SheetData.builder().sheetId(requestDto.getSheetId()).data(requestDto.getData())
                .rowNo(requestDto.getRowNo()).colNo(requestDto.getColNo()).build();

        SheetVO sheetVo = findSheetById(requestDto.getSheetId());
        log.debug("sheet rowNum:{}, sheetData rowNo: {}", sheetVo.getRowNum(), sheetData.getRowNo());
        if(sheetVo.getRowNum() < sheetData.getRowNo()+1){
            Sheet sheetDt = new Sheet();
            BeanUtils.copyProperties(sheetVo, sheetDt);
            sheetDt.setRowNum(sheetData.getRowNo()+1);
            sheetRepository.save(sheetDt);
        }

        if (ObjectUtils.isEmpty(requestDto.getDataId())) {
            // 처음 업데이트 한 경우
            SheetData created = sheetDataRepository.save(sheetData);
            return Optional.ofNullable(created.getDataId());
        } else {
            sheetData.setDataId(requestDto.getDataId());
            sheetDataRepository.findById(requestDto.getDataId()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            sheetDataRepository.saveAndFlush(sheetData);
            return Optional.ofNullable(null);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SheetDataRequestDto {
        @NotEmpty
        private String sheetId;
        private String dataId;
        @NotNull
        private Integer rowNo;
        @NotNull
        private Integer colNo;
        @NotNull
        private String data;
    }

    /**
     * 컬럼 추가
     */
    @Transactional
    public void createColumn(String sheetId, SheetColRequestDto request) throws NotFoundException {
        // sheet col 숫자 증가
        SheetService.SheetVO sheetVO = this.findSheetById(sheetId);
        Sheet sheet = new Sheet();
        BeanUtils.copyProperties(sheetVO, sheet);
        sheet.setColNum(sheet.getColNum() + 1);
        sheetRepository.save(sheet);

        // col 데이터 추가
        SheetColumn sheetColumn = new SheetColumn();
        BeanUtils.copyProperties(request, sheetColumn);
        sheetColumn.setSheetId(sheetId);
        sheetColumnRepository.save(sheetColumn);
    }

    /**
     * 컬럼 삭제
     */
    @Transactional
    public void deleteColumn(Integer colNo){
        sheetColumnRepository.deleteById(colNo);
    }

    /**
     * 컬럼 수정
     */
    @Transactional
    public void updateColumn(String sheetId, SheetColRequestDto request) {
        // col 데이터 추가
        SheetColumn sheetColumn = sheetColumnRepository.findById(request.getColNo()).get();
        log.debug("find col to update : {}", sheetColumn);
        BeanUtils.copyProperties(request, sheetColumn);
        sheetColumn.setSheetId(sheetId);
        sheetColumnRepository.save(sheetColumn);
    }
}
