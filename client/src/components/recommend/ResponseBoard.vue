<template>
  <div class="response-board">
    <div class="good" @click="handleClickGood">
      <i class="fa-solid fa-face-smile"></i>
      <div class="text-wrapper">
        <span>만족했어요</span>
      </div>
    </div>
    <div class="bad" @click="handleClickBad">
      <div class="text-wrapper">
        <span>별로예요</span>
      </div>
      <i class="fa-solid fa-face-frown"></i>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['good', 'bad'])

const handleClickGood = () => {
  emit('good')
}

const handleClickBad = () => {
  emit('bad')
}
</script>

<style scoped>
.response-board {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  border-radius: 5rem;
  background-color: var(--white-color);
  color: var(--white-color);
  box-shadow: 0rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  transition: 0.8s ease opacity;
  opacity: 0;
}

.response-board i {
  font-size: 3.9rem;
}

.response-board span {
  font-size: 2.6rem;
  font-weight: 500;
}

.response-board.flipped {
  transition-delay: 0.5s;
  opacity: 1;
}

.text-wrapper {
  display: flex;
  flex-grow: 1;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.good {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 28rem;
  height: 7rem;
  padding-left: 2rem;
  gap: 1rem;
  border-radius: 5rem 0 0 5rem;
  background-color: var(--semi-blue-color);
  box-shadow: 0.1rem 0.2rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  transition: 0.2s ease;
  cursor: pointer;
}

.bad {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 28rem;
  height: 7rem;
  padding-right: 2rem;
  gap: 1rem;
  border-radius: 0 5rem 5rem 0;
  background-color: var(--semi-red-color);
  box-shadow: -0.1rem 0.2rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  transition: 0.2s ease;
  cursor: pointer;
}

.good:hover {
  box-shadow: 0.1rem 0.2rem 0.3rem 0.1rem rgba(60, 60, 60, 0.5);
}

.bad:hover {
  box-shadow: -0.1rem 0.2rem 0.3rem 0.1rem rgba(60, 60, 60, 0.5);
}

/* 데스크탑 */
@media screen and (max-width: 1920px) {
  .good {
    width: 24rem;
  }

  .bad {
    width: 24rem;
  }
}

/* 노트북 */
@media screen and (max-width: 1683px) {
  .good {
    width: 22rem;
  }

  .bad {
    width: 22rem;
  }
}

/* 모바일 */
@media screen and (max-width: 560px) {
  .good {
    width: 20rem;
  }

  .bad {
    width: 20rem;
  }

  .response-board i {
    font-size: 3.6rem;
  }

  .response-board span {
    font-size: 2.4rem;
  }
}

@media screen and (max-width: 374px) {
  .good {
    width: 19rem;
  }

  .bad {
    width: 19rem;
  }

  .response-board i {
    font-size: 3.3rem;
  }

  .response-board span {
    font-size: 2.2rem;
  }
}
@media screen and (max-width: 320px) {
  .good {
    width: 17rem;
  }

  .bad {
    width: 17rem;
  }

  .response-board i {
    font-size: 3rem;
  }

  .response-board span {
    font-size: 2rem;
  }
}
</style>
