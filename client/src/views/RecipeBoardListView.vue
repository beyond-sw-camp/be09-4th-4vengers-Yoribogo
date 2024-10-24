<template>
  <div class="recipe-board-list-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'recipe-board'">
      <div class="recipe-board-container">
        <SearchBar @search="handleSearch"></SearchBar>

        <div class="not-found" v-if="menuList.length === 0">
          <span>게시글이 존재하지 않습니다.</span>
        </div>

        <!-- 전체 레시피 목록 -->
        <div v-if="menuList.length > 0" class="recipe-board-list">
          <MenuItem
            v-for="item in menuList"
            :key="item.board_id"
            :menuName="item.menu_name"
            :menuImage="item.board_image || ''"
            @click="goDetail(item.board_id)"
          ></MenuItem>
        </div>
        <WriteRecipeButton @click="goWrite()"></WriteRecipeButton>
      </div>
    </MainBoard>
    <PaginationComponent :data="pageInfo" @changePage="handlePageChange"></PaginationComponent>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import SearchBar from '@/components/common/SearchBar.vue'
import MenuItem from '@/components/recipe/MenuItem.vue'
import PaginationComponent from '@/components/common/PaginationComponent.vue'
import WriteRecipeButton from '@/components/recipe-board/WriteRecipeButton.vue'
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const menuList = ref([]) // 전체 게시글
const pageInfo = ref({})

const isLogin = ref(false)

// 게시글 목록 또는 검색 결과 가져오기
const fetchData = async (page, searchQuery = '') => {
  try {
    let response
    if (searchQuery) {
      // 검색어가 있는 경우 검색 API 호출
      response = await axios.get(
        `/api/recipe-board/search?recipeBoardMenuName=${searchQuery}&pageNo=${page}`
      )
    } else {
      // 전체 게시글 조회
      response = await axios.get(`/boot/api/recipe-board/boards?pageNo=${page}`)
    }

    if (response.data.success) {
      menuList.value = response.data.data.content
      pageInfo.value = response.data.data.page
    } else {
      menuList.value = []
      pageInfo.value = {}
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 검색 핸들러
const handleSearch = (searchQuery) => {
  router.push({ path: '/recipe-board', query: { q: searchQuery, page: 1 } })
}

// 페이지 변경 핸들러
const handlePageChange = (newPage) => {
  const searchQuery = route.query.q || ''
  searchQuery === ''
    ? router.push({ path: '/recipe-board', query: { page: newPage } })
    : router.push({ path: '/recipe-board', query: { q: searchQuery, page: newPage } })
}

// 게시글 클릭 시 디테일 페이지로 이동
const goDetail = (boardId) => {
  router.push(`/recipe-board/${boardId}`)
}

// 작성 페이지로 이동
const goWrite = () => {
  router.push(`/recipe-board/write`)
}

// URL 쿼리 변화를 감지하는 watcher
watch(
  () => route.query,
  (newQuery) => {
    const searchQuery = newQuery.q || ''
    const page = parseInt(newQuery.page) || 1
    fetchData(page, searchQuery)
  },
  { immediate: true }
)

onMounted(() => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  }
})
</script>

<style scoped>
.recipe-board-list-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.write-recipe-container {
  display: flex;
  width: 100%;
  justify-content: flex-end;
  margin-top: 4rem;
  margin-bottom: 4rem;
  margin-right: 8rem;
}

.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
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

.recipe-board-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.search-bar {
  margin-top: 8rem;
}

.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 4rem;
  font-size: 2.4rem;
  color: var(--black-color);
}

.recipe-board-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  width: 80%;
  margin-top: 6rem;
}

@media screen and (max-width: 960px) {
  .recipe-board-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 480px) {
  .recipe-board-list {
    grid-template-columns: 1fr;
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
