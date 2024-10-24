<template>
  <div class="qna-view">
    <header>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <p @click="goHome">Q & A</p>
          <form class="search" @submit.prevent="searchText">
            <i class="fa-solid fa-magnifying-glass" @click="searchText"></i>
            <input
              id="search-qna-input"
              class="inputValue"
              type="search"
              @keyup.enter="searchText"
              v-model="sTxt"
              placeholder="문의사항 검색"
            />
          </form>
        </div>
        <div class="L2">
          <div class="p-container">
            <p @click="changeList($event.target.innerText)" class="active">전체</p>
            <p @click="changeList($event.target.innerText)">답변대기</p>
            <p @click="changeList($event.target.innerText)">답변완료</p>
          </div>
          <div class="create-btn">
            <button type="button" @click="goCreate">QnA 작성</button>
          </div>
        </div>
        <div class="L3">
          <div class="list" v-for="inquiry in pageInquiries" :key="inquiry.inquiryId">
            <a href="" @click.prevent="toInquiry(inquiry)">{{ inquiry.inquiryId }}</a>
            <div class="title">
              <a href="" @click.prevent="toInquiry(inquiry)">{{ inquiry.inquiryTitle }}</a>
              <img
                class="privateIcon"
                v-if="inquiry.inquiryVisibility == 'PRIVATE'"
                src="/src/assets/images/private_icon.png"
              />
            </div>
            <p>{{ inquiry.user.nickname }}</p>
            <p>{{ inquiry.inquiryCreatedAt }}</p>
            <div class="visibility1" v-if="inquiry.answerStatus == 'PENDING'">답변대기</div>
            <div class="visibility2" v-else>답변완료</div>
          </div>
        </div>
        <div class="L4">
          <button class="back" type="button" @click="goBack"></button>
          <div class="pageNums" v-for="num in nums.length" :key="num.code">
            <button type="button" @click="goPage(num)">{{ num }}</button>
          </div>
          <button class="next" type="button" @click="goNext">></button>
        </div>
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'

import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/tokenStore'
import { getInquiries, getUserInfo } from '@/api/qna.js'

const router = useRouter()
const tokenStore = useTokenStore()
const inquiry = ref([])
const list = ref([])
const filtd = ref([])
const user = ref({})
const sTxt = ref('')
const isLogin = ref(false)

onMounted(async () => {
  if (localStorage.getItem('token')) {
    isLogin.value = true
    user.value = await getUserInfo(tokenStore.token.userId)
    user.value = user.value.data
  }

  const response = await getInquiries()

  setinquiry(response)
  updateList(list.value)
  goPage(1)
})

const setNums = (lst) => {
  if (Math.floor(lst.length / 10) < 5) next.value = Math.ceil(lst.length / 10)
  nums.value = Array.from({ length: next.value - index.value }, (_, i) => i + index.value)
}

const updateList = (lst) => {
  setNums(lst)
  list.value = lst
  filtd.value = lst
}

const setinquiry = (data) => {
  for (let i = 0; i < data.data.length; i++)
    data.data[i].inquiryCreatedAt = data.data[i].inquiryCreatedAt.split('T')[0]
  if (data.data.length > 5) next.value = 5
  else next.value = data.data.length
  inquiry.value = data.data.reverse()
  list.value = inquiry.value
  filtd.value = inquiry.value
}

const nums = ref([])
const index = ref(0)
const next = ref(0)

const goBack = () => {
  if (index.value > 0) {
    if (next.value - index.value < 5) next.value -= next.value - index.value
    else next.value -= 5
    index.value -= 5
  }
  updateNums()
}

const goNext = () => {
  const totalNums = ref(Math.ceil(list.value.length / 10))
  if (next.value < totalNums.value) {
    if (totalNums.value - next.value < 5) next.value += totalNums.value - next.value + 1
    else next.value += 5
    index.value += 5
  }
  updateNums()
}

const updateNums = () => {
  nums.value = Array.from({ length: next.value - index.value }, (_, i) => i + index.value)
}

const pageInquiries = ref([])

const goPage = (num) => {
  pageInquiries.value = list.value.slice((num - 1) * 10, (num - 1) * 10 + 10)
}

const changeList = (elemt) => {
  const tags = document.querySelectorAll('.L2 p')
  for (let i = 0; i < tags.length; i++) {
    if (tags[i].innerText === elemt) {
      tags[i].classList.add('active')
    } else {
      tags[i].classList.remove('active')
    }
  }

  switch (elemt) {
    case '전체':
      list.value = filtd.value
      break
    case '답변완료':
      list.value = filtd.value.filter((ans) => ans.answerStatus === 'ANSWERED')
      break
    case '답변대기':
      list.value = filtd.value.filter((ans) => ans.answerStatus === 'PENDING')
      break
    default:
      break
  }
  setNums(list.value)
  goPage(1)
}

const searchText = () => {
  filtd.value = inquiry.value.filter((ans) => ans.inquiryTitle.includes(sTxt.value))
  updateList(filtd.value)
  changeList('전체')
}

const goHome = () => {
  router.go(0)
}

/* 문의 상세페이지로 이동 */
const toInquiry = (inquiry) => {
  if (
    inquiry.inquiryVisibility === 'PRIVATE' &&
    inquiry.user.userId !== user.value.user_id &&
    user.value.user_role !== 'ADMIN'
  ) {
    alert('비밀글은 작성자와 관리자만 볼 수 있습니다.')
  } else {
    router.push(`/qna/${inquiry.inquiryId}`)
  }
}

const goCreate = () => {
  router.push('/qna/new')
}
</script>

<style scoped>
.qna-view {
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

.wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80%;
  min-height: 60rem;
  font-size: 2.2rem;
  margin-top: 8rem;
}

.L1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.L1 p {
  cursor: pointer;
  font-size: 4rem;
  font-weight: 700;
  color: var(--black-color);
}

.search {
  display: grid;
  grid-template-columns: 15% 80%;
  align-items: center;
  margin-top: 1rem;
  width: 28rem;
  height: 5rem;
  border-radius: 1rem;
  border: 1px solid #aeb3bb;
  text-align: center;
  justify-content: center;
}

.search i {
  color: #aeb3bb;
}

input[type='search'] {
  align-items: center;
  border: none;
  outline: none;
  font-size: 2.2rem;
}

input[type='search']::placeholder {
  color: #aeb3bb;
  font-weight: 400;
  font-size: 2.2rem;
}

.L2 {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
  margin-top: 4rem;
  padding-bottom: 2rem;
  border-bottom: 0.05rem solid #bfc1c466;
}

.p-container {
  display: flex;
  justify-content: space-evely;
  color: #aeb3bb;
}

.p-container p {
  position: relative;
  font-size: 2.5rem;
  cursor: pointer;
  color: #aeb3bb;
  transition: color 0.3s ease;
}

.p-container p.active {
  color: var(--black-color);
  font-weight: bold;
}

.p-container p::after {
  content: '';
  position: absolute;
  bottom: -2.6rem;
  left: 0;
  width: 0;
  height: 0.4rem;
  background-color: var(--black-color);
}

.p-container p.active::after {
  width: 100%; /* 밑줄의 길이 */
}

.L2 div p {
  display: flex;
  width: 12rem;
  justify-content: center;
  cursor: pointer;
  font-size: 2.5rem;
}

.create-btn button {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 4.5rem;
  width: 12rem;
  border: none;
  color: var(--white-color);
  background-color: var(--navy-color);
  cursor: pointer;
  border-radius: 0.8rem;
  font-size: 2rem;
}

.L3 {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 2rem;
  margin-top: 2rem;
}

.list {
  display: grid;
  grid-template-columns: 0.5fr 5fr 2fr 2fr 1fr;
  justify-items: center;
  align-items: center;
  border-bottom: 0.5px solid #bfc1c466;
  padding-bottom: 2rem;
  font-size: 2rem;
}

.L3 .list a,
.L3 .list p {
  font-weight: 500;
  color: var(--black-color);
}

.title {
  display: flex;
  justify-self: start;
  align-items: center;
  padding-left: 1.5rem;
  gap: 1rem;
}

.privateIcon {
  width: 1.2rem;
  height: 1.4rem;
}

.visibility1,
.visibility2 {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 4rem;
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

.L4 {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  gap: 2rem;
  margin-top: 4rem;
  margin-bottom: 4rem;
}

.L4 button,
.L4 div,
.L4 div button {
  display: flex;
  background: none;
  border: none;
  color: var(--black-color);
  font-size: 3rem;
  cursor: pointer;
  height: 100%;
}

@media screen and (max-width: 768px) {
  .L2 div p {
    width: 10rem;
  }

  .list {
    font-size: 1.8rem;
  }

  .visibility1,
  .visibility2 {
    height: 3.2rem;
    width: 6.4rem;
    font-size: 1.2rem;
  }
}

@media screen and (max-width: 600px) {
  .search {
    width: 24rem;
    height: 5rem;
  }

  input[type='search'] {
    font-size: 2rem;
  }

  input[type='search']::placeholder {
    font-size: 2rem;
  }

  .list {
    font-size: 1.6rem;
  }

  .visibility1,
  .visibility2 {
    height: 3rem;
    width: 6rem;
    font-size: 1.1rem;
  }
}

@media screen and (max-width: 500px) {
  .L2 div p {
    width: 8rem;
    font-size: 2rem;
  }

  .create-btn button {
    height: 4rem;
    width: 10rem;
    font-size: 1.6rem;
  }

  .list {
    font-size: 1.5rem;
  }

  .visibility1,
  .visibility2 {
    height: 2.7rem;
    width: 5.4rem;
    font-size: 1rem;
  }
}

@media screen and (max-width: 480px) {
  .list {
    font-size: 1.4rem;
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
  .search {
    width: 22rem;
    height: 5rem;
  }

  input[type='search'] {
    font-size: 1.8rem;
  }

  input[type='search']::placeholder {
    font-size: 1.8rem;
  }

  .L2 div p {
    font-size: 1.8rem;
  }

  .create-btn button {
    height: 3.8rem;
    width: 8.4rem;
    font-size: 1.4rem;
  }

  .list {
    font-size: 1.2rem;
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
