<template>
  <div class="recipe-container">
    <div class="not-found" v-if="props.isEmpty">
      <span>요리가 존재하지 않습니다.</span>
    </div>
    <div class="recipe-list" v-if="!props.isEmpty">
      <MenuItem
        v-for="item in props.menuList"
        :key="item.recipe_id"
        :menuName="item.menu_name"
        :menuImage="item.menu_image || ''"
        @click="goDetail(item.recipe_id)"
      ></MenuItem>
    </div>
  </div>
</template>

<script setup>
import MenuItem from '../recipe/MenuItem.vue'
import { useRouter } from 'vue-router' // Vue Router 사용

const router = useRouter() // 라우터 인스턴스 가져오기

const props = defineProps({
  menuList: {
    type: Object,
    required: true
  },
  isEmpty: {
    type: Boolean,
    required: true
  }
})

const goDetail = (recipeId) => {
  router.push(`/wiki/${recipeId}`)
}
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 4rem;
  font-size: 2.4rem;
  color: var(--black-color);
}

.recipe-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  align-items: center;
}

.recipe-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  width: 80%;
  margin-top: 6rem;
  margin-bottom: 8rem;
}

@media screen and (max-width: 960px) {
  .recipe-list {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
