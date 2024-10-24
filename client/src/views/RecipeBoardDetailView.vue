<template>
  <div class="recipe-board-detail-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'recipe-board'">
      <BackButton></BackButton>
      <div class="menu-name-wrapper">
        <span class="menu-name">{{ menuInfo.menu_name }}</span>
      </div>
      <div class="menu-img-wrapper">
        <img v-show="isImageLoading" :src="defaultImage" alt="MenuImage" />
        <img
          v-show="!isImageLoading"
          :src="menuImageSrc"
          alt="MenuImage"
          @load="handleImageLoad"
          @error="handleImageError"
        />
        <div class="profile-container" style="justify-content: center; align-items: center">
          <div class="profile-image">
            <img
              :src="profileImageSrc"
              alt="Profile"
              style="width: 100px; height: 100px; border-radius: 50px"
            />
          </div>
          <div class="nickname-container">
            <div class="nickname">{{ writerProfiles.nickname }}</div>
            <div class="badge-wrapper">
              <img :src="badgeImageSrc" alt="배지 이미지" style="width: 20px; height: 20px" />
            </div>
          </div>
        </div>
      </div>

      <!-- 즐겨찾기 버튼
      <div class="favorite-button-container">
        <button @click="toggleFavorite" class="favorite-button">
          {{ isFavorited ? '즐겨찾기 취소' : '즐겨찾기' }}
        </button>
      </div> -->

      <div class="menu-ingredient-container">
        <div class="info">재료</div>
        <span class="menu-ingredient">{{ menuInfo.ingredients }}</span>
      </div>
      <div class="menu-manual-container">
        <div class="info">조리 순서</div>
        <!-- manuals 필드에서 매뉴얼 리스트를 바인딩 -->
        <RecipeManual
          v-for="manual in menuInfo.manuals"
          :key="manual.manual_id"
          :manualContent="manual.manual_content"
          :manualImage="manual.manual_image"
        ></RecipeManual>
      </div>

      <!-- 댓글 컴포넌트 -->
      <WriteCommentInput
        :comments="commentInfo"
        :userInfo="userProfiles"
        @add-comment="handleAddComment"
      >
      </WriteCommentInput>

      <!-- 대댓글 컴포넌트 -->
      <WriteRecommentInput>
        :recomments="recommentInfo" @add-recomment="hadleAddRecomment"
      </WriteRecommentInput>

      <!-- 댓글 입력 -->
      <form class="comment-input" @submit.prevent="submitComment">
        <input
          id="comment-text-input"
          v-model="newComment"
          placeholder="댓글을 입력하세요"
          style="width: 92%; border: 1px solid gray; height: 5rem; padding: 2rem"
          required
        />
        <button class="submit-btn" type="submit">등록</button>
      </form>
    </MainBoard>
    <aside>
      <GoTopButton></GoTopButton>
    </aside>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import BackButton from '@/components/common/BackButton.vue'
import RecipeManual from '@/components/recipe/RecipeManual.vue'
import GoTopButton from '@/components/common/GoTopButton.vue'
import WriteCommentInput from '@/components/recipe-board/WriteCommentInput.vue'
import WriteRecommentInput from '@/components/recipe-board/WriteRecommentInput.vue'
import axios from 'axios'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const isImageLoading = ref(true)
const isImageError = ref(false)
const isLogin = ref(false)

const route = useRoute()
const router = useRouter()

const menuInfo = ref({})
const menuImageSrc = ref('')
const commentInfo = ref([])
const userProfiles = ref([]) // 유저 프로필
const writerProfiles = ref({})
const profileImageSrc = ref('')
const badgeImageSrc = ref('')
const newComment = ref('')
// const isFavorited = ref(false)

const defaultImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)

const fetchData = async () => {
  try {
    commentInfo.value = []
    userProfiles.value = []
    const recipeResponse = (await axios.get(`/boot/api/recipe-board/detail/${route.params.board_id}`))
      .data
    if (recipeResponse.success) {
      menuInfo.value = recipeResponse.data
      menuImageSrc.value = menuInfo.value.board_image || defaultImage.value
      const writerResponse = (await axios.get(`/boot/api/users/${menuInfo.value.user_id}/profile`)).data
      if (writerResponse.success) {
        writerProfiles.value = writerResponse.data
        profileImageSrc.value = writerProfiles.value.profileImage || defaultImage.value
        badgeImageSrc.value = writerProfiles.value.tierImage || defaultImage.value
      }
    }

    // 댓글 정보 가져오기 (배열 형태로 바로 반환됨)
    const commentsResponse = (
      await axios.get(`/boot/api/recipe-board/${route.params.board_id}/comments`)
    ).data

    if (commentsResponse.success) {
      commentInfo.value = commentsResponse.data // 댓글 데이터를 저장
    }

    // forEach 대신 for...of 사용
    for (const comment of commentInfo.value) {
      try {
        const profileResponse = (await axios.get(`/boot/api/users/${comment['user_id']}/profile`)).data
        if (profileResponse.success) {
          // userProfiles 배열에 하나씩 추가
          userProfiles.value.push({
            nickname: profileResponse.data.nickname,
            profileImage: profileResponse.data.profileImage,
            tierImage: profileResponse.data.tierImage,
            userRole: profileResponse.data.userRole
          })
        } else {
          console.error('유저 정보 불러오기 실패')
        }
      } catch (error) {
        console.error('유저 정보 불러오는 중 에러 발생', error)
      }
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 새 댓글 추가 처리 함수
const handleAddComment = async (newComment) => {
  try {
    const userId = JSON.parse(localStorage.getItem('token')).userId
    const commentData = {
      comment_content: newComment,
      recipe_board_id: route.params.board_id,
      user_id: userId
    }

    // 서버로 새 댓글 전송
    const response = (
      await axios.post(`/boot/api/recipe-board/${route.params.board_id}/comments`, commentData)
    ).data

    // 새로운 댓글을 로컬에 추가
    if (response.success) {
      fetchData()
    }
  } catch (error) {
    console.error('Failed to add comment:', error)
  }
}

// 댓글 작성 함수
const submitComment = () => {
  if (newComment.value.trim() !== '') {
    handleAddComment(newComment.value)
    newComment.value = '' // 입력창 초기화
  }
}

// 새 대댓글 추가 함수

// 이미지 로딩 오류 처리 함수
const handleImageError = () => {
  menuImageSrc.value = defaultImage.value
  isImageLoading.value = false // 로딩 중지
  isImageError.value = true
}

// 이미지 로드 완료 처리 함수
const handleImageLoad = () => {
  isImageLoading.value = false // 로딩 중지
  isImageError.value = false
}

onMounted(() => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  } else {
    alert('마이페이지를 보시려면 로그인이 필요합니다!')
    router.push('/login-page')
    return
  }

  fetchData()
})
</script>

<style scoped>
.favorite-button {
}

.recipe-board-detail-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 20rem;
}

.back-btn {
  position: absolute;
  top: 14rem;
  left: 12rem;
}

.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
}

.go-top-btn {
  position: fixed;
  top: 84%;
  right: 8rem;
}

.menu-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 18rem;
}

.menu-name {
  font-size: 4rem;
  font-weight: 500;
  color: var(--black-color);
}

.menu-img-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 72rem;
  height: 54rem;
  margin-top: 4rem;
  border-radius: 1.4rem;
}

.menu-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.menu-ingredient-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 4rem;
  margin-top: 18rem;
}

.menu-ingredient {
  text-align: center;
  width: 68%;
  font-size: 2.4rem;
  font-weight: 500;
  line-height: 4rem;
  word-wrap: break-word;
}

.menu-manual-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 4rem;
  margin-top: 4rem;
  margin-bottom: 12rem;
}

.info {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 2rem;
  font-size: 2rem;
  font-weight: 350;
  color: var(--white-color);
  background-color: var(--black-color);
  border-radius: 3rem;
}

.profile-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  bottom: -13rem;
}

/* 작성자 프로필 */
.profile-image {
  width: 15rem;
  height: 15rem;
  background-color: white;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

/* 이미지 스타일 */
.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지가 원 안에 꽉 차게 만듦 */
  border-radius: 50%; /* 이미지를 원형으로 만듦 */
}

.nickname-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nickname {
  font-size: 3rem;
}

.badge-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 즐겨찾기 버튼 */
.favorite-button-container button {
  border-style: none;
  padding: 1rem 2rem;
  background-color: var(--pink-color);
  color: white;
  position: absolute;
  top: 19rem;
  right: 10rem;
  border-radius: 3px;
}

/* 댓글 css */
/* 답글 버튼 */
.reply-btn {
  color: #6c6c6c;
  border-style: none;
}

.comment-container {
  background-color: #fff5f5;
  width: 500px;
}

.comment-input {
  width: 80%;
  margin-top: 3rem;
  padding: 0 0 3rem 0;
}

.submit-btn {
  height: 5rem;
  background-color: #3e3e3e;
  color: white;
  border-style: none;
  padding: 0 2rem;
}

@media screen and (max-width: 1440px) {
  .menu-img-wrapper {
    width: 68rem;
    height: 51rem;
  }

  .back-btn {
    left: 8rem;
  }
}

@media screen and (max-width: 1280px) {
  .menu-img-wrapper {
    width: 52rem;
    height: 39rem;
  }

  .back-btn {
    left: 6rem;
  }
}

@media screen and (max-width: 1024px) {
  .go-top-btn {
    right: 5rem;
  }
}

@media screen and (max-width: 600px) {
  .menu-img-wrapper {
    width: 40rem;
    height: 30rem;
  }

  .back-btn {
    left: 5rem;
  }

  .go-top-btn {
    right: 4rem;
  }

  .menu-name {
    font-size: 3rem;
  }
}

@media screen and (max-width: 480px) {
  .notification-btn {
    left: 10rem;
  }

  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .menu-img-wrapper {
    width: 36rem;
    height: 27rem;
  }

  .notification-btn {
    left: 9rem;
  }

  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .notification-btn {
    left: 8rem;
  }

  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
</style>
