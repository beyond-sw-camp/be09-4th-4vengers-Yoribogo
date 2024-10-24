<template>
  <div class="qna-create-view">
    <header>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <input
            id="qna-title-input"
            type="text"
            class="t"
            v-model="title"
            placeholder="제목을 입력해주세요."
          />
        </div>
        <div class="L2">
          <textarea
            id="qna-content-input"
            class="c"
            v-model="content"
            placeholder="내용을 입력해주세요."
          ></textarea>
        </div>
        <div class="L3">
          <div class="vib">
            <label>
              <input id="personal-input" type="radio" value="PRIVATE" v-model="visibility" />
              비공개
            </label>
          </div>
          <button type="button" class="b" @click="push">등록</button>
        </div>
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'

import { ref, onMounted, toRaw } from 'vue'
import { useRouter } from 'vue-router'
import { addInquiry, getUserInfo } from '@/api/qna.js'
import { useTokenStore } from '@/stores/tokenStore.js'

const router = useRouter()
const tokenStore = useTokenStore()
const user = ref({})

const isLogin = ref(false)

onMounted(async () => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  } else {
    alert('문의 작성을 하시려면 로그인이 필요합니다!')
    router.push('/login')
    return
  }

  user.value = await getUserInfo(tokenStore.token.userId)
  user.value = user.value.data
})

const title = ref('')
const content = ref('')
const visibility = ref('PUBLIC')

const dto = ref({})

const push = async () => {
  dto.value = toRaw({
    inquiryTitle: title.value,
    inquiryContent: content.value,
    inquiryVisibility: visibility.value,
    user: toRaw(user.value)
  })

  const response = await addInquiry(toRaw(dto.value))
  router.push(`/qna/${response.data.inquiryId}`)
}
</script>

<style scoped>
.qna-create-view {
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

.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
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
.wrapper {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100vh;
  padding: 60px 60px 20px 60px;
  overflow: hidden;
  font-size: 2.4rem;
}
.t::placeholder {
  font-size: 6rem;
}
.L1 {
  display: flex;
  height: 20%;
  width: 100%;
  align-items: flex-end;
  border-bottom: 1px solid #bfc1c466;
}
.t {
  height: 7rem;
  width: 100%;
  font-size: 6rem;
  border: none;
  outline: none;
  margin: 10px;
}
.L2 {
  height: 60%;
}
.c {
  width: 100%;
  height: 100%;
  border: none;
  resize: none;
  outline: none;
  font-size: 2.4rem;
  padding: 10px;
  border-bottom: 1px solid #bfc1c466;
}
.L3 {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
  align-items: center;
  height: 20%;
  text-align: center;
  padding: 10px;
  column-gap: 10px;
}
.vib label {
  font-size: 2rem;
}
.vib {
  margin-bottom: 10px;
}
.b {
  height: 35px;
  width: 70px;
  font-size: 2.7rem;
  border: none;
  background-color: black;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}
</style>
