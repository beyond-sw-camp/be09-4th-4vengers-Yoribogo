<template>
  <div class="write-recipe-container">
    <button class="write-recipe-btn" @click="goWriteRecipe">
      <i class="fa-solid fa-pencil"></i>
    </button>

    <span class="tooltip-text">레시피 작성하기</span>
    <!-- 문구 추가 -->
  </div>
</template>

<script setup>
// import { useRouter } from 'vue-router'; // router 사용을 위해 import

// const router = useRouter(); // router 객체 초기화

// const goWriteRecipe = () => {
//     router.push('/write-recipe'); // 레시피 작성 페이지로 이동
// }
</script>

<style scoped>
.write-recipe-btn {
  background-color: var(--white-color);
  width: 7rem;
  height: 7rem;
  border-style: none;
  border-radius: 50%;
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
}

.tooltip-text {
  visibility: hidden;
  width: 100px;
  /* background-color: var(--red-color); */
  background-color: rgba(55, 65, 81);
  color: #fff;
  text-align: center;
  border-radius: 5px;
  padding: 5px;
  position: absolute;
  top: 8rem;
  right: 50%;
  transform: translateX(50%);
  z-index: 1;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 1.5rem;
}

.write-recipe-container:hover .tooltip-text {
  visibility: visible;
  opacity: 1;
}

.fa-solid {
  font-size: 3.5rem;
  color: var(--black-color);
}
</style>
