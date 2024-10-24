<template>
  <button :class="buttonClass" :disabled="disabled">
    {{ label }}
  </button>
</template>

<script>
export default {
  name: 'YesNoButton',
  props: {
    type: {
      type: String,
      default: 'next' // next 또는 cancel 값을 가질 수 있음
    },
    label: {
      type: String,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    buttonClass() {
      return this.type === 'cancel' ? 'cancel-btn' : 'next-btn'
    }
  }
}
</script>

<style scoped>
button {
  width: 360px;
  height: 5rem;
  margin: 1rem;
  border: none;
  border-radius: 5px; /* 둥근 모서리 적용 */
  font-size: 2rem; /* 글자 크기 */
  cursor: pointer;
  font-weight: bold; /* 글자 두께 */
}

button[disabled] {
  cursor: not-allowed; /* disabled 상태일 때 커서 변경 */
  opacity: 0.6; /* 반투명 효과로 비활성화 표시 */
}

.cancel-btn {
  background-color: #a6a6a6; /* 회색 */
  color: #fff;
}

.next-btn {
  background-color: #2c3e60; /* 남색 */
  color: #fff;
}
</style>
