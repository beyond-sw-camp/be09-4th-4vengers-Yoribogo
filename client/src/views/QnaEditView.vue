<template>
  <div class="qna-edit-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <input type="text" class="t" v-model="title" placeholder="제목을 입력해주세요." />
        </div>
        <div class="L2">
          <textarea class="c" v-model="content" placeholder="내용을 입력해주세요."></textarea>
        </div>
        <div class="L3">
          <div class="vib">
            <label>
              <input type="checkbox" value="PRIVATE" v-model="vib" />
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
import { useRoute, useRouter } from 'vue-router'
import { getUserInfo, modifyInquiry, getInquiry } from '@/api/qna.js'
import { useTokenStore } from '@/stores/tokenStore.js'

const route = useRoute()
const router = useRouter()
const tokenStore = useTokenStore()
const user = ref({})

const isLogin = ref(false)

const inquiry = ref({})
const title = ref('')
const content = ref('')
const vib = ref(false)
const visibility = ref('')

const dto = ref({})

onMounted(async () => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  } else {
    alert('문의를 수정하시려면 로그인이 필요합니다!')
    router.push('/login')
    return
  }

  user.value = await getUserInfo(tokenStore.token.userId)
  user.value = user.value.data

  inquiry.value = await getInquiry(route.params.num)
  inquiry.value = inquiry.value.data

  dto.value = {
    inquiryId: inquiry.value.inquiryId,
    inquiryStatus: inquiry.value.inquiryStatus,
    inquiryCreatedAt: inquiry.value.inquiryCreatedAt,
    answers: inquiry.value.answers,
    answerStatus: inquiry.value.answerStatus,
    user: toRaw(user.value)
  }

  title.value = inquiry.value.inquiryTitle
  content.value = inquiry.value.inquiryContent
  visibility.value = inquiry.value.inquiryVisibility

  if (visibility.value === 'PRIVATE') vib.value = true
})

const setDTO = () => {
  if (vib.value) visibility.value = 'PRIVATE'
  else visibility.value = 'PUBLIC'

  dto.value.inquiryTitle = title.value
  dto.value.inquiryContent = content.value
  dto.value.inquiryVisibility = visibility.value

  return dto.value
}

const push = async () => {
  const response = await modifyInquiry(toRaw(setDTO()))
  router.push(`/qna/${response.data.inquiryId}`)
}
</script>

<style scoped>
.qna-edit-view {
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
</style>
