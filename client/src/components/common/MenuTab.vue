<template>
  <div class="menu-tab">
    <div class="text-wrapper">
      <i :class="props.iconClass"></i>
      <span><slot></slot></span>
    </div>
  </div>
</template>

<script setup>
// 아이콘 클래스를 받아오는 props 정의
const props = defineProps({
  iconClass: {
    type: String,
    required: true // 아이콘 클래스가 필수
  }
})
</script>

<style scoped>
.menu-tab {
  display: flex;
  justify-content: center;
  width: 19rem;
  height: 12rem;
  border-radius: 0.8rem;
  cursor: pointer;
}

.text-wrapper {
  font-size: 2.4rem;
  font-weight: 500;
  line-height: 4rem;
  color: var(--white-color);
  margin-top: 2.4rem;
}

.text-wrapper i {
  display: none;
}

.bg-pink {
  background-color: var(--pink-color);
}

.bg-red {
  background-color: var(--red-color);
}

.cur {
  z-index: 2;
}

@media screen and (max-width: 1367px) {
  .menu-tab {
    width: 16%;
  }

  .text-wrapper {
    font-size: 2.1rem;
  }
}

@media screen and (max-width: 1024px) {
  .menu-tab {
    width: 14%;
    height: 10rem;
  }

  .text-wrapper {
    font-size: 2.8rem;
    line-height: 3rem;
  }

  .text-wrapper i {
    display: inline;
  }

  .text-wrapper span {
    display: none;
  }
}
</style>
