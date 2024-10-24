package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipeboard.dto.*;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/recipe-board/favorites")
public class RecipeBoardFavoriteController {

    private final RecipeBoardFavoriteService recipeBoardFavoriteService;

    @Autowired
    public RecipeBoardFavoriteController(RecipeBoardFavoriteService recipeBoardFavoriteService) {
        this.recipeBoardFavoriteService = recipeBoardFavoriteService;
    }

    @PostMapping("/")
    public ResponseDTO<?> addFavorite(@RequestBody RecipeBoardFavoriteDTO recipeBoardFavoriteDTO) {
        ResponseFavoriteDTO responseFavoriteDTO = recipeBoardFavoriteService.addFavorite(recipeBoardFavoriteDTO);
        return ResponseDTO.ok(responseFavoriteDTO); // 등록 성공 확인용 dto
    }

    // 회원이 즐겨찾기한 내역 페이지로 조회(마이페이지에서)
    @GetMapping("/users/{userId}")
    public ResponseDTO<?> getFavorites(@PathVariable("userId") Long userId, @RequestParam Integer pageNo) {
        Page<MyFavoriteBoardDTO> favoriteBoardDTOs = recipeBoardFavoriteService.findFavoriteBoardsByUserId(userId, pageNo);
        return ResponseDTO.ok(favoriteBoardDTOs);
    }

    @DeleteMapping("/users/{userId}/boards/{recipeBoardId}")
    public ResponseDTO<?> removeFavorite(@PathVariable("userId") Long userId, @PathVariable("recipeBoardId") Long recipeBoardId) {
        recipeBoardFavoriteService.removeFavorite(userId, recipeBoardId);
        return ResponseDTO.ok(null);
    }

    @GetMapping("/users/{userId}/boards/{recipeBoardId}/status")
    public ResponseDTO<?> getFavoriteStatus(@PathVariable("userId") Long userId,
                                            @PathVariable("recipeBoardId") Long recipeBoardId) {
        FavoriteStatusDTO favoriteStatusDTO = recipeBoardFavoriteService.getFavoriteStatus(userId, recipeBoardId);
        return ResponseDTO.ok(favoriteStatusDTO);
    }


}
