package com.avengers.yoribogo.recipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipe.domain.RecommendedMenu;
import com.avengers.yoribogo.recipe.domain.RecommendedMenuStatus;
import com.avengers.yoribogo.recipe.domain.Satisfaction;
import com.avengers.yoribogo.recipe.dto.GoodMenuDTO;
import com.avengers.yoribogo.recipe.dto.RecommendedMenuDTO;
import com.avengers.yoribogo.recipe.repository.RecommendedMenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RecommendedMenuServiceImpl implements RecommendedMenuService {

    private final Integer ELEMENTS_PER_PAGE = 12;

    private final ModelMapper modelMapper;
    private final RecommendedMenuRepository recommendedMenuRepository;

    @Autowired
    public RecommendedMenuServiceImpl(ModelMapper modelMapper,
                                      RecommendedMenuRepository recommendedMenuRepository) {
        this.modelMapper = modelMapper;
        this.recommendedMenuRepository = recommendedMenuRepository;
    }

    // 추천 요리 회원별 조회
    @Override
    public Page<GoodMenuDTO> findRecommendedMenuByUserId(Long userId, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        // 페이지와 한 페이지당 요소 수 설정 (pageNumber는 0부터 시작)
        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recommendedMenuId")
        );

        // 기존 엔티티 목록 조회
        Page<GoodMenuDTO> recommendedMenuList = recommendedMenuRepository.findRecommendedMenuWithRecipeByUserId(
                userId, Satisfaction.GOOD, RecommendedMenuStatus.ACTIVE, pageable);

        // 조회된 결과가 없을 때 예외 처리
        if (recommendedMenuList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECOMMENDED_MENU);
        }

        return recommendedMenuList;
    }

    // 추천 요리 등록
    @Override
    @Transactional
    public RecommendedMenuDTO registRecommendedMenu(RecommendedMenuDTO registRecommendedMenuDTO) {
        // 기존 엔티티 조회
        RecommendedMenu existingMenu = recommendedMenuRepository.findByRecipeIdAndUserId(
                registRecommendedMenuDTO.getRecipeId(), registRecommendedMenuDTO.getUserId());

        // 이미 있을 경우 정보 수정
        if (existingMenu != null) {
            existingMenu.setRecommendedMenuStatus(registRecommendedMenuDTO.getRecommendedMenuStatus());
            return modelMapper.map(existingMenu, RecommendedMenuDTO.class);
        }

        RecommendedMenuDTO newRecommendedMenuDTO = RecommendedMenuDTO
                .builder()
                .satisfaction(registRecommendedMenuDTO.getSatisfaction())
                .recommendedMenuStatus(RecommendedMenuStatus.ACTIVE)
                .recommendedMenuCreatedAt(LocalDateTime.now().withNano(0))
                .userId(registRecommendedMenuDTO.getUserId())
                .recipeId(registRecommendedMenuDTO.getRecipeId())
                .build();

        RecommendedMenu recommendedMenu =
            recommendedMenuRepository.save(modelMapper.map(newRecommendedMenuDTO, RecommendedMenu.class));

        return modelMapper.map(recommendedMenu, RecommendedMenuDTO.class);
    }

    // 추천 요리 삭제
    @Override
    @Transactional
    public void removeRecommendedMenu(Long recommendedMenuId) {
        // 기존 엔티티 조회
        RecommendedMenu recommendedMenu = recommendedMenuRepository.findById(recommendedMenuId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECOMMENDED_MENU));

        // 이미 삭제 처리된 엔티티인 경우 예외 처리
        if(recommendedMenu.getRecommendedMenuStatus() == RecommendedMenuStatus.INACTIVE) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECOMMENDED_MENU);
        }

        // INACTIVE 처리(SOFT DELETE)
        recommendedMenu.setRecommendedMenuStatus(RecommendedMenuStatus.INACTIVE);

        // 변경 사항 저장
        recommendedMenuRepository.save(recommendedMenu);
    }

}
