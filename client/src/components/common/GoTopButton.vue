<template>
  <button class="go-top-btn" @click="goTop">
    <i class="fa-solid fa-angles-up"></i>
  </button>
</template>

<script setup>
const goTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>
<style scoped>
.go-top-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6.3rem;
  height: 6.3rem;
  border: none;
  border-radius: 50%;
  background-color: var(--red-color);
  color: var(--white-color);
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
  z-index: 9999;
}

.go-top-btn i {
  font-size: 3rem;
}

.go-top-btn:hover {
  transition: 0.3s ease;
  box-shadow: 0rem 0.4rem 0.4rem 0.1rem rgba(60, 60, 60, 0.5);
}
</style>
