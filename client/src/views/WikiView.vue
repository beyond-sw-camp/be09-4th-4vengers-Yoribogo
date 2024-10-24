<template>
  <div class="wiki-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'wiki'">
      <div class="wiki-container">
        <SearchBar @search="handleSearch"></SearchBar>
        <div class="not-found" v-if="isEmpty">
          <span>요리가 존재하지 않습니다.</span>
        </div>
        <div class="wiki-list" v-if="!isEmpty">
          <MenuItem
            v-for="item in menuList"
            :key="item.recipe_id"
            :menuName="item.menu_name"
            :menuImage="item.menu_image || ''"
            @click="goDetail(item.recipe_id)"
          ></MenuItem>
        </div>
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
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router' // Vue Router 사용
import axios from 'axios'

const route = useRoute() // 현재 경로 정보 가져오기
const router = useRouter() // 라우터 인스턴스 가져오기

const menuList = ref([])
const pageInfo = ref({})

const isEmpty = ref(true)
const isLogin = ref(false)

const fetchData = async (name, page) => {
  try {
    let response
    name === ''
      ? (response = (await axios.get(`/boot/api/recipes?page=${page}`)).data)
      : (response = (await axios.get(`/boot/api/recipes/search?name=${name}&page=${page}`)).data)
    if (response.success) {
      menuList.value = response.data.content
      pageInfo.value = response.data.page
      isEmpty.value = false
    } else {
      menuList.value = []
      pageInfo.value = {}
      isEmpty.value = true
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 검색 핸들러
const handleSearch = (searchQuery) => {
  router.push({ path: '/wiki', query: { q: searchQuery, page: 1 } }) // 쿼리 파라미터 업데이트
}

// 페이지 변경 핸들러
const handlePageChange = (newPage) => {
  const name = route.query.q || ''
  name === ''
    ? router.push({ path: '/wiki', query: { page: newPage } })
    : router.push({ path: '/wiki', query: { q: name, page: newPage } }) // 페이지 변경 시 쿼리 업데이트
}

const goDetail = (recipeId) => {
  router.push(`/wiki/${recipeId}`) // 상세 페이지로 이동
}

// URL 쿼리 변화를 감지하는 watcher
watch(
  () => route.query,
  (newQuery) => {
    const name = newQuery.q || ''
    const page = parseInt(newQuery.page) || 1 // 기본값은 1
    fetchData(name, page)
  },
  { immediate: true } // 즉시 호출
)

onMounted(() => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  }
})
</script>

<style scoped>
.wiki-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
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

.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 4rem;
  font-size: 2.4rem;
  color: var(--black-color);
}

.wiki-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  align-items: center;
}

.search-bar {
  margin-top: 8rem;
}

.wiki-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  width: 80%;
  margin-top: 6rem;
  margin-bottom: 8rem;
}

@media screen and (max-width: 960px) {
  .wiki-list {
    grid-template-columns: repeat(2, 1fr);
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
