<template>
  <div class="recipe-board-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'recipe-board'">
      <WriteRecipeBoardComponent> </WriteRecipeBoardComponent>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import WriteRecipeBoardComponent from '@/components/recipe-board/WriteRecipeBoardComponent.vue'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const isLogin = ref(false)
const router = useRouter()

onMounted(() => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  } else {
    alert('게시글을 작성하시려면 로그인이 필요합니다!')
    router.push('/login')
    return
  }
})
</script>

<style scoped>
.recipe-board-container {
  display: flex;
  align-items: center;
}

.recipe-board-view {
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

.write-recipe-container {
  position: absolute;
  bottom: 7rem;
  right: 8rem;
}

@media screen and (max-width: 480px) {
  .search-bar {
    width: 80%;
  }

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
