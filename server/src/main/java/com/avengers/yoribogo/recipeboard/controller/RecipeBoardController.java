package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService recipeBoardService;

    @Autowired
    public RecipeBoardController(RecipeBoardService recipeBoardService) {
        this.recipeBoardService = recipeBoardService;
    }

    // 나만의 레시피 등록
    @PostMapping("/create")
    public ResponseDTO<ResponseBoardDTO> createRecipeBoard(
            @RequestParam("menu_name") String recipeBoardMenuName,  // 메뉴 이름
            @RequestParam("ingredients") String recipeBoardIngredient,  // 재료
            @RequestParam("user_id") Long userId,  // 사용자 ID
            @RequestParam(value = "boardImage", required = false) MultipartFile boardImage // 파일 데이터 (선택사항)
    ) {
        // DTO 객체 생성
        RecipeBoardDTO recipeBoardDTO = new RecipeBoardDTO();
        recipeBoardDTO.setRecipeBoardMenuName(recipeBoardMenuName);
        recipeBoardDTO.setRecipeBoardIngredient(recipeBoardIngredient);
        recipeBoardDTO.setUserId(userId);
        recipeBoardDTO.setBoardImage(boardImage);

        // 서비스 호출하여 게시글 및 이미지 등록
        ResponseBoardDTO response = recipeBoardService.registRecipeBoard(recipeBoardDTO, boardImage);
        return ResponseDTO.ok(response);
    }

    // 나만의 레시피 매뉴얼 등록
    @PostMapping("/create/manual/{recipeBoardId}")
    public ResponseDTO<?> createRecipeManuals(
            @PathVariable("recipeBoardId") Long recipeBoardId,  // 레시피 ID
            @RequestParam("manual_steps") List<Integer> manualSteps,  // 메뉴얼 단계들
            @RequestParam("manual_contents") List<String> manualContents,  // 메뉴얼 내용들
            @RequestParam(value = "manual_images", required = false) List<MultipartFile> manualImages  // 메뉴얼 이미지들 (선택사항)
    ) {
        List<RecipeBoardManualDTO> manualDTOs = new ArrayList<>();

        // 각 매뉴얼 정보를 DTO로 변환하여 리스트에 추가
        for (int i = 0; i < manualSteps.size(); i++) {
            RecipeBoardManualDTO manualDTO = new RecipeBoardManualDTO();
            manualDTO.setRecipeBoardManualStep(manualSteps.get(i));
            manualDTO.setRecipeBoardManualContent(manualContents.get(i));

            // 이미지가 있는 경우에만 처리
            if (manualImages != null && i < manualImages.size() && !manualImages.get(i).isEmpty()) {
                manualDTO.setRecipeBoardManualImage(manualImages.get(i));
            }

            manualDTOs.add(manualDTO);
        }

        // 서비스 호출하여 여러 매뉴얼 등록
        ResponseBoardDTO response = recipeBoardService.addManualsToRecipeBoard(recipeBoardId, manualDTOs);

        return ResponseDTO.ok(response);
    }


    // 페이지 번호로 나만의 레시피 전체 조회
    @GetMapping("/boards")
    public ResponseDTO<?> getRecipeBoardByPageNo(@RequestParam Integer pageNo) {
        Page<RecipeBoardDTO> recipeBoardDTOPage = recipeBoardService.findRecipeBoardByPageNo(pageNo);
        return ResponseDTO.ok(recipeBoardDTOPage);
    }

    // 나만의 레시피 게시글 단건 조회
    @GetMapping("/detail/{recipeBoardId}")
    public ResponseDTO<?> getRecipeBoardById(@PathVariable("recipeBoardId") Long recipeBoardId) {
        ResponseBoardDTO recipeBoardDTO = recipeBoardService.findRecipeBoardById(recipeBoardId);
        return ResponseDTO.ok(recipeBoardDTO);
    }

    // 요리 이름으로 게시글 전체 조회
    @GetMapping("/search")
    public ResponseDTO<?> search(@RequestParam String recipeBoardMenuName,
                                 @RequestParam Integer pageNo) {
        Page<RecipeBoardDTO> recipeBoardDTOPage = recipeBoardService.findRecipeBoardByMenuName(recipeBoardMenuName, pageNo);
        return ResponseDTO.ok(recipeBoardDTOPage);
    }

    // 본인이 작성한 게시글 전체 조회
    @GetMapping("/users/{userId}/boards")
    public ResponseDTO<?> getUserBoards(@PathVariable Long userId,
                                        @RequestParam Integer pageNo) {
        Page<RecipeBoardDTO> recipeBoardDTOPage = recipeBoardService.findRecipeBoardByUserId(userId, pageNo);
        return ResponseDTO.ok(recipeBoardDTOPage);
    }

    // 게시글 수정
    @PutMapping("/update/{recipeBoardId}")
    public ResponseDTO<?> updateRecipeBoard(@PathVariable Long recipeBoardId,
                                            @RequestPart RecipeBoardDTO updateRecipeBoardDTO,
                                            @RequestPart(required = false) MultipartFile boardImage) {
        ResponseBoardDTO responseBoardDTO = recipeBoardService.updateRecipeBoard(recipeBoardId, updateRecipeBoardDTO, boardImage);
        return ResponseDTO.ok(responseBoardDTO);
    }

    // 매뉴얼 수정
    @PostMapping("/update/{recipeBoardId}/manuals")
    public ResponseDTO<?> updateRecipeBoardManual(@PathVariable Long recipeBoardId,
                                                  @RequestBody List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs) {
        ResponseBoardDTO responseBoardDTO = recipeBoardService.updateRecipeBoardManual(recipeBoardId, updateRecipeBoardManualDTOs);
        return ResponseDTO.ok(responseBoardDTO);
    }

    // 매뉴얼 삭제
    @DeleteMapping("/delete/{recipeBoardId}")
    public ResponseDTO<?> deleteRecipeBoard(@PathVariable Long recipeBoardId) {
        recipeBoardService.removeRecipeBoard(recipeBoardId);
        return ResponseDTO.ok(null);
    }

}
