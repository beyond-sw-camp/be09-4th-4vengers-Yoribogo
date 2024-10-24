<template>
  <div class="write-recipe-board" style="display: flex; flex-direction: column">
    <div style="display: flex">
      <div class="attribute-board" :class="{ first: !isSmallScreen }">
        <AttributeMenuNameInput
          :name="'메뉴명'"
          :placeholder="'메뉴명을 입력하세요'"
          :show-button="false"
          v-model="menu_name"
        ></AttributeMenuNameInput>
        <AttributeImageInput
          :name="'대표 사진'"
          :placeholder="'여기에 이미지를 드래그하거나 클릭하여 추가'"
          v-model="board_image"
        ></AttributeImageInput>
        <AttributeIngredientInput
          :name="'재료'"
          :placeholder="'재료를 하나씩 입력하고 추가해주세요'"
          :show-button="true"
          :ingredients="ingredients"
          @add-item="addIngredient"
        ></AttributeIngredientInput>
        <!-- 조리 순서 -->
        <AtrributeTextAndImageInput
          :name="'조리 순서'"
          :placeholder="'단계별 요리법을 작성해주세요'"
          @add="addStep"
          v-model="manual_step"
        ></AtrributeTextAndImageInput>
      </div>

      <!-- 재료 및 조리 순서 요약 -->
      <div class="attribute-board second" v-if="!isSmallScreen">
        <AttributeIngredientStep
          :ingredients="ingredients"
          :manual_step="manual_step"
          @add-step="addStep"
          @remove-step="removeStep"
          @remove-item="removeIngredient"
        ></AttributeIngredientStep>
      </div>
    </div>

    <!-- 등록버튼 -->
    <div class="submit-button-container">
      <button class="submit-button" @click="submitRecipe">등록하기</button>
    </div>

    <!-- 로딩 스피너 -->
    <LoadingSpinner v-if="loading" />
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted, watch } from 'vue'
import AttributeImageInput from '@/components/recipe-board/AtrributeImageInput.vue'
import AtrributeTextAndImageInput from '@/components/recipe-board/AtrributeTextAndImageInput.vue'
import AttributeIngredientStep from './AttributeIngredientStep.vue'
import AttributeMenuNameInput from '@/components/recipe-board/AttributeMenuNameInput.vue'
import AttributeIngredientInput from './AttributeIngredientInput.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import axios from 'axios'
import { useTokenStore } from '@/stores/tokenStore'
import { useRouter } from 'vue-router'

const router = useRouter()

const isSmallScreen = ref(window.innerWidth <= 1280)
const handleResize = () => {
  isSmallScreen.value = window.innerWidth <= 1280
}

const menu_name = ref('')
const board_image = ref('')
const ingredients = ref([])
const manual_step = ref([]) // 조리 순서 배열
const loading = ref(false) // 로딩 상태 관리

const tokenStore = useTokenStore()

// 로컬스토리지에서 데이터를 불러오는 함수
const loadFromLocalStorage = () => {
  const storedMenuName = localStorage.getItem('menu_name')
  const storedFileName = localStorage.getItem('board_image')
  const storedIngredients = localStorage.getItem('ingredients')
  const storedSteps = localStorage.getItem('manual_step')

  if (storedMenuName) menu_name.value = storedMenuName
  if (storedFileName) board_image.value = storedFileName
  if (storedIngredients) ingredients.value = JSON.parse(storedIngredients) || []
  if (storedSteps) manual_step.value = JSON.parse(storedSteps) || []
}

// 메뉴가 변경되는 경우 로컬스토리지 변경사항 저장
watch(menu_name, (newValue) => {
  localStorage.setItem('menu_name', newValue)
})

// 대표사진이 변경되는 경우 로컬스토리지 변경사항 저장
watch(board_image, (newValue) => {
  localStorage.setItem('board_image', newValue)
})

// 배열에 재료 추가 함수
const addIngredient = (ingredient) => {
  if (ingredient && !ingredients.value.includes(ingredient)) {
    ingredients.value = [...ingredients.value, ingredient]
    saveToLocalStorage() // 추가 후 로컬 스토리지 업데이트
  } else {
    alert('재료를 반드시 입력하거나 같은 재료는 안돼요!')
  }
}
// 재료 삭제 함수
const removeIngredient = (index) => {
  ingredients.value = ingredients.value.filter((_, i) => i !== index)
  saveToLocalStorage()
}
// 하위 컴포넌트로부터 새로운 조리 순서 단계 추가
const addStep = (newStep) => {
  if (newStep && newStep.content) {
    manual_step.value = [
      ...manual_step.value,
      {
        step: newStep.content,
        image: newStep.image || '' // 이미지가 없으면 빈 문자열로 설정
      }
    ]
    saveToLocalStorage()
  } else {
    alert('내용을 모두 입력해주세요!')
  }
}
// 조리 순서 삭제 함수
const removeStep = (index) => {
  manual_step.value = manual_step.value.filter((_, i) => i !== index)
  saveToLocalStorage()
}

// 로컬 스토리지에 저장하는 함수
const saveToLocalStorage = () => {
  try {
    localStorage.setItem('menu_name', menu_name.value)
    localStorage.setItem('board_image', board_image.value)
    localStorage.setItem('ingredients', JSON.stringify(ingredients.value))
    localStorage.setItem('manual_step', JSON.stringify(manual_step.value))
  } catch (e) {
    console.error('Failed to save to localStorage:', e)
  }
}

const submitRecipe = async () => {
  try {
    loading.value = true

    const formData = new FormData()
    formData.append('menu_name', menu_name.value)
    formData.append('ingredients', ingredients.value.join(', '))
    formData.append('user_id', tokenStore.token.userId)

    if (board_image.value && board_image.value instanceof File) {
      formData.append('boardImage', board_image.value)
    } else {
      console.error('board_image is not a File instance')
    }

    const boardResponse = await axios.post('/boot/api/recipe-board/create', formData)

    // manual_step을 처리하는 비동기 함수
    const manualDataPromises = manual_step.value.map(async (step, index) => {
      let manualImage = step.image

      // blob URL인 경우 File 객체로 변환
      if (manualImage && manualImage.startsWith('blob')) {
        manualImage = await fetch(manualImage)
          .then((res) => res.blob())
          .then((blob) => new File([blob], `manual_step_${index}.jpg`, { type: 'image/jpeg' }))
      }

      return {
        manual_step: index + 1,
        manual_content: step.step,
        manual_image: manualImage instanceof File ? manualImage : null
      }
    })

    // 모든 비동기 처리가 완료될 때까지 기다림
    const manualData = await Promise.all(manualDataPromises)

    if (manualData.length > 0) {
      const manualFormData = new FormData()
      manualData.forEach((manual) => {
        manualFormData.append('manual_steps', manual.manual_step)
        manualFormData.append('manual_contents', manual.manual_content)
        if (manual.manual_image) {
          manualFormData.append('manual_images', manual.manual_image)
        }
      })

      await axios.post(
        `/boot/api/recipe-board/create/manual/${boardResponse.data.data.board_id}`,
        manualFormData
      )
    }

    // 로컬 스토리지에서 token을 제외한 모든 항목 삭제
    localStorage.removeItem('menu_name')
    localStorage.removeItem('board_image')
    localStorage.removeItem('ingredients')
    localStorage.removeItem('manual_step')

    // 레시피 등록 성공 메시지 및 페이지 이동
    alert('레시피가 성공적으로 등록되었습니다.')
    router.push('/recipe-board') // 페이지 이동
  } catch (error) {
    console.error('레시피 등록 실패:', error)
    alert('레시피 등록에 실패했습니다.')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
  loadFromLocalStorage()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.write-recipe-board {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 90%;
  height: auto;
  margin-top: 8rem;
  margin-bottom: 8rem;
  gap: 3rem;
}

.attribute-board {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  gap: 3rem;
}

.first {
  width: 50%;
  border-right: 2px solid var(--light-gray-color);
}

.second {
  width: 50%;
}

.submit-button-container {
  justify-content: center;
  align-items: center;
  display: flex;
}

.submit-button {
  background-color: var(--navy-color);
  padding: 1rem 2rem;
  border: transparent;
  border-radius: 3px;
  color: white;
}

/* 로딩 스피너 스타일 */
.loading-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>
