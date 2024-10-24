<template>
  <div class="qna-detail-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <div class="title">
            <h1 v-bind="inquiry">Q. {{ inquiry.inquiryTitle }}</h1>
            <img
              class="privateIcon"
              v-if="inquiry.inquiryVisibility === 'PRIVATE'"
              src="/src/assets/images/private_icon.png"
            />
            <div class="visibility1" v-if="inquiry.answerStatus == 'PENDING'">답변대기</div>
            <div class="visibility2" v-else>답변완료</div>
          </div>
          <div class="icons">
            <button class="kebab" type="button" @click="toggle" />
            <div class="menus" ref="menu">
              <a @click="edit">수정</a>
              <a @click="remove">삭제</a>
            </div>
          </div>
        </div>
        <div class="L2" v-bind="inquiry">
          <div class="inquiry-info">
            <img class="icon" src="@/assets/images/inquiry_icon.png" />
            <div class="info">
              <p>{{ inquiry.nickname }}</p>
              <p>{{ inquiry.inquiryCreatedAt }}</p>
            </div>
          </div>
          <div class="inquiry_content">
            <p>{{ inquiry.inquiryContent }}</p>
          </div>
          <div class="write">
            <p v-bind="inquiry">답변 {{ inquiry.answers }}개</p>
            <div class="group">
              <input id="answer-input" type="text" v-model="writeAns" class="inputVal" />
              <button type="button" @click="sendAnswer">등록</button>
            </div>
          </div>
          <div class="answers" v-for="ans in answers" :key="ans.answerId">
            <div class="admin" v-if="ans.writerType == 'ADMIN'">
              <div class="ans">
                <img class="icon" src="@/assets/images/answer_icon.png" />
                <div class="info1">
                  <p>관리자</p>
                  <p>{{ ans.answerCreatedAt }}</p>
                </div>
              </div>
              <div class="content1">
                <p class="answer-content">{{ ans.answerContent }}</p>
                <div v-if="ans.user.userId === user.user_id">
                  <p class="delete-btn" @click="delAnswer(ans)">x</p>
                </div>
              </div>
            </div>
            <div class="enterprise" v-else>
              <div class="ans">
                <img class="icon" src="@/assets/images/inquiry_icon.png" />
                <div class="info2">
                  <p>{{ ans.user.nickname }}</p>
                  <p>{{ ans.answerCreatedAt }}</p>
                </div>
              </div>
              <div class="content2">
                <p class="answer-content">{{ ans.answerContent }}</p>
                <div v-if="ans.user.userId === user.user_id">
                  <p class="delete-btn" @click="delAnswer(ans)">x</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'

import { ref, onMounted, toRaw } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAnswers, addAnswer, deleteAnswer, getUserInfo, deleteInquiry } from '@/api/qna.js'
import { useTokenStore } from '@/stores/tokenStore.js'

const route = useRoute()
const router = useRouter()
const tokenStore = useTokenStore()
const inquiry = ref({})
const answers = ref([])
const visible = ref()
const user = ref({})

const isLogin = ref(false)

onMounted(async () => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
  } else {
    alert('문의 내용을 보시려면 로그인이 필요합니다!')
    router.push('/login')
    return
  }

  const data = await getAnswers(route.params.num)

  user.value = await getUserInfo(tokenStore.token.userId)
  user.value = user.value.data

  setInquiry(data.data)
  setAnswer(data.data.answer)
  setDTO()

  visible.value = false
})

const setInquiry = (data) => {
  inquiry.value = data
  inquiry.value.nickname = data.user.nickname
  inquiry.value.inquiryCreatedAt = inquiry.value.inquiryCreatedAt.replace('T', ' ')
}

const setAnswer = (data) => {
  for (let i = 0; i < data.length; i++) {
    answers.value[i] = data[i]
    answers.value[i].nickname = data[i].user.nickname
    answers.value[i].answerCreatedAt = data[i].answerCreatedAt.replace('T', ' ')
  }
}

const toggle = () => {
  visible.value = !visible.value
  const menu = document.querySelector('.menus')
  menu.style.display = visible.value ? 'block' : 'none'
}

const writeAns = ref('')
const dto = ref({})

const setDTO = () => {
  dto.value = toRaw({
    answerContent: writeAns.value,
    writerType: user.value.user_role,
    inquiryId: inquiry.value.inquiryId,
    user: toRaw(user.value)
  })
}

const sendAnswer = async () => {
  if (inquiry.value.user.userId === user.value.user_id || user.value.user_role === 'ADMIN') {
    setDTO()
    await addAnswer(toRaw(dto.value))
    router.go(0)
  } else {
    alert('문의 작성자만 작성 가능합니다.')
    writeAns.value = ''
  }
}

const delAnswer = async (ans) => {
  await deleteAnswer(ans.answerId)
  router.go(0)
  alert('답변이 삭제되었습니다.')
}

// 수정 페이지로 이동
const edit = () => {
  if (inquiry.value.user.userId === user.value.user_id) {
    router.push(`/qna/edit/${inquiry.value.inquiryId}`)
  } else {
    alert('문의 작성자만 수정 가능합니다.')
    toggle()
  }
}

// 삭제 후 목록으로 이동
const remove = () => {
  if (inquiry.value.user.userId === user.value.user_id) {
    deleteInquiry(inquiry.value.inquiryId)
    alert('문의가 삭제되었습니다.')
    router.push('/qna')
  } else {
    alert('작성자만 삭제 가능합니다.')
    toggle()
  }
}
</script>

<style scoped>
.qna-detail-view {
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

.wrapper {
  display: flex;
  flex-direction: column;
  width: 80%;
  min-height: 60rem;
  margin-top: 8rem;
  overflow: hidden;
  font-size: 2.4rem;
}

.L1 {
  display: flex;
  height: 10%;
  align-items: flex-end;
}

.title {
  display: flex;
  align-items: center;
  width: 80%;
  margin-bottom: 2rem;
  gap: 2rem;
}

h1 {
  font-size: 5rem;
}

.privateIcon {
  height: 2rem;
  align-self: center;
}

.visibility1,
.visibility2 {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 3.6rem;
  width: 8rem;
}

.visibility1 {
  font-size: 1.2rem;
  border-radius: 2rem;
  background: #aaa;
  color: var(--white-color);
}

.visibility2 {
  font-size: 1.2rem;
  border-radius: 2rem;
  background: var(--pink-color);
  color: var(--white-color);
}

.icons {
  width: 20%;
  position: relative;
  display: inline-block;
  text-align: right;
  padding-bottom: 1%;
}

.kebab {
  width: 2rem; /* 아이콘 크기 조절 */
  height: 2rem;
  background-color: white;
  background-image: url('@/assets/images/kebab_menu_icon.png');
  border: none;
  outline: none;
  background-position: right;
  background-size: contain; /* 이미지를 버튼 크기에 맞춤 */
  background-repeat: no-repeat;
  background-position: center;
  cursor: pointer; /* 마우스 오버 시 클릭 가능한 상태로 */
}

.menus {
  display: none; /* 기본적으로 드롭다운 메뉴는 숨김 */
  position: absolute;
  top: 3.8rem; /* 아이콘 바로 아래에 드롭다운 메뉴가 나타나도록 설정 */
  right: 0;
  background-color: var(--white-color);
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  z-index: 1;
}

.menus a {
  color: var(--black-color);
  padding: 1.5rem 2rem;
  text-decoration: none;
  display: block;
  font-size: 2rem;
  cursor: pointer;
}

.menus a:hover {
  background-color: #f1f1f1; /* 마우스 오버 시 배경색 변경 */
}

.active .menus {
  display: block; /* 메뉴가 활성화되면 보이게 설정 */
}

.L2 {
  display: flex;
  flex-direction: column;
  border-top: 0.1rem solid #bfc1c466;
}

.inquiry-info {
  padding: 2rem 0 3% 1.2rem;
}
.inquiry_content {
  padding: 0 0 5% 1.2rem;
  border-bottom: 0.1rem solid #bfc1c466;
}
.write {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 3rem 0 3rem 0;
  gap: 2rem;
}

.group {
  width: 100%;
  display: grid;
  height: 4rem;
  grid-template-columns: 90% 10%;
  border: 0.5px solid #bfc1c466;
  justify-items: end;
}

.group input {
  width: 100%;
  border: none;
  outline: none;
  padding-left: 1.2rem;
}

.group button {
  justify-items: right;
  border: none;
  color: white;
  width: 7rem;
  background-color: var(--navy-color);
  cursor: pointer;
  font-size: 2rem;
}

.answers {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 0.2rem;
}

.admin,
.enterprise {
  display: grid;
  height: 9%;
  grid-template-rows: 1fr 2fr 0.5fr;
  padding: 1.2rem;
  margin-bottom: 1.2rem;
}

.admin {
  background-color: #fff5f5;
}
.ans,
.inquiry-info {
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  width: fit-content;
}
.info1,
.info2 {
  display: flex;
  gap: 1.2rem;
}
.info1 p:first-child,
.info2 p:first-child {
  font-weight: bold;
}

.content1,
.content2 {
  display: flex;
  justify-content: space-between;
  padding-left: 5rem;
  margin-top: 0.8rem;
}

.answer-content {
  display: flex;
  width: 70%;
  word-break: break-all;
}

.icon {
  width: 4rem;
  height: 4rem;
  margin: 0 1.2rem 0 0;
}

.delete-btn {
  color: rgb(75, 75, 75);
  cursor: pointer;
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
