package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.*;
import org.springframework.data.domain.Page;

public interface RecipeBoardFavoriteService {

    ResponseFavoriteDTO addFavorite(RecipeBoardFavoriteDTO recipeBoardFavoriteDTO);

    Page<MyFavoriteBoardDTO> findFavoriteBoardsByUserId(Long userId, Integer pageNo);

    void removeFavorite(Long userId, Long recipeBoardId);

    FavoriteStatusDTO getFavoriteStatus(Long userId, Long recipeBoardId);
}
