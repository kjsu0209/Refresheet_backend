package com.pjt.refresheet.controller;

import com.pjt.refresheet.domain.SheetColumn;
import com.pjt.refresheet.domain.SheetData;
import com.pjt.refresheet.service.SheetService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("sheet")
@RestController
@Validated
public class SheetController {

    private final SheetService service;

    /**
     * sheet 생성
     * @param request SheetRequestDto
     */
    @PostMapping("/v1.0.0")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSheet(@RequestBody @Valid SheetService.SheetRequestDto request) throws NotFoundException {
        log.debug("Controller createSheet - request: {}", request);
        return service.createSheet(request);
    }



    /**
     * sheetId로 sheet 불러오기
     * @param sheetId String
     * @return SheetVO
     * @throws NotFoundException id가 존재하지 않음
     */
    @GetMapping("/v1.0.0/{sheetId}")
    @ResponseStatus(HttpStatus.OK)
    public SheetService.SheetVO findSheetById(@PathVariable(name = "sheetId") String sheetId) throws NotFoundException {
        log.debug("Controller findSheetById - sheetId: {}", sheetId);

        return service.findSheetById(sheetId);
    }

    /**
     * sheet 전부 불러오기
     * @return List
     */
    @GetMapping("/v1.0.0")
    @ResponseStatus(HttpStatus.OK)
    public List<SheetService.SheetVO> findSheetList(){
        log.debug("Controller findSheetList ");

        return service.findSheetAll();
    }

    /**
     * sheet 수정하기
     */
    @PutMapping("/v1.0.0/{sheetId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSheet(@PathVariable(name = "sheetId") String sheetId, @RequestBody @Valid SheetService.SheetRequestDto requestDto) throws NotFoundException {
        log.debug("Cotroller updateSheet");

        service.updateSheet(sheetId, requestDto);
    }

    /**
     * sheet data만 수정하기
     * @param request SheetDataRequestDto
     * @return ResponseEntity
     */
    @PostMapping("/v1.0.0/data")
    public ResponseEntity<Object> upsertSheetData(@RequestBody @Valid SheetService.SheetDataRequestDto request) throws NotFoundException {
        log.debug("Controller upsertSheetData - request: {}", request);

        Optional<String> upsertResult = service.upsertSheetData(request);
        if(upsertResult.isEmpty()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(upsertResult.get(), HttpStatus.CREATED);
        }
    }

    /**
     * column 추가
     * @param sheetId String
     * @param request SheetColRequestDto
     * @throws NotFoundException 404
     */
    @PostMapping("/v1.0.0/{sheetId}/col")
    public void createColumn(@PathVariable(name = "sheetId") String sheetId, @RequestBody @Valid SheetService.SheetColRequestDto request) throws NotFoundException {
        log.debug("Controller createColumn - request: {}", request);

        service.createColumn(sheetId, request);
    }

    /**
     * column 수정
     * @param sheetId String
     * @param request SheetColRequestDto
     * @throws NotFoundException 404
     */
    @PutMapping("/v1.0.0/{sheetId}/col")
    public void updateColumn(@PathVariable(name = "sheetId") String sheetId, @RequestBody @Valid SheetService.SheetColRequestDto request) throws NotFoundException {
        log.debug("Controller updateColumn - request: {}", request);

        service.updateColumn(sheetId, request);
    }
}
