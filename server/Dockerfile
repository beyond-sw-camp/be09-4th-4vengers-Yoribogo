## 1. 빌드 스테이지 시작
# gradle 이미지 가져옴(빌드 스테이지를 build라는 이름으로 설정)
FROM gradle:7.6.1-jdk17-alpine AS build

# 컨테이너 내부(alpine 리눅스 환경)의 /app 폴더로 설정 (실제 서버를 docker 이미지로 옮겨서 실행하는 작업)
WORKDIR /app

# Gradle 빌드에 필요한 설정 파일들을 복사
COPY build.gradle settings.gradle /app/

# 소스 파일 복사
COPY src /app/src

# application.yml 또는 application.properties 복사
COPY src/main/resources/application.yml /app/src/main/resources/application.yml


# gradle을 사용하여 프로젝트 빌드 (테스트는 실행하지 않음)
RUN gradle clean build -x test --no-daemon

## 2. 실행 스테이지 시작
# openjdk 17버전의 이미지를 가져와 JVM 환경 구축
FROM openjdk:17-alpine

# 빌드 스테이지에서 생성된 JAR 파일을 실행 스테이지로 복사
COPY --from=build /app/build/libs/*.jar ./app.jar

# app.jar 파일을 실행 (스프링 부트 서버 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]
