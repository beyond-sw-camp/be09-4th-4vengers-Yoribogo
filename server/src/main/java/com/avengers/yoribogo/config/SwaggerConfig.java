package com.avengers.yoribogo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

///* 설명. Swagger는  OpenAPI Specification(OAS)이다. */
///* 설명. http://localhost:8080/swagger-ui/index.html */
/*        위의 주소에서 자기가 작성한 api 호출 가능  */
@OpenAPIDefinition(
        info = @Info(title = "Yoribogo API 명세서",
                description = "Yoribogo API 명세서"))
@Configuration
public class SwaggerConfig {

}