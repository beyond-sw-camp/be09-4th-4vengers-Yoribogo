package com.avengers.yoribogo.recipe.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.notification.service.NotificationService;
import com.avengers.yoribogo.openai.service.OpenAIService;
import com.avengers.yoribogo.recipe.domain.Recipe;
import com.avengers.yoribogo.recipe.dto.AIRecipeDTO;
import com.avengers.yoribogo.recipe.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;
    private final AIRecipeService aiRecipeService;
    private final OpenAIService openAIService;
    private final NotificationService notificationService;
    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public ImageServiceImpl(RecipeRepository recipeRepository,
                            AIRecipeService aiRecipeService,
                            OpenAIService openAIService,
                            NotificationService notificationService,
                            AmazonS3Client s3Client) {
        this.recipeRepository = recipeRepository;
        this.aiRecipeService = aiRecipeService;
        this.openAIService = openAIService;
        this.notificationService = notificationService;
        this.s3Client = s3Client;
    }

    // AI 이미지 생성
    @Async
    @Override
    public void generateImageAsync(String trimmedDescription, Long recipeId) {
        try {
            // AI 사진 생성
            String gptImageUrl = registImages(trimmedDescription);

            // S3에 이미지 업로드
            String s3ImageUrl = uploadMenuImage(gptImageUrl, recipeId);

            // 기존 엔티티 조회
            Recipe existingRecipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

            // 엔티티 정보 수정
            existingRecipe.setMenuImage(s3ImageUrl);

            recipeRepository.save(existingRecipe);

            // DTO에 요리 레시피 정보 담기
            AIRecipeDTO aiRecipeDTO = AIRecipeDTO
                    .builder()
                    .menuName(existingRecipe.getMenuName())
                    .menuIngredient(existingRecipe.getMenuIngredient())
                    .menuImage(s3ImageUrl)
                    .recipeId(existingRecipe.getRecipeId())
                    .build();

            // AI 요리 레시피 수정
            aiRecipeService.modifyAIRecipe(aiRecipeDTO);
            notificationService.sendImageUpdateNotification(s3ImageUrl);
        } catch (Exception e) {
            log.error("레시피 ID: " + recipeId + "의 이미지 생성 중 오류 발생", e);
        }
    }

    // 요리 사진 생성하기
    public String registImages(String menuName) {
        return openAIService.getImages(menuName).getData().get(0).getUrl();
    }

    // 이미지의 url을 받아 S3에 등록하는 메소드
    public String uploadMenuImage(String imageUrl, Long recipeId) {
        String fileName = "recipe_" + recipeId + ".png"; // 기본 파일명 및 확장자

        // URL 객체 생성
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            // 응답 코드 확인
            if (connection.getResponseCode() != 200) {
                throw new IOException("URL에서 이미지를 가져오지 못했습니다. 응답 코드: " + connection.getResponseCode());
            }

            // 연결에서 입력 스트림 가져오기
            try (InputStream inputStream = connection.getInputStream()) {
                // S3에 대한 ObjectMetadata 생성
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(connection.getContentLengthLong());
                metadata.setContentType(connection.getContentType());
                metadata.setContentDisposition("inline");

                // S3에 파일 업로드
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
            } // try-with-resources 문을 사용하여 입력 스트림 자동으로 닫기

            // 업로드된 이미지의 S3 URL 반환
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonClientException | IOException e) {
            log.error("S3에 이미지 업로드 실패", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    // 이미지의 url을 받아 S3에서 삭제하는 메소드
    public void deleteMenuImage(String fileUrl) {
        // 유효성 검사
        if (fileUrl == null || !fileUrl.contains(".com/")) {
            log.error("유효하지 않은 파일 URL: {}", fileUrl);
            return;
        }

        String fileName = fileUrl.substring(fileUrl.lastIndexOf(".com/") + 5); // ".com/" 길이 5

        try {
            // S3에서 파일 삭제 요청
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("S3에서 성공적으로 파일이 삭제되었습니다: {}", fileName);
        } catch (AmazonClientException e) {
            log.error("S3에서 파일을 삭제하지 못하였습니다.: {}", fileName, e);
        }
    }
}
