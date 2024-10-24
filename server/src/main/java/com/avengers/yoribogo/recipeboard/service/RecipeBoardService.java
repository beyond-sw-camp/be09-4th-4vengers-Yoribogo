package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeBoardService {

    ResponseBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO, MultipartFile boardImage);

    Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo);

    ResponseBoardDTO findRecipeBoardById(Long recipeBoardId);

    ResponseBoardDTO addManualsToRecipeBoard(Long recipeBoardId, List<RecipeBoardManualDTO> manualDTOs);

    ResponseBoardDTO updateRecipeBoard(Long recipeBoardId, RecipeBoardDTO updateRecipeBoardDTO, MultipartFile boardImage);

    ResponseBoardDTO updateRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs);

    void removeRecipeBoard(Long recipeBoardId);

    Page<RecipeBoardDTO> findRecipeBoardByMenuName(String recipeBoardName, Integer pageNo);

    Page<RecipeBoardDTO> findRecipeBoardByUserId(Long userId, Integer pageNo);
}
