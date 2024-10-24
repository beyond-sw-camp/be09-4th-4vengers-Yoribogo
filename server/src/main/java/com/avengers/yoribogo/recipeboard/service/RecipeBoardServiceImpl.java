package com.avengers.yoribogo.recipeboard.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardManual;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardStatus;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseBoardDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardManualRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RecipeBoardServiceImpl implements RecipeBoardService {

    public final Integer ELEMENTS_PER_PAGE = 12;

    private final ModelMapper modelMapper;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeBoardManualRepository recipeBoardManualRepository;
    private final AmazonS3Client s3Client;

    @Autowired
    public RecipeBoardServiceImpl(ModelMapper modelMapper,
                                  RecipeBoardRepository recipeBoardRepository,
                                  RecipeBoardManualRepository recipeBoardManualRepository,
                                  AmazonS3Client s3Client) {
        this.modelMapper = modelMapper;
        this.recipeBoardRepository = recipeBoardRepository;
        this.recipeBoardManualRepository = recipeBoardManualRepository;
        this.s3Client = s3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public ResponseBoardDTO addManualsToRecipeBoard(Long recipeBoardId, List<RecipeBoardManualDTO> manualDTOs) {
        // 게시글 찾기
        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        List<RecipeBoardManual> manualList = new ArrayList<>();

        for (RecipeBoardManualDTO manualDTO : manualDTOs) {
            RecipeBoardManual manual = modelMapper.map(manualDTO, RecipeBoardManual.class);
            manual.setRecipeBoard(recipeBoard);  // 게시글과 메뉴얼 연결

            // 이미지가 있으면 업로드
            if (manualDTO.getRecipeBoardManualImage() != null && !manualDTO.getRecipeBoardManualImage().isEmpty()) {
                String imageUrl = uploadManualImage(manualDTO.getRecipeBoardManualImage(), recipeBoardId, manualDTO.getRecipeBoardManualStep());
                manual.setRecipeBoardManualImage(imageUrl);
            }

            manualList.add(manual);  // 메뉴얼 리스트에 추가
        }

        // 메뉴얼 저장
        recipeBoardManualRepository.saveAll(manualList);

        // 게시글 정보를 다시 반환
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    // 매뉴얼 이미지 업로드 메서드
    public String uploadManualImage(MultipartFile manualImage, Long recipeBoardId, int manualStep) {
        String originalFilename = manualImage.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String fileName = "manual_" + recipeBoardId + "_" + manualStep + fileExtension;  // 파일명을 manual로 구분

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(manualImage.getSize());

            String contentType = manualImage.getContentType();
            if (contentType != null) {
                metadata.setContentType(contentType);
            } else {
                metadata.setContentType("application/octet-stream");
            }

            metadata.setContentDisposition("inline");

            // S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, manualImage.getInputStream(), metadata));

            // 업로드된 파일의 URL 반환
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonClientException | IOException e) {
            log.error("S3에 매뉴얼 이미지 업로드 실패", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    // 매뉴얼 이미지 삭제 메서드
    public void deleteManualImage(String manualImageUrl) {
        String splitStr = ".com/";
        String fileName = manualImageUrl.substring(manualImageUrl.lastIndexOf(splitStr) + splitStr.length());

        log.info("Attempting to delete manual image from S3: " + fileName);

        try {
            // S3에서 파일 삭제 요청
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("Successfully deleted manual image from S3: " + fileName);
        } catch (AmazonClientException e) {
            log.error("Failed to delete manual image from S3: " + fileName, e);
        }
    }


    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByPageNo(Integer pageNo) {

        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoardCreatedAt")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findAll(pageable);

        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public ResponseBoardDTO updateRecipeBoard(Long recipeBoardId, RecipeBoardDTO updateRecipeBoardDTO, MultipartFile boardImage) {

        // 기존 게시글 데이터 조회
        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        // 업데이트할 필드 값이 입력되었을 때만 변경
        if (updateRecipeBoardDTO.getRecipeBoardMenuName() != null) {
            recipeBoard.setRecipeBoardMenuName(updateRecipeBoardDTO.getRecipeBoardMenuName());
        }
        if (updateRecipeBoardDTO.getRecipeBoardIngredient() != null) {
            recipeBoard.setRecipeBoardIngredient(updateRecipeBoardDTO.getRecipeBoardIngredient());
        }

        // 이미지 필드
        if (boardImage != null && !boardImage.isEmpty()) {
            if (recipeBoard.getRecipeBoardImage() != null) {
                deleteBoardImage(recipeBoard.getRecipeBoardImage());
            }

            String imageUrl = uploadBoardImage(boardImage, recipeBoard.getRecipeBoardId());
            recipeBoard.setRecipeBoardImage(imageUrl);
        }

        recipeBoard = recipeBoardRepository.save(recipeBoard);
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public ResponseBoardDTO updateRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> updateRecipeBoardManualDTOs) {
        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        // 기존 매뉴얼 리스트 불러오기
        List<RecipeBoardManual> existingManuals = recipeBoardManualRepository.findByRecipeBoardRecipeBoardId(recipeBoardId);

        // 기존 매뉴얼을 Map으로 변환 (step 기준으로 관리하기 위해)
        Map<Integer, RecipeBoardManual> existingManualMap = existingManuals.stream()
                .collect(Collectors.toMap(RecipeBoardManual::getRecipeBoardManualStep, manual -> manual));

        // 새로 등록하거나 수정된 매뉴얼 리스트
        List<RecipeBoardManual> manualList = new ArrayList<>();

        for (RecipeBoardManualDTO manualDTO : updateRecipeBoardManualDTOs) {
            RecipeBoardManual manual = existingManualMap.get(manualDTO.getRecipeBoardManualStep());

            if (manual != null) {
                // 기존 매뉴얼이 있는 경우: 수정
                manual.setRecipeBoardManualContent(manualDTO.getRecipeBoardManualContent());

                // 이미지 수정이 필요한 경우
                if (manualDTO.getRecipeBoardManualImage() != null && !manualDTO.getRecipeBoardManualImage().isEmpty()) {
                    // 기존 이미지가 있다면 삭제
                    if (manual.getRecipeBoardManualImage() != null) {
                        deleteManualImage(manual.getRecipeBoardManualImage());
                    }
                    // 새 이미지 업로드
                    String imageUrl = uploadManualImage(manualDTO.getRecipeBoardManualImage(), recipeBoardId, manualDTO.getRecipeBoardManualStep());
                    manual.setRecipeBoardManualImage(imageUrl);
                }

            } else {
                // 새로운 매뉴얼이 있는 경우: 추가
                manual = modelMapper.map(manualDTO, RecipeBoardManual.class);
                manual.setRecipeBoard(recipeBoard);

                // 새로운 이미지가 있으면 업로드
                if (manualDTO.getRecipeBoardManualImage() != null && !manualDTO.getRecipeBoardManualImage().isEmpty()) {
                    String imageUrl = uploadManualImage(manualDTO.getRecipeBoardManualImage(), recipeBoardId, manualDTO.getRecipeBoardManualStep());
                    manual.setRecipeBoardManualImage(imageUrl);
                }
            }

            // 수정 또는 새로 추가된 매뉴얼 리스트에 추가
            manualList.add(manual);
        }

        // 삭제할 매뉴얼 리스트: 업데이트 요청에 없는 기존 매뉴얼은 삭제 대상
        List<RecipeBoardManual> manualsToDelete = existingManuals.stream()
                .filter(manual -> updateRecipeBoardManualDTOs.stream()
                        .noneMatch(dto -> dto.getRecipeBoardManualStep() == manual.getRecipeBoardManualStep()))
                .collect(Collectors.toList());

        // 삭제 처리
        for (RecipeBoardManual manualToDelete : manualsToDelete) {
            if (manualToDelete.getRecipeBoardManualImage() != null) {
                deleteManualImage(manualToDelete.getRecipeBoardManualImage());
            }
            recipeBoardManualRepository.delete(manualToDelete);
        }

        // 새로 등록되거나 수정된 매뉴얼을 저장
        recipeBoardManualRepository.saveAll(manualList);

        // 게시글 정보를 반환
        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }


    @Override
    public ResponseBoardDTO findRecipeBoardById(Long recipeBoardId) {

        RecipeBoard recipeBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        return modelMapper.map(recipeBoard, ResponseBoardDTO.class);
    }

    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByMenuName(String recipeBoardMenuName, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoardId")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findByRecipeBoardMenuNameContaining(recipeBoardMenuName, pageable);

        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public Page<RecipeBoardDTO> findRecipeBoardByUserId(Long userId, Integer pageNo) {
        // 페이지 번호 유효성 검사
        if (pageNo == null || pageNo < 1) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                ELEMENTS_PER_PAGE,
                Sort.by(Sort.Direction.DESC, "recipeBoardId")
        );

        Page<RecipeBoard> recipeBoardPage = recipeBoardRepository.findByUserId(userId, pageable);
        if (recipeBoardPage.getContent().isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }
        return convertEntityPageToDTOPage(recipeBoardPage);
    }

    @Override
    public void removeRecipeBoard(Long recipeBoardId) {
        RecipeBoard existingBoard = recipeBoardRepository.findById(recipeBoardId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD));

        recipeBoardRepository.delete(existingBoard);
    }


    private Page<RecipeBoardDTO> convertEntityPageToDTOPage(Page<RecipeBoard> recipeBoardPage) {
        List<RecipeBoardDTO> recipeBoardDTOList = recipeBoardPage.getContent().stream()
                .map(recipeBoard -> modelMapper.map(recipeBoard, RecipeBoardDTO.class))
                .toList();

        return new PageImpl<>(recipeBoardDTOList, recipeBoardPage.getPageable(), recipeBoardPage.getTotalElements());
    }

    @Override
    public ResponseBoardDTO registRecipeBoard(RecipeBoardDTO registRecipeBoardDTO, MultipartFile boardImage) {
        // 1. RecipeBoard 게시글 저장 준비
        RecipeBoard newRecipeBoard = modelMapper.map(registRecipeBoardDTO, RecipeBoard.class);
        newRecipeBoard.setUserId(registRecipeBoardDTO.getUserId());  // User ID 설정

        // 서버에서 자동으로 설정할 필드
        newRecipeBoard.setRecipeBoardCreatedAt(LocalDateTime.now().withNano(0));
        newRecipeBoard.setRecipeBoardComments(0);
        newRecipeBoard.setRecipeBoardLikes(0);
        newRecipeBoard.setRecipeBoardStatus(RecipeBoardStatus.ACTIVE);

        // 2. RecipeBoard를 먼저 저장하여 ID 생성
        newRecipeBoard = recipeBoardRepository.save(newRecipeBoard);  // 저장 후, 새로 저장된 객체 반환

        // 3. 보드 이미지 등록
        if (boardImage != null && !boardImage.isEmpty()) {
            // 이미지 업로드 후 URL 저장
            String imageUrl = uploadBoardImage(boardImage, newRecipeBoard.getRecipeBoardId());
            newRecipeBoard.setRecipeBoardImage(imageUrl);

            // 이미지가 업로드된 이후에 다시 저장
            newRecipeBoard = recipeBoardRepository.save(newRecipeBoard);
        }

        // 4. 저장된 RecipeBoard 객체를 ResponseBoardDTO로 변환하여 반환
        return modelMapper.map(newRecipeBoard, ResponseBoardDTO.class);
    }

    /** 설명.
     *  MultipartFile을 S3에 업로드하고, 업로드된 파일의 URL을 반환하는 메서드.
     * 설명.
     * @param boardImage 업로드할 게시글 이미지 파일
     * @param recipeBoardId 게시글의 ID로 파일명을 지정
     * @return 업로드된 파일의 S3 URL
     * @throws CommonException 파일 업로드에 실패할 경우 발생
     */
    public String uploadBoardImage(MultipartFile boardImage, Long recipeBoardId) {
        String originalFilename = boardImage.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String fileName = "board_" + recipeBoardId + fileExtension;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(boardImage.getSize());

            // 파일의 실제 Content-Type을 설정
            String contentType = boardImage.getContentType();
            if (contentType != null) {
                metadata.setContentType(contentType);
            } else {
                metadata.setContentType("application/octet-stream");
            }

            metadata.setContentDisposition("inline");

            // S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, boardImage.getInputStream(), metadata));

            // 업로드된 파일의 URL 반환
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonClientException | IOException e) {
            log.error("S3에 이미지 업로드 실패", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**설명.
     *  S3에서 기존 게시글 이미지를 삭제하는 메서드.
     *설명.
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteBoardImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        log.info("Attempting to delete file from S3: " + fileName);

        try {
            // S3에서 파일 삭제 요청
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("Successfully deleted image from S3: " + fileName);
        } catch (AmazonClientException e) {
            log.error("Failed to delete image from S3: " + fileName, e);
        }
    }

}

