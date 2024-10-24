<template>
  <div class="result-view">
    <header>
      <ResultNav></ResultNav>
    </header>
    <main>
      <div class="result-container">
        <p v-show="!isLoading">요리보고가 추천하는 요리는!</p>
        <p v-show="isLoading">요리보고가 추천 중입니다...</p>
        <LoadingSpinner v-show="isLoading"></LoadingSpinner>
        <div class="result-info" v-show="!isLoading">
          <span :style="{ opacity: isFlipped ? 1 : 0 }">{{ menuName }}</span>
        </div>
        <div class="card-board-container" v-show="!isLoading">
          <ResultBoard
            :img="menuImage"
            :text="menuName"
            :recipeId="recipeId"
            @flipped="isFlipped = !isFlipped"
          >
          </ResultBoard>
        </div>
        <!-- 로그인된 사용자만 ResponseBoard가 보이도록 설정 -->
        <div class="response-board-container" v-show="!isLoading && isLoggedIn">
          <ResponseBoard
            :class="{ flipped: isFlipped }"
            @good="postRecommendData('GOOD')"
            @bad="postRecommendData('BAD')"
          ></ResponseBoard>
        </div>
      </div>
    </main>
    <aside>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </aside>
  </div>
</template>

<script setup>
import ResultNav from '../components/recommend/ResultNav.vue'
import ResultBoard from '../components/recommend/ResultBoard.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import ResponseBoard from '@/components/recommend/ResponseBoard.vue'
import { useTokenStore } from '@/stores/tokenStore'

const router = useRouter()

const isFlipped = ref(false)
const isLoading = ref(true)

// Pinia 스토어에서 토큰 정보를 가져와서 로그인 상태를 확인
const tokenStore = useTokenStore()
const isLoggedIn = ref(false)

const menuName = ref('')
const menuImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)
const recipeId = ref(1)

const fetchRecommendedMenu = async () => {
  let requestData

  try {
    const questionResponse = JSON.parse(localStorage.getItem('question_responses'))

    if (questionResponse) {
      requestData = {
        first: questionResponse.question_1,
        second: questionResponse.question_2,
        third: questionResponse.question_3,
        fourth: questionResponse.question_4,
        fifth: questionResponse.question_5
      }
    } else {
      throw new Error('세션이 만료되었습니다.')
    }

    const response = (await axios.post('/boot/api/recipes/recommend', requestData)).data

    if (response.success) {
      menuName.value = response.data.menu_name
      if (response.data.menu_image) menuImage.value = response.data.menu_image
      recipeId.value = response.data.recipe_id
      isLoading.value = false
      localStorage.removeItem('question_responses')
    } else {
      throw new Error('잘못된 요청 사항이거나 답변하지 않은 문항이 있습니다.')
    }
  } catch (error) {
    console.error('오류 발생:', error)
    alert(error.message)
    router.push('/question/1')
  }
}

const postRecommendData = async (satisfaction) => {
  try {
    const userId = JSON.parse(localStorage.getItem('token')).userId
    await axios.post('/boot/api/recommended-menus', {
      satisfaction: satisfaction,
      user_id: userId,
      recipe_id: recipeId.value
    })
  } catch (error) {
    console.error(error)
  } finally {
    alert('회원님의 소중한 의견 감사합니다!')
    router.push('/')
  }
}

onMounted(() => {
  // 토큰이 있는지 확인하여 로그인 여부 설정
  isLoggedIn.value = !!tokenStore.token.accessToken
  fetchRecommendedMenu()
})
</script>

<style scoped>
.result-view {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.profile-btn {
  position: absolute;
  top: 14rem;
  right: 12rem;
}

.home-btn {
  position: absolute;
  top: 14rem;
  right: 20rem;
}

main {
  justify-content: center;
  align-items: center;
}

.result-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 120rem;
  gap: 3rem;
  margin-top: 5rem;
  font-size: 4rem;
  font-weight: 700;
}

.result-container p {
  font-size: 3rem;
  font-weight: 700;
}

.result-info {
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 15rem; /* 최소 너비 설정 */
  padding: 1rem 2rem; /* 내부 여백 추가 */
  height: 8rem;
  background-color: var(--light-yellow-color);
  border-radius: 5rem;
}

.result-info span {
  transition: opacity 0.7s ease;
  white-space: nowrap;
}

.card-board-container {
  display: flex;
  flex-direction: row;
}

.response-board-container {
  margin-bottom: 4rem;
}

@media screen and (max-width: 768px) {
  .profile-btn {
    right: 6rem;
  }

  .home-btn {
    right: 14rem;
  }
}

@media screen and (max-width: 480px) {
  .profile-btn {
    top: 14rem;
    right: 3rem;
  }

  .home-btn {
    top: 22rem;
    right: 3rem;
  }
}
</style>
