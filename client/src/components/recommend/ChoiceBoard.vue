<template>
  <div class="card-board">
    <img :src="props.img" alt="Image" />
    <span>{{ props.text }}</span>
  </div>
</template>

<script setup>
const props = defineProps({
  img: {
    type: String,
    required: true
  },
  text: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.card-board {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 44rem;
  height: 53rem;
  gap: 1.6rem;
  background-color: var(--white-color);
  box-shadow: 0.5rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  border-radius: 1.8rem;
  cursor: pointer;
  transition: 0.3s ease-out;
}

.card-board:hover {
  transform: translateY(-0.3rem);
  box-shadow: 0.5rem 0.5rem 0.3rem 0.2rem rgba(60, 60, 60, 0.5);
}

.card-board img {
  height: 22.6rem;
}

.card-board span {
  font-size: 3.5rem;
}

/* 노트북 */
@media screen and (max-width: 1683px) {
  .card-board {
    width: 37rem;
    height: 44rem;
  }

  .card-board img {
    height: 20rem;
  }

  .card-board span {
    font-size: 3.5rem;
  }
}

/* 모바일 */
@media screen and (max-width: 767px) {
  .card-board {
    width: 30rem;
    height: 36rem;
  }

  .card-board img {
    height: 16rem;
  }
}

@media screen and (max-width: 560px) {
  .card-board {
    width: 27rem;
    height: 32rem;
  }

  .card-board span {
    font-size: 3.1rem;
  }
}

@media screen and (max-width: 374px) {
  .card-board {
    width: 24rem;
    height: 28.5rem;
  }

  .card-board img {
    height: 14rem;
  }

  .card-board span {
    font-size: 2.8rem;
  }
}
@media screen and (max-width: 320px) {
  .card-board {
    width: 21rem;
    height: 25rem;
  }

  .card-board img {
    height: 12rem;
  }

  .card-board span {
    font-size: 2.5rem;
  }
}
</style>
