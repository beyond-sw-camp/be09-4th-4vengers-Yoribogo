<template>
  <div class="result-nav">
    <div class="rounded-line"></div>
    <div class="menu">
      <div class="circle">
        <div class="mini-circle"></div>
      </div>
      <span>요리보고의 추천</span>
    </div>
  </div>
</template>

<style scoped>
.result-nav {
  display: flex;
  justify-content: center;
  align-items: center;
  width: calc(100% - 24rem);
  height: 100%;
  position: relative;
  z-index: 9999;
}

.rounded-line {
  position: absolute;
  width: 100%;
  height: 4px;
  background-color: var(--white-color);
  border-radius: 1rem;
  z-index: 0;
}

.menu {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1;
  gap: 1rem;
  margin-top: 3.7rem;
  font-size: 2rem;
  color: var(--sub-yellow-color);
}

.circle {
  display: flex;
  height: 3.5rem;
  width: 3.5rem;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: var(--white-color);
}

.mini-circle {
  display: flex;
  height: 2.1rem;
  width: 2.1rem;
  border-radius: 50%;
  background-color: var(--yellow-color);
}
</style>
