# 목차

1. [팀 소개](#팀-소개)
2. [기술 스택](#기술-스택)
   - [Backend](#1-backend)
   - [Security](#2-security)
   - [Database](#3-database)
   - [Frontend](#4-frontend)
   - [DevOps](#5-devops)
   - [Tool](#6-tool)
3. [프로젝트 개요](#프로젝트-개요)
   - [프로젝트 소개](#프로젝트-소개)
   - [프로젝트 배경](#프로젝트-배경)
   - [유사 서비스와의 차별성](#유사-서비스와의-차별성)
4. [DDD](#ddd)
   - [Bounded Context](#bounded-context)
   - [연관 관계 매핑](#연관-관계-매핑)
5. [WBS](#wbs)
6. [시스템 아키텍쳐](#시스템-아키텍쳐)
7. [UI 설계](#ui-설계)
8. [데이터베이스 모델링](#데이터베이스-모델링)
   - [개념 모델링](#개념-모델링)
   - [논리 모델링](#논리-모델링)
   - [물리 모델링](#물리-모델링)
9. [JenkinsScript](#jenkins-script)
10. [결과 화면](#결과-화면)
11. [동료 평가](#동료-평가)

## Team _4vengers_<a id="팀-소개">

[<img src="https://img.shields.io/badge/Notion-Link-333333?logo=Notion">](https://www.notion.so/4vengers-1133c20995cb8086b316f83013f75823)
| <img src="https://github.com/user-attachments/assets/5a0d8aa8-0a1f-41f3-a93f-b2985c1765d6" height="100"/> | <img src="https://github.com/user-attachments/assets/79d48cf7-bdd4-4ec1-831c-bb57d5d9f57d" height="100"/> | <img src="https://github.com/user-attachments/assets/74133185-5305-4361-a9e0-52e76f67de3d" height="100"/> | <img src="https://github.com/user-attachments/assets/a17e5ef4-f7be-48fb-ae41-5cd3b27f5918" height="100"/> | <img src="https://github.com/user-attachments/assets/00ef7243-5ac8-4f3a-8b43-2271ee79665c" height="100"/> | <img src="https://github.com/user-attachments/assets/8381e8af-6a25-4f00-8a5e-454323d1f340" height="100"/> |
| ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| 박경희 | 윤채연 | 전기범 | 장민근 | 조창욱 | 최해관 | 
| [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/iwillbfine) | [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/yncayn) | [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/woodart8) | [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/caolian2003) | [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/Chochanguk) | [<img src="https://img.shields.io/badge/Github-Link-181717?logo=Github">](https://github.com/woodart8) |
| ISTP | ISFP | INFJ | INTP | ESTJ | ENTJ |

---

# 기술 스택 <a id="기술-스택">

### 1. Backend <a id="1-backend"></a>

| Java 17                                                                       | Spring Boot                                                                                              | Spring Data JPA                                                                                                 | Hibernate                                                                       | Gradle                                                                 | JUnit5                                                                                             | OpenAI                                                                                       | SSE                                                                                         |
| ----------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| ![Java](https://img.shields.io/badge/Java-17-007396.svg?&logo=java&color=red) | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F.svg?&logo=spring-boot&color=lightgreen) | ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F.svg?&logo=spring-data-JPA) | ![Hibernate](https://img.shields.io/badge/Hibernate-59666C.svg?&logo=hibernate) | ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?&logo=gradle) | ![JUnit5](https://img.shields.io/badge/JUnit5-25A162.svg?&logo=junit5&logoColor=white&color=green) | ![OpenAI](https://img.shields.io/badge/OpenAI-412991?style=flat&logo=openai&logoColor=white) | ![SSE](https://img.shields.io/badge/SSE-grey.svg) |

### 2. Security <a id="2-security"></a>

| Spring Security                                                                                                   | JWT                                                                                      | 카카오 로그인                                                                                    | 네이버 로그인                                                                                     |
| ----------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------- |
| ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F.svg?&logo=spring-security&logoColor=white) | ![JWT](https://img.shields.io/badge/JWT-F60055.svg?&logo=json-web-token&logoColor=white) | ![Kakao Login](https://img.shields.io/badge/Kakao_Login-FFCD00.svg?&logo=kakao&logoColor=black)   | ![Naver Login](https://img.shields.io/badge/Naver_Login-03C75A.svg?&logo=naver&logoColor=white)   |

### 3. Database <a id="3-database"></a>

| MariaDB                                                                                                   | MongoDB                                                                                    | Redis                                                                                    | Amazon S3 |
| ----------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------- |
| ![MariaDB](https://img.shields.io/badge/MariaDB-003545.svg?&logo=mariadb&logoColor=white) | ![MongoDB](https://img.shields.io/badge/MongoDB-47A248.svg?&logo=mongodb&logoColor=white) | ![Redis](https://img.shields.io/badge/Redis-FF4438.svg?&logo=redis&logoColor=white)   | ![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31.svg?&logo=amazons3&logoColor=white)   |

### 4. Frontend <a id="4-frontend"></a>

| Vue 3                                                                                     | JavaScript                                                                                      | HTML                                                                                            | CSS                                                                                            | EventSource                                                                                     |
| ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ |
| ![Vue 3](https://img.shields.io/badge/Vue_3-4FC08D.svg?&logo=vue.js&logoColor=white)       | ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E.svg?&logo=javascript&logoColor=black) | ![HTML](https://img.shields.io/badge/HTML-E34F26?logo=html5&logoColor=white)                     | ![CSS](https://img.shields.io/badge/CSS-1572B6?logo=css3&logoColor=white)                     | ![EventSource](https://img.shields.io/badge/EventSource-grey.svg) |

### 5. DevOps <a id="5-devops"></a>

| Docker                                                                                    | Jenkins                                                                                            |
| ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ |
| ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?&logo=docker&logoColor=white)       | ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=jenkins&logoColor=white)                     |

### 6. Tool <a id="6-tool"></a>

| GitHub                                                                                          | DA#                                                                                              | IntelliJ IDEA                                                                                          | Visual Studio Code                                                                                  |
| ---------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"> &nbsp; | <img src="https://img.shields.io/badge/DA%23-0B6121.svg?style=flat&logo=draw.io&logoColor=white"> &nbsp; | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=flat&logo=intellij-idea&logoColor=white) | ![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=flat&logo=visual-studio-code&logoColor=white) |

---

## 🍀 프로젝트 개요<a id="프로젝트-개요">
무엇을 먹을지 고민하는 우리에게 꼭 필요한 AI기반 레시피 추천 서비스입니다.

### 1. 프로젝트 소개<a id="프로젝트-소개">

**당신만을 위한 요리 레시피 보물창고**

✔️ 어떤 음식을 먹을지 항상 고민하시나요 ?

✔️ AI가 당신의 취향과 상황에 맞는 최적의 레시피를 추천해 드립니다 !

✔️ 공공데이터를 활용한 신뢰할 수 있는 레시피 정보를 제공합니다 !!

### 2. 프로젝트 배경<a id="프로젝트-배경">

안녕하세요, 여러분! 🍽️ '오늘 뭐 먹지?' 하면서 고민해본 적 있으시죠? 바로 이 고민에서 우리 프로젝트가 시작됐어요.

### 우리의 목표

- **일상 속 고민 해결** : 매일 반복되는 메뉴 고민, 지겹지 않으세요? 우리가 그 고민을 덜어드리고 싶었어요.
- **맞춤형 추천** : 같은 음식이라도 사람마다 좋아하는 맛이 다르잖아요. 여러분의 취향을 제대로 반영한 맞춤 추천을 해드리고 싶었답니다.
- **신뢰할 수 있는 정보** : 인터넷에는 레시피가 정말 많죠. 하지만 어떤 게 좋은 건지 헷갈리실 거예요. 우리는 여러분께 확실히 믿을 수 있는 정보를 드리고 싶었어요.

### 우리의 해결책

1. **공공데이터 활용** : 정부에서 제공하는 믿을 만한 공공데이터를 활용하기로 했어요.
2. **AI 기술 적용** : 최신 AI 기술로 여러분의 취향을 정확히 분석해 딱 맞는 레시피를 추천해드려요.

> AI와 공공데이터의 힘을 합쳐 여러분 한 분 한 분에게 꼭 맞는 레시피를 추천해주는 서비스를 만들었어요!

### 우리의 비전

- 여러분의 소소한 일상 고민을 해결하고, 더 즐거운 식사 시간을 만드는 데 도움을 드리고 싶어요.
- 매일의 식사가 새로운 즐거움이 되길 바라면서, 여러분의 주방에 작은 변화를 가져다 드리고 싶어요.

**우리의 서비스와 함께 새로운 요리에 도전해보는 건 어떨까요?** 🍳👨‍🍳👩‍🍳

### 3. 유사 서비스와의 차별성<a id="유사-서비스와의-차별성">

|         | AI 서비스 | 메뉴추천 | 커뮤니티 | 레시피제공 |
| ------- | -------- | -------- | --------- | -------- |
| 요리보고 | O       | O       | O          | O       |
| 제철밥상  | X        | X        | X        | O        |
| 만개의레시피  | X       | O       | O        | O       |
| 버터얌    | X       | O       | O        | O       |

---
   
# ✔️ DDD(Domain-Driven Design) <a id="ddd">

### 1. Bounded Context<a id="bounded-context">

![devops project - Step 5  Bounded Context](https://github.com/user-attachments/assets/8bc565a9-7a87-47f6-9759-1ce08e364dd3)

### 2. 연관 관계 매핑 <a id="연관-관계-매핑">
![image](https://github.com/user-attachments/assets/ca16faff-789f-4fa6-8a46-921636d6f3e3)

---

# ✔️ WBS <a id="wbs">
![wbs_devops](https://github.com/user-attachments/assets/57e8bd29-a323-4c89-986b-5d76fc694f07)

---

# ✔️ 시스템 아키텍쳐 <a id="시스템-아키텍쳐"> 
![image](https://github.com/user-attachments/assets/0fae1883-04a2-4243-bdba-f129682f936d)

---

# ✔️ UI설계 <a id="ui-설계">

<details>
  <summary>홈페이지 & 요리 추천</summary>

  ![질문 1](https://github.com/user-attachments/assets/ff026a5e-c7de-48d4-8697-e06c1567fc26)

  ![질문 2](https://github.com/user-attachments/assets/c1ae70a9-ec9b-4c10-a450-9df17ec21d89)

</details>

<details>
  <summary>로그인 & 회원가입</summary>

  ![로그인 & 회원가입](https://github.com/user-attachments/assets/69a93975-1573-4252-abfc-2bd85618df8b)

</details>

<details>
  <summary>나만의 레시피 게시판</summary>

  ![나만의 레시피 게시판](https://github.com/user-attachments/assets/975fbc26-b7ac-4966-88f1-ad53c1e6a2bc)

</details>

<details>
  <summary>문의 게시판</summary>

  ![문의 게시판](https://github.com/user-attachments/assets/e9833b51-96fd-406f-b7ed-c45a425a4cfd)

</details>

<details>
  <summary>마이페이지</summary>

  ![마이페이지](https://github.com/user-attachments/assets/1ea2a0a7-b6be-4d91-a133-2df95126e6e0)

</details>

---

# ✔️ 데이터베이스 모델링<a id="데이터베이스-모델링">
### 1. 개념 모델링<a id="개념-모델링">
![image](https://github.com/user-attachments/assets/7d9fa206-fdac-4a4b-b707-ba8545c19475)

<br>

### 2. 논리 모델링<a id="논리-모델링">
![논리모델링](https://github.com/user-attachments/assets/6adb2f33-b29b-469a-855f-433e92d39fb8)

<br>

### 3. 물리 모델링<a id="물리-모델링">
![물리모델링](https://github.com/user-attachments/assets/c04e7d8c-dc5f-4593-aab6-30b65c17e564)

---

# ✔️ JenkinsScript<a id="jenkins-script">

[Jenkins 백엔드 CI/CD 깃허브](https://github.com/hanwha-be09-4vengers/Yoribogo-Server)
```groovy
pipeline {
    agent any

    tools {
        gradle 'gradle'
        jdk 'openJDK17'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('DOCKERHUB_PASSWORD')
        DOCKERHUB_USERNAME = 'chochanguk'
        GITHUB_URL = 'https://github.com/hanwha-be09-4vengers/Yoribogo-Server.git'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker --version'
                    } else {
                        bat 'docker --version'
                    }
                }
            }
        }
        stage('Source Build') {
            steps {
                git branch: 'main', url: "${env.GITHUB_URL}"
                script {
                    if (isUnix()) {
                        sh "chmod +x ./gradlew"
                        sh "./gradlew clean build"
                    } else {
                        bat "gradlew.bat clean build"
                    }
                }
            }
        }
        stage('Container Build and Push') {
            steps {    
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        if (isUnix()) {
                            sh "cp ./build/libs/*.jar ."
                            sh "docker build -t ${DOCKERHUB_USERNAME}/yoribogo-pipe:latest ."
                            sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                            sh "docker push ${DOCKERHUB_USERNAME}/yoribogo-pipe:latest"
                        } else {
                            bat "copy .\\build\\libs\\*.jar ."
                            bat "docker build -t ${DOCKERHUB_USERNAME}/yoribogo-pipe:latest ."
                            bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                            bat "docker push ${DOCKERHUB_USERNAME}/yoribogo-pipe:latest"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh 'docker logout'
                } else {
                    bat 'docker logout'
                }
            }
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
```

---

# ✔️ 결과 화면<a id="결과-화면">

<h2>1. Jenkins CI/CD </h2>
  <details>
      <img src="https://github.com/user-attachments/assets/ea5e2a4d-892f-4b01-90de-dff5091c5a82" alt="">
  </details>
          
<h2>2. 회원</h2> 

<details>
  <summary>2.1 회원가입</summary>
  <br>
  2.1.1 이메일 인증번호 발송
  <img src="https://github.com/user-attachments/assets/653e1470-af64-4eea-a326-fb0e5bc3c189" alt="">
  <br><br>
  2.1.2 이메일 인증완료
  <img src="https://github.com/user-attachments/assets/cbd99245-546d-4282-9c14-181b226ca58a" alt="">
  <br><br>
  2.1.3 회원가입 완료
  <img src="https://github.com/user-attachments/assets/4d2ce3e3-f37e-4674-a15b-7b0268ba277a" alt="">
</details>

<details>
   <summary>2.2 일반 로그인</summary>
   <img src="https://github.com/user-attachments/assets/75915074-48ee-4e4c-b1d3-9653d42a19aa" alt="">
</details>

<details>
  <summary>2.3 관리자 로그인</summary>
   <img src="https://github.com/user-attachments/assets/d5b6551b-65f5-4b3c-81c1-fcbc53239f79" alt="">
</details>

<details>
  <summary>2.4 카카오 로그인</summary>
   <img src="https://github.com/user-attachments/assets/10681f95-3116-45d0-ae4b-1666a07d3c0c" alt="">
</details>

<details>
  <summary>2.5 네이버 로그인</summary>
  <img src="https://github.com/user-attachments/assets/6f1b0833-7ac4-4007-a81b-8fafc7b0111d" alt="">
</details>

<details>
  <summary>2.6 로그아웃</summary>
   <img src="https://github.com/user-attachments/assets/400373f2-7f70-4556-81c9-bfa2bbb26c5b" alt="">
</details>

<h2>3. 요리 위키</h2> 

<details>
  <summary>3.1 요리 위키</summary>
  <img src="https://github.com/user-attachments/assets/0faaaa1c-699f-4b97-979a-2b68e6adbc31" alt="">
</details>

<details>
   <summary>3.2 요리 위키 검색</summary>
   <img src="https://github.com/user-attachments/assets/86658262-dd3e-41f9-a221-edfc49fa92eb" alt="">
</details>

<details>
  <summary>3.3 요리 위키 페이지네이션</summary>
   <img src="https://github.com/user-attachments/assets/c47d6bec-51c3-4576-8689-451871236c33" alt="">
</details>

<details>
  <summary>3.4 요리 위키 상세정보</summary>
   <img src="https://github.com/user-attachments/assets/41fc845d-9a42-4ed0-8dbd-d169c953ab27" alt="">
</details>

<h2>4. 요리보고의 추천</h2> 

<details>
  <summary>4.1 요리 추천</summary>
  <img src="https://github.com/user-attachments/assets/0d058a6f-0854-4bda-abbb-c01b40eeda02" alt="">
</details>

<details>
   <summary>4.2 요리 이미지 생성 비동기 처리</summary>
   <img src="https://github.com/user-attachments/assets/223c8e93-df85-4d9e-ac72-ebf54aed35e5" alt="">
</details>

<details>
   <summary>4.3 요리 레시피 생성 비동기 처리</summary>
   <img src="https://github.com/user-attachments/assets/346b8687-0a67-41c9-a7a1-ca28b38945fe" alt="">
</details>

<details>
  <summary>4.4 요리 추천에 만족한 경우</summary>
   <img src="https://github.com/user-attachments/assets/7d8af233-fbd8-4652-bb7a-9c425f0a429b" alt="">
</details>

<details>
  <summary>4.5 요리 추천에 불만족한 경우</summary>
   <img src="https://github.com/user-attachments/assets/5a2f5215-a177-4b86-ae16-d10c01d9ae62" alt="">
</details>

<h2>5. 나만의 레시피</h2> 

<details>
  <summary>5.1 나만의 레시피</summary>
  <img src="https://github.com/user-attachments/assets/bf6c2091-8d1d-4cb2-b1d9-7fb61f6f9f49" alt="">
</details>

<details>
   <summary>5.2 나만의 레시피 검색</summary>
   <img src="https://github.com/user-attachments/assets/73194181-2668-422f-a67d-b4e7566c63b0" alt="">
</details>

<details>
  <summary>5.3 나만의 레시피 페이지네이션</summary>
  <img src="https://github.com/user-attachments/assets/1a9aa467-fc74-4ab6-8a68-6089d3e24b6a" alt="">
</details>

<details>
  <summary>5.4 나만의 레시피 상세정보</summary>
  <img src="https://github.com/user-attachments/assets/3dcfad10-0b0c-46ad-ae83-2b3a1fd94245" alt="">
</details>

<details>
  <summary>5.5 나만의 레시피 작성</summary>
  <img src="https://github.com/user-attachments/assets/baf33d5e-5ee2-4966-b2f8-4954c9b24786" alt="">
</details>

<details>
  <summary>5.6 나만의 레시피 댓글 작성</summary>
  <img src="https://github.com/user-attachments/assets/86a4c910-c755-48b4-a602-3717d154d407" alt="">
</details>

<h2>6. 마이페이지</h2> 

<details>
  <summary>6.1 프로필 수정</summary>
  <img src="https://github.com/user-attachments/assets/6076b590-f3eb-4584-a7b2-cf14edd673b2" alt="">
</details>

<details>
   <summary>6.2 비밀번호 변경</summary>
   <br>
   6.2.1 비밀번호 변경
   <img src="https://github.com/user-attachments/assets/a10ab92e-2b0c-4642-86a3-6427c32189fa" alt="">
   <br><br>
   6.2.2 변경된 비밀번호로 로그인
   <img src="https://github.com/user-attachments/assets/c364b89c-0948-462a-b724-b3f0b191e689" alt="">
</details>

<details>
  <summary>6.3 회원탈퇴</summary>
  <img src="https://github.com/user-attachments/assets/ac3110c6-23ac-4115-acb5-64dfe070ea92" alt="">
</details>

<h2>7. Q&A</h2> 

<details>
  <summary>7.1 Q&A</summary>
  <img src="https://github.com/user-attachments/assets/6bdb4bb1-69d5-479a-bb2f-acacdab2d55f" alt="">
</details>

<details>
   <summary>7.2 문의 작성</summary>
   <img src="https://github.com/user-attachments/assets/33afefeb-344f-44fb-a285-5857be218a23" alt="">
</details>

<details>
  <summary>7.3 문의 수정</summary>
  <img src="https://github.com/user-attachments/assets/329bb93f-98c6-4db4-bc33-ec079557b08e" alt="">
</details>

<details>
  <summary>7.4 문의 삭제</summary>
  <img src="https://github.com/user-attachments/assets/4a082156-8c8b-44f1-b0bf-582cd3f98c4a" alt="">
</details>

<details>
  <summary>7.5 문의 검색</summary>
  <img src="https://github.com/user-attachments/assets/8df65b7a-12c3-48ff-9eef-1239ae636171" alt="">
</details>

<details>
  <summary>7.6 문의 상세정보 조회</summary>
  <br>
  7.6.1 문의 작성자이거나 관리자일 경우
  <img src="https://github.com/user-attachments/assets/b03e68cc-8eeb-4b1f-bb28-a5161f17bf01" alt="">
  <br><br>
  7.6.2 비밀글일 때 작성자나 관리자가 아닐 경우
  <img src="https://github.com/user-attachments/assets/0b944dca-f6bf-40ce-830d-e04b2d7a33a7" alt="">
</details>

<details>
  <summary>7.7 관리자 답변</summary>
  <br>
  7.7.1 관리자 답변
  <img src="https://github.com/user-attachments/assets/c219de34-526a-47da-9afd-c3846f9168b9" alt="">
  <br><br>
  7.7.2 답변완료 처리
  <img src="https://github.com/user-attachments/assets/d0d8a4e2-7b8a-4a4d-8824-e49f3745d777" alt="">
</details>

<h2>8. 알림</h2> 

<details>
  <summary>8.1 SSE 연결</summary>
  <img src="https://github.com/user-attachments/assets/68e251b7-9c57-4ce6-9cd4-84a3a1b65bc6" alt="">
</details>

<details>
  <summary>8.2 알림 확인</summary>
  <img src="https://github.com/user-attachments/assets/7b78add8-3fd7-4eca-84be-4757c3c27620" alt="">
</details>

---

# ✔️ 동료 평가<a id="동료-평가">


| Team Member | 박경희님의 동료평가                                                                                                                                                                                                                       |
| :---------: | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   윤채연    | 프로젝트 전체 디자인과 나만의 레시피 구현을 담당해 주셨습니다. 통일감 있고 완성도 높은 결과물을 만들어 주셔서 감사합니다. |
|   장민근    | 서비스 알림 기능을 담당해 주셨습니다. 알림 서비스를 성공적으로 제공할 수 있게 해 주셔서 감사합니다.  |
|   전기범    | 추천 서비스 기능을 담당해 주셨습니다. 유저의 취향에 맞춘 요리와 레시피 추천을 구현해 주셔서 감사합니다. |
|   조창욱    | 회원 관련 기능을 담당해 주셨습니다. 프로젝트의 회원 관련 기능과 보안 기능을 구현해 주셔서 감사합니다. |
|   최해관    | 나만의 레시피 기능을 담당해주셨습니다. 유저들이 정보를 공유할 수 있는 기능을 구현해 주셔서 감사합니다. |

| Team Member | 윤채연님의 동료평가                                                                                                                                                                                                                       |
| :---------: | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   박경희    | 문의 게시판 기능을 담당해주셨고 백과 프론트 모두 다 잘 구현해주셨습니다. 질문 사항들을 바로바로 물어봐주셔서 개발 서비스에 대해 더 깊이 이해할 수 있었습니다.  |
|   장민근    | SSE을 이용한 알림 기능과 레시피 좋아요 기능을 잘 구현해주셨습니다. 쉽지 않은 부분이었음에도 새벽까지 에러를 해결하시려는 모습이 인상깊었습니다. 제가 서비스 아키텍처에 대한 이해가 살짝 부족했는데 잘 설명해주셔서 덕분에 잘 이해할 수 있었습니다. |
|   전기범    | 개발 속도가 엄청 빠르셔서 놀랐습니다. 제가 많이 부족했는데 옆에서 잘 설명해주셔서 코드를 어떻게 봐야하는지 잘 이해할 수 있었고, 덕분에 프론트에 대해 많이 이해하고 갑니다. 마지막까지 제가 막힌 부분 도와주셔서 감사합니다 |
|   조창욱    | 팀장 역할을 훌륭하게 해주셨습니다. 모든 팀원들의 개발 상황을 계속 점검해주셔서 현재 개발 상황에 대해 잘 이해할 수 있었습니다. 저 뿐만 아니라 다른 팀원들의 코드도 같이 점검해주시고 새벽까지 팀원들의 에러를 해결해주시려고 많이 노력하셨습니다. 지저분하게 작성한 코드를 금방 이해하시고 문제점을 바로 지적해주셔서 빠른 파드백이 가능했습니다. 많이 부족한데 많이 알려주시고 도와주셔서 감사합니다.  |
|   최해관    | 게시글 등록 전반적인 기능을 잘 구현해주셨습니다. 제가 중간에 막혀서 넘겨 받으신 코드도 금방 이해하시고 수정해주셨습나다.  코드를 낄끔하게 잘 작성해주셔서 코드를 수월하게 추가할수 있었습니다.제가 이해하지 못한 부분에 대해서 천천히 다시 알려주셔서 감사합니다. |

| Team Member | 전기범님의 동료평가                                                                                                                                                                                                                                                                                                                                                                                                                            |
| :---------: | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   박경희    | 비밀글일 경우 처리하는 로직과 관리자가 답변했을 때 완료처리하는 로직을 잘 구현해주셨습니다. 감사합니다. |
|   윤채연    | 디자인은 일단 완벽하고, 게시판 기능을 풀스택으로 처음 개발해보는 것이었음에도 댓글부분을 잘 만들어주셨습니다. 고생하셨습니다. |
|   장민근    | 이번 프로젝트에서 가장 난이도 있었던 SSE 부분을 성공적으로 구현해주셨습니다. 감사합니다. |
|   조창욱    | 에이스답게 회원 기능을 디테일까지 살려서 완벽하게 구현해주셨습니다. 고생 많으셨습니다. |
|   최해관    | 나만의 레시피 기능을 컨디션이 안 좋았음에도 잘 만들어주셔서 감사합니다. |

| Team Member | 장민근님의 동료평가                                                                                                                                                                                                                       |
| :---------: | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   박경희    | 자기의 몫을 책임감있게 매듭짓는 팀원입니다. 프로젝트 진행에 필요한 요점만을 파악한 후 주변 팀원들의 큰 도움 없이도 자신이 맡은 완성도 있게 마무리 했습니다. 평소에 활발한 성격은 아니지만, 그와 반대로 프로젝트 진행 시에 추진력은 사뭇 다른 사람처럼 느껴졌습니다. 어떤 일이든 자신이 맡아서 처리할 수 있다는 자신감이 가장 인상깊었습니다. |
|   윤채연    | 디자인에 강점이 있으신 분입니다. 프로젝트 주제가 선정 되자마자 빠르게 디자인 초안을 만드셔서 이를 기반으로 더 편안하고 빠른 개발이 가능했었습니다. 이번 프로젝트의 모든 디자인을 도맡아서 진행하였고, 완성도 있게 마무리 하여 정말 책임감 있는 모습을 보였다 생각합니다. 이에 더하여 게시판 구현에도 기여를 하는 등, 이번 프로젝트에서 많은 일을 담당하였습니다. |
|   전기범    | 기본적으로 알고리즘 구현력에 상당한 강점을 보인 팀원입니다. 프로젝트 주제 선정 이후 AI 도입에 대해 논의를 하였는데 먼저 자진하여 개발 난이도도 높으며 만들어야할 API의 양도 많은 부분을 맡았습니다. 이는 백엔드에서만 국한된 것이 아니라 프론트에서도 많은 페이지를 미리 만들어두어서 팀원들이 쉽게 프론트 개발에 접근할 수 있도록 가이드라인을 잡아주기도 했습니다. 개인의 시험 일정과 겹쳐 많은 시간을 할애하지 못했음에도, 가장 먼저 개발을 끝내고 팀원들을 도와주는 모습이 가장 인상깊었습니다.|
|   조창욱    |  자바, 스프링, 시큐리티, CS지식, 트러블 슈팅, 팀 리딩 능력 등 뭐 하나 빠지는게 없는 우리 팀의 명실상부 에이스 팀원입니다. 창욱님 또한 본인의 일정과 겹쳐 프로젝트에 많은 시간을 투자할 수 없었으나, 기범님과 마찬가지로 엄청나게 빠른 개발 속도로 본인이 맡은 일을 단기간에 끝낸 점이 가장 인상깊었습니다. 단순 CURD가 아니라 프로젝트에서 가장 어려운 파트였던 Security 부분이었다는 점을 생각해보면, 실력이 얼마나 뛰어난지 굳이 말을 안 해도 모두가 알 것이라 생각합니다. 또한 본인이 프로젝트 진행 시 시큐리티 적용 이후 CORS 에러로 많은 어려움을 겪고 있었는데, 자기  10시간 가량을 에러 해결에 도움을 줬던 점 또한 인상깊었습니다. |
|   최해관    | 매사에 긍정적이고 활발한 모습이 많이 기억에 남는 팀원입니다. 팀원과의 커뮤니케이션 또한 굉장히 활발합니다. 이번 프로젝트에서는 커뮤니티를 담당했는데, 커뮤니티 로직 자체가 여러 도메인과 엮여있고 S3까지 사용해서 구현하기가 굉장히 어려웠을텐데 마지막까지 포기하지 않고 끝끝내 완주하시는 모습이 정말 인상깊었습니다. 졸려도 새벽까지 참고 끝까지 개발을 할 정도로 의지가 대단합니다. |

| Team Member | 조창욱님의 동료평가                                                                                                                                                                                                                       |
| :---------: | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   박경희    | 경희님께서는 제가 맡긴 업무는 반드시 하셨고, 프로젝트 진행 중에 크리티컬 이슈 발생시 빠르고, 정확하게 말씀하셔서 덕분에 프로젝트를 진행할 수 있었던 것 같습니다. 감사합니다. |
|   윤채연    | 채연님은 저희 팀에서 피그마를 통한 디자인을 담당하셨습니다. 전공지식인 UI/UX를 살리셔서 디자인을 꼼꼼하게 만드셨고, 덕분에 프로젝트 개발이 빨랐습니다. 자신이 맡은 파트를 반드시 하고자 하는 의자가 강하셨고, 책임감이 강한 동료라는 인식을 크게 받았습니다. 덕분에 프로젝트 잘 마무리 했습니다. 감사합니다.  |
|   전기범    | 저희 서비스의 메인 기능인 OpenAI API와 공공데이터를 이용한 메뉴 레시피 추천 로직을 담당하셨습니다. 공공데이터는 직접 데이터를 파싱하시고, 이를 db에 저장함으로써 개발의 효율성을 극대화 하셨습니다. 이 뿐만 아니라 AI 요리 추천 로직에 Dall-E를 통한 요리 이미지 생성 기능을 도입해 사용자들에게 요리 이미지를 제공함으로써 사용자 경험을 개선하였고, 프롬프트 유효성 검사를 도입해 크리티컬 에러를 예방하셨습니다. 저희 팀이 개발하는데 필요한 공통 컴포넌트를 솔선수범하여 미리 만들어 주셨고 어려움을 겪는 팀원들을 위해 열심히 일하셨습니다. 정말 감사합니다. |
|   장민근    | 민근님은 처음하시는 sse 로직 개발을 맡으셨습니다. 처음하시는데도 불구하고, 맡은 업무에 대해 끝까지 파고들며, 잘 안되는 부분도 개발자 포럼 사이트를 참고하며 꼼꼼하게 개발하셨습니다. 덕분에 저희 기능중에 가장 어려운 알림 기능을 성공적으로 수행할 수 있었습니다. 감사합니다. |
|   최해관    | 해관님께서는 제가 맡긴 업무인 공공데이터 조사와 공공데이터 파싱을 파이썬 스크립트로 짜셔서 덕분에 기범님이 공공데이터를 db에 잘 저장할 수 있었습니다. 그 외에도 프로젝트의 핵심 주제 아이디어나 기타 신박한 아이디어를 제시하셔서 프로젝트의 길을 틀 수 있었습니다. 정말 감사합니다. |



| Team Member | 최해관님의 동료평가                                                                                                                                                                                                                       |
| :---------: | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|   박경희    | 묵묵히 맡은 바 일을 책임있게 끝까지 완수하시는 모습을 보고 배우는 점이 많았습니다. 또 회의 시에 아무도 생각 못했던 허점을 찾아내주셔서 프로젝트 방향을 올바르게 수정할 수 있었습니다. 저도 그 모습 본받아서 최종 프로젝트 때까지 함께 열심히 달려가보겠습니다! |
|   윤채연    | 전공을 잘 살리셔서 우리 팀의 전체적인 디자인의 틀을 잡아주시고 덕분에 개발이 애자일하게 잘 진행된 것 같습니다. 항상 제일 까다로운 부분이 ui/ux적 측면에서 쉽게 다가갈 수 있는 디자인을 고려하는 것이었는데 피그마를 통해 뚝딱뚝딱 엄청난 디자인이 만들어지는 것을 보고 감탄이 나왔습니다. 저도 본받아서 사용자 친화적인 디자인을 만들기 위해 노력하겠습니다! |
|   전기범    | 프론트와 백 전반에 걸쳐 기범님의 지식이 없었다면 개발이 성공적으로 진행되지 못했을 것 같습니다. 중요한 시험과 일정이 겹치셨는데도 팀을 위에 전체적인 세팅부터 많은 양의 개발을 도맡아 진행하셨습니다. 어깨에 너무 많은 짐을 지워드린 것 같아 죄송한 마음입니다. 서버, 클라이언트 간의 통신에 대한 이론이 해박한 모습을 보고 더 열심히 공부해야겠다고 생각했습니다! 고생하셨습니다. |
|   장민근    | 이번 프로젝트의 제일 까다로운 부분이었던 sse 를 구현하시느라 시작부터 백엔드와 프론트엔드 작업을 동시에 소화하시면서 NoSQL 연동까지 진행하느라 많이 힘드셨을 것 같습니다. 그럼에도 항상 믿음직하게 묵묵히 개발하는 모습과 그 개발을 위해 새로운 공부를 진행하는 모습이 감명 깊었습니다. 저도 공부 열심히 해서 새로운 기술에 겁먹지 않는 사람이 되겠습니다. |
|   조창욱    | 제가 개발 도중 모르는 것이 생길 때마다 질문을 많이 했는데도 웃으며 즐겁게 받아주셔서 감사합니다. 워낙 개발 과정 전반에 대한 지식이 많으신 것도 있지만 그에 걸맞는 설명 능력이 뛰어나셔서 쉽게 이해할 수 있었습니다. 회원이라는 큰 도메인을 맡아 본인도 많이 바쁘셨을텐데도 어려움을 겪고 있는 팀원이 있으면 몸소 나서 해결해주시고 모르는 부분을 확실히 알려주셔서 감사합니다. 저도 열심히 공부해서 누군가에게 알려줄 수 있는 사람이 되겠습니다. 정말 고생 많으셨습니다! |

