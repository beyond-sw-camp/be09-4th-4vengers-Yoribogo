package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardFavorite;
import com.avengers.yoribogo.recipeboard.dto.*;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardFavoriteRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RecipeBoardFavoriteServiceImpl implements RecipeBoardFavoriteService {

    private final Integer ELEMENTS_PER_PAGE = 12;

    private final ModelMapper modelMapper;
    private final RecipeBoardFavoriteRepository recipeBoardFavoriteRepository;
    private final RecipeBoardRepository recipeBoardRepository;  // RecipeBoardRepository 추가

    @Autowired
    public RecipeBoardFavoriteServiceImpl(ModelMapper modelMapper,
                                          RecipeBoardFavoriteRepository recipeBoardFavoriteRepository,
                                          RecipeBoardRepository recipeBoardRepository) {  // RecipeBoardRepository 의존성 주입
        this.modelMapper = modelMapper;
        this.recipeBoardFavoriteRepository = recipeBoardFavoriteRepository;
        this.recipeBoardRepository = recipeBoardRepository;
    }

    @Override
    public ResponseFavoriteDTO addFavorite(RecipeBoardFavoriteDTO recipeBoardFavoriteDTO) {
        Long userId = recipeBoardFavoriteDTO.getUserId();
        Long boardId = recipeBoardFavoriteDTO.getRecipeBoardId();

        RecipeBoard recipeBoard = recipeBoardRepository.findById(boardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        if (!recipeBoardFavoriteRepository.existsByUserIdAndRecipeBoard_RecipeBoardId(userId, boardId)) {
            RecipeBoardFavorite favorite = new RecipeBoardFavorite();
            favorite.setUserId(userId);
            favorite.setRecipeBoard(recipeBoard);
            RecipeBoardFavorite savedFavorite = recipeBoardFavoriteRepository.save(favorite);

            // ResponseFavoriteDTO로 수동 매핑
            ResponseFavoriteDTO responseFavoriteDTO = new ResponseFavoriteDTO();
            responseFavoriteDTO.setRecipeBoardFavoriteId(savedFavorite.getRecipeBoardFavoriteId());
            responseFavoriteDTO.setUserId(savedFavorite.getUserId());
            responseFavoriteDTO.setRecipeBoardId(savedFavorite.getRecipeBoard().getRecipeBoardId());

            return responseFavoriteDTO;
        }
        return null;
    }

    @Override
    public Page<MyFavoriteBoardDTO> findFavoriteBoardsByUserId(Long userId, Integer pageNo) {
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoard.recipeBoardId")
        );

        Page<RecipeBoardFavorite> favorites = recipeBoardFavoriteRepository.findAllByUserId(userId, pageable);

        return favorites.map(favorite -> {
            RecipeBoard recipeBoard = favorite.getRecipeBoard();
            return new MyFavoriteBoardDTO(
                    recipeBoard.getRecipeBoardId(),
                    recipeBoard.getRecipeBoardMenuName(),
                    recipeBoard.getRecipeBoardImage()
            );
        });
    }

    @Override
    public void removeFavorite(Long userId, Long recipeBoardId) {
        RecipeBoardFavorite favorite = recipeBoardFavoriteRepository
                .findByUserIdAndRecipeBoard_RecipeBoardId(userId, recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        recipeBoardFavoriteRepository.delete(favorite);
    }

    @Override
    public FavoriteStatusDTO getFavoriteStatus(Long userId, Long recipeBoardId) {

        boolean isExists = recipeBoardFavoriteRepository.existsByUserIdAndRecipeBoard_RecipeBoardId(userId, recipeBoardId);
        return new FavoriteStatusDTO(isExists);
    }

    //    @Override
//    public Page<RecipeBoardDTO> findFavoriteBoardsByUserId(Long userId, Integer pageNo) {
//        if (pageNo == null || pageNo < 1) {
//            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
//        }
//
//        Pageable pageable = PageRequest.of(
//                pageNo - 1,
//                ELEMENTS_PER_PAGE,
//                Sort.by(Sort.Direction.DESC, "recirecipe_board_commentpeBoard.recipeBoardId")
//        );
//
//        Page<RecipeBoardFavorite> favorites = recipeBoardFavoriteRepository.findAllByUserId(userId, pageable);
//
//        return favorites.map(favorite -> modelMapper.map(favorite, RecipeBoardDTO.class));
//    }
//
//    @Override
//    public ResponseFavoriteDTO toggleFavorite(RecipeBoardFavoriteDTO recipeBoardFavoriteDTO) {
//        Long userId = recipeBoardFavoriteDTO.getUserId();
//        Long boardId = recipeBoardFavoriteDTO.getBoardId();
//
//        // RecipeBoard 엔티티 조회
//        RecipeBoard recipeBoard = recipeBoardRepository.findById(boardId)
//                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));
//
//        Optional<RecipeBoardFavorite> favorite = recipeBoardFavoriteRepository
//                .findByUserIdAndRecipeBoard_RecipeBoardId(userId, boardId);
//
//        if (favorite.isPresent()) {
//            // 즐겨찾기 삭제
//            recipeBoardFavoriteRepository.delete(favorite.get());
//            return null;
//        } else {
//            // 새로운 즐겨찾기 추가
//            RecipeBoardFavorite newFavorite = new RecipeBoardFavorite();
//            newFavorite.setUserId(userId);
//            newFavorite.setRecipeBoard(recipeBoard);  // RecipeBoard 엔티티 설정
//            newFavorite = recipeBoardFavoriteRepository.save(newFavorite);
//
//            log.info("service", newFavorite.toString());
//
//            return modelMapper.map(newFavorite, ResponseFavoriteDTO.class);
//        }
//  }
}
