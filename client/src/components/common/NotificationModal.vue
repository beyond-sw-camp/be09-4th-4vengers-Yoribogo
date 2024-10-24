<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>알림</h2>
      <!-- 알림 목록 -->
      <ul class="notification-list">
        <li
          v-for="notification in noticationList"
          :key="notification.notificationId"
          :class="[
            'notification',
            {
              unread: notification.notificationStatus === 'UNREAD',
              read: notification.notificationStatus === 'READ'
            }
          ]"
          @click="markAsRead(notification)"
        >
          <span>{{ notification.notificationContent }}</span>
          <span class="notification-date">{{
            formatDate(notification.notificationCreatedAt)
          }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'

// 부모 컴포넌트에서 전달받을 props 정의
const props = defineProps({
  notifications: {
    type: Array,
    required: true
  }
})

const noticationList = ref([])

const emit = defineEmits(['close']) // 부모 컴포넌트로 close 이벤트 전달

const closeModal = () => {
  emit('close') // 부모 컴포넌트에 close 이벤트 전달
}

// 날짜 포맷 함수
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 알림 읽기 함수 (서버 요청)
const markAsRead = async (notification) => {
  if (notification.notificationStatus === 'READ') return // 이미 읽은 알림이면 무시

  try {
    const response = await fetch(`/api/notifications/updateStatus/${notification.notificationId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        notificationStatus: 'READ'
      })
    })

    if (response.ok) {
      notification.notificationStatus = 'READ' // 상태 업데이트 후 클라이언트 측 상태 변경
    } else {
      console.error('Failed to update notification status')
    }
  } catch (error) {
    console.error('Error updating notification status:', error)
  }
}

onMounted(() => {
  if (props.notifications !== null) noticationList.value = props.notifications
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 10px;

  /* 크기 조정 */
  width: 53rem; /* 기존보다 넓게 설정 */
  max-height: 60rem; /* 기존보다 높게 설정 */

  max-width: 90%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.notification-list {
  list-style: none;
  padding: 0;
  margin: 0;

  /* 알림 목록이 5개를 넘으면 스크롤 가능하도록 설정 */
  max-height: 40rem; /* 목록 높이 증가 */
  overflow-y: auto; /* 스크롤 활성화 */

  /* 스크롤바 숨기기 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.notification-list::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.notification {
  padding: 1rem;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.unread {
  background-color: #f0f0f0; /* Gray */
  font-weight: bold;
}

.read {
  background-color: white;
}

.notification:hover {
  background-color: var(--light-pink-color);
}

.notification-date {
  font-size: 0.9rem;
  color: #666;
  margin-left: 1rem;
}
</style>
