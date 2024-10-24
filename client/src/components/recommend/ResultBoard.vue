<template>
  <div class="result-board" :class="{ flipped: isFlipped }" @click="flipCard">
    <div class="front">
      <img :src="bgImg" alt="BackgroundImage" />
    </div>
    <div class="back">
      <img :src="props.img" alt="Image" />
      <span class="menu-name">{{ props.text }}</span>
      <button class="go-wiki-detail-btn" @click="goWikiDetail">
        <span>레시피 확인하러 가기</span>
        <i class="fa-solid fa-magnifying-glass"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  img: {
    type: String,
    required: true
  },
  text: {
    type: String,
    required: true
  },
  recipeId: {
    type: Number,
    required: true
  }
})

const router = useRouter()

const bgImg =
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728805407561_0d689efb-7da9-461f-987a-6996c392361d_%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC.svg'

const emit = defineEmits(['flipped'])

const isFlipped = ref(false)

const flipCard = () => {
  isFlipped.value = !isFlipped.value
  emit('flipped')
}

const goWikiDetail = () => {
  router.push(`/wiki/${props.recipeId}`)
}
</script>

<style scoped>
.result-board {
  position: relative;
  width: 60rem;
  height: 68rem;
  cursor: pointer;
}

.front,
.back {
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  box-shadow: 0.5rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  border-radius: 1.8rem;
  transition:
    transform 0.8s ease,
    box-shadow 0.2s ease;
  transform-style: preserve-3d;
}

.front {
  background: var(--white-color);
  transform: rotateY(0deg);
}

.front img {
  width: 80%;
}

.back {
  gap: 4rem;
  background-color: var(--white-color);
  transform: rotateY(180deg);
}

.back img {
  height: 22.6rem;
  border-radius: 1.4rem;
}

.result-board.flipped .front {
  transform: rotateY(180deg);
}

.result-board.flipped .back {
  transform: rotateY(360deg);
}

.front:hover,
.back:hover {
  box-shadow: 0.5rem 0.5rem 0.3rem 0.2rem rgba(60, 60, 60, 0.5);
}

.result-board .menu-name {
  max-width: 75%;
  word-break: break-word;
  text-align: center;
  font-size: 4rem;
}

.go-wiki-detail-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.6rem;
  background-color: transparent;
  border: none;
  font-size: 2rem;
  font-weight: 400;
  line-height: 2.5rem;
  color: var(--red-color);
  cursor: pointer;
}

.go-wiki-detail-btn i {
  font-size: 1.8rem;
}

/* 데스크탑 */
@media screen and (max-width: 1920px) {
  .result-board {
    width: 50rem;
    height: 56rem;
  }
}

/* 노트북 */
@media screen and (max-width: 1683px) {
  .result-board {
    width: 47rem;
    height: 53rem;
  }
}

/* 모바일 */
@media screen and (max-width: 767px) {
  .back img {
    height: 20rem;
  }
}

@media screen and (max-width: 560px) {
  .result-board {
    width: 42rem;
    height: 47rem;
  }

  .back img {
    height: 18rem;
  }

  .result-board .menu-name {
    font-size: 3.3rem;
  }

  .go-wiki-detail-btn {
    font-size: 1.65rem;
  }

  .go-wiki-detail-btn i {
    font-size: 1.52rem;
  }
}

@media screen and (max-width: 374px) {
  .result-board {
    width: 40rem;
    height: 45rem;
  }

  .back img {
    height: 16rem;
  }

  .result-board .menu-name {
    font-size: 3.2rem;
  }

  .go-wiki-detail-btn {
    font-size: 1.6rem;
  }

  .go-wiki-detail-btn i {
    font-size: 1.5rem;
  }
}
@media screen and (max-width: 320px) {
  .result-board {
    width: 36rem;
    height: 40rem;
  }

  .back img {
    height: 14rem;
  }

  .result-board .menu-name {
    font-size: 3rem;
  }

  .go-wiki-detail-btn {
    font-size: 1.5rem;
  }

  .go-wiki-detail-btn i {
    font-size: 1.45rem;
  }
}
</style>
