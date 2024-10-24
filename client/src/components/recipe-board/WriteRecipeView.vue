<template>
  <div class="write-recipe-view-container">
    <div style="display: flex">
      <!-- 입력창 컨테이너 -->
      <div class="input-container" style="width: 50%">
        <!-- 메뉴명 입력 필드 -->
        <WriteRecipeInput
          :id="'menu-name'"
          :label="'메뉴 명'"
          :type="'text'"
          :placeholder="'메뉴명을 입력해주세요'"
        />

        <!-- 대표사진 입력 필드 -->
        <WriteRecipeInput
          :id="'menu-image'"
          :label="'대표 사진'"
          :type="'image'"
          :placeholder="'여기에 이미지를 드래그하거나 클릭하여 추가'"
        />

        <!-- 재료 입력 필드 -->
        <WriteRecipeInput
          :id="'ingredient'"
          :label="'재료'"
          :type="'text'"
          :placeholder="'재료를 하나씩 입력하고 추가해주세요'"
        />

        <!-- 조리 순서 입력 필드 -->
        <WriteRecipeInput
          :id="'cooking-order'"
          :label="'조리 순서'"
          :type="'step'"
          :placeholder="'단계별 요리법을 작성해주세요'"
        />
      </div>

      <!-- 보여지는 창 컨테이너 -->
      <div class="show-container"></div>
    </div>

    <div>
      <button class="regist-recipe-btn">등록하기</button>
    </div>
  </div>
</template>

<script setup>
import WriteRecipeInput from '@/components/recipe-board/WriteRecipeInput.vue'
</script>

<style scoped>
.write-recipe-view-container {
  width: 100%;
  display: flex;
  flex-direction: column;
}
.input-container {
  position: relative;
  width: 100%;
  height: auto;
  padding: 1rem;
}

.show-container {
  width: 50%;
}

.regist-recipe-btn {
  background-color: var(--navy-color);
  color: white;
  height: 30px;
  line-height: 30px;
  padding: 0.5rem 1.3rem;
  border-radius: 0.3rem;
  border: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  right: 50%;
}
</style>
