<template>
  <div class="question-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
      <QuestionNav :questions="questions"></QuestionNav>
    </header>
    <main>
      <div class="question-container">
        <div class="question-info">
          <span>{{ questions[qid - 1].label }}</span>
        </div>
        <div class="card-board-container" v-if="qid < 5">
          <ChoiceBoard
            :img="questions[qid - 1].leftImg"
            :text="questions[qid - 1].leftText"
            @click="goNext(questions[qid - 1].leftText, qid + 1)"
          >
          </ChoiceBoard>
          <ChoiceBoard
            :img="questions[qid - 1].rightImg"
            :text="questions[qid - 1].rightText"
            @click="goNext(questions[qid - 1].rightText, qid + 1)"
          >
          </ChoiceBoard>
        </div>
        <form class="input-container" v-if="qid === 5" @submit.prevent="goNext(null, qid + 1)">
          <input id="extra-input" type="text" placeholder="답변:" v-model="inputText" />
          <button class="submit-btn">추천 결과 보기</button>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'

import QuestionNav from '../components/recommend/QuestionNav.vue'
import ChoiceBoard from '../components/recommend/ChoiceBoard.vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()
const router = useRouter()

const qid = ref(parseInt(route.params.qid))

const inputText = ref('')

// 얘는 나중에 저희 s3쓰면 바뀔 예정
const sunnyImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808575580_c6554ceb-96c2-47a3-88b6-8622d067e0c6_%E1%84%82%E1%85%A1%E1%86%AF%E1%84%8A%E1%85%B5-%E1%84%8C%E1%85%A9%E1%87%82%E1%84%8B%E1%85%A1%E1%84%8B%E1%85%AD.png'
const cloudyImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808575569_fa440027-cbc6-4afa-b736-26e76f8d16d1_%E1%84%82%E1%85%A1%E1%86%AF%E1%84%8A%E1%85%B5-%E1%84%92%E1%85%B3%E1%84%85%E1%85%A7%E1%84%8B%E1%85%AD.png'
const feelGoodImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808575573_c2a3291e-ea1f-4d7c-892d-5a6543c7530b_%E1%84%80%E1%85%B5%E1%84%87%E1%85%AE%E1%86%AB-%E1%84%8C%E1%85%A9%E1%87%82%E1%84%8B%E1%85%A1%E1%84%8B%E1%85%AD.png'
const feelBadImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808546668_8a0acfdc-0dfd-48f8-a35d-5126dcbdff24_%E1%84%80%E1%85%B5%E1%84%87%E1%85%AE%E1%86%AB-%E1%84%87%E1%85%A7%E1%86%AF%E1%84%85%E1%85%A9.png'
const eatAloneImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808252339_fbd0243e-50d4-4e4b-9b74-7666763cb7eb_Group%20988418.png'
const eatTogetherImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808546672_f318980a-bb2c-4c1e-86f8-8a98eb5dabdd_%E1%84%8B%E1%85%A7%E1%84%85%E1%85%A7%E1%84%86%E1%85%A5%E1%86%BC%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A5%E1%86%A8%E1%84%8B%E1%85%A5%E1%84%8B%E1%85%AD.png'
const vegeImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808546686_c01ec2a8-29a2-434b-8252-236dbb9e67ef_%E1%84%87%E1%85%B5%E1%84%80%E1%85%A5%E1%86%AB.png'
const meatImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808546694_906a83e1-d7d5-4e79-bc28-aabb5c8c4693_%E1%84%80%E1%85%A9%E1%84%80%E1%85%B5.png'

watch(
  () => route.params.qid,
  (newQid) => {
    qid.value = parseInt(newQid)
  }
)

// 얘는 이제 db의 질문과 선지로 바뀔 예정
const questions = [
  {
    qid: 1,
    label: '오늘의 날씨는 어떤가요?',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 2,
    label: '오늘의 기분은 어떤가요?',
    leftImg: feelGoodImg,
    leftText: '좋아요!',
    rightImg: feelBadImg,
    rightText: '나빠요!'
  },
  {
    qid: 3,
    label: '몇 명이 먹는 음식인가요?',
    leftImg: eatAloneImg,
    leftText: '혼자 먹어요',
    rightImg: eatTogetherImg,
    rightText: '여러명이 먹어요'
  },
  {
    qid: 4,
    label: '채식 또는 비건 식단을 따르시나요?',
    leftImg: vegeImg,
    leftText: '네',
    rightImg: meatImg,
    rightText: '아니요'
  },
  {
    qid: 5,
    label: '제가 또 알아야 하는게 있을까요?'
  }
]

const goNext = (selectedText, nextQid) => {
  // localStorage에서 question_responses를 가져오고, 없으면 빈 객체로 초기화
  const responses = JSON.parse(localStorage.getItem('question_responses')) || {}

  // 현재 질문의 qid 값을 사용하여 응답 저장
  responses[`question_${qid.value}`] = qid.value < 5 ? selectedText : inputText.value

  // localStorage에 응답 저장
  localStorage.setItem('question_responses', JSON.stringify(responses))

  // 다음 질문으로 이동
  router.push(nextQid <= 5 ? `/question/${nextQid}` : `/question/result`)
}
</script>

<style scoped>
.question-view {
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

.question-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 120rem;
  margin-top: 11rem;
  font-size: 4rem;
  font-weight: 700;
}

.question-info {
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 50rem;
  padding: 1rem 3rem;
  height: 8rem;
  background-color: var(--white-color);
  border-radius: 5rem;
  color: var(--black-color);
}

.question-info span {
  white-space: nowrap;
}

.card-board-container {
  display: flex;
  flex-direction: row;
  gap: 4rem;
  margin-top: 5.4rem;
}

.input-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  gap: 6rem;
  margin-top: 16rem;
}

.input-container input {
  width: 59rem;
  height: 10rem;
  padding-left: 4rem;
  border: none;
  border-radius: 1.4rem;
  font-size: 3.8rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
}

.submit-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 34rem;
  height: 9rem;
  background-color: var(--light-yellow-color);
  border: none;
  border-radius: 1.4rem;
  box-shadow: 0.5rem 0.3rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  font-size: 3.5rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
  transition: 0.3s ease-out;
}

.submit-btn:hover {
  transform: translateY(-0.2rem);
  box-shadow: 0.5rem 0.3rem 0.3rem 0.1rem rgba(60, 60, 60, 0.5);
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
