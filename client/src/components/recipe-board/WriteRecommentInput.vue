<template>
  <div v-for="(recomment, index) in recomments" :key="index">
    <div
      class="recomment-container"
      style="margin-top: 3rem; height: auto; padding: 3rem; display: flex; flex-direction: column"
    >
      <div class="recomment-info" style="display: flex">
        <div class="profile-img">
          <!-- 프로필 이미지는 따로 받아오기 -->
          <img
            :src="getProfileImage(recomment['user-id']) || defaultProfileImage"
            alt="profile"
            width="50"
            height="50"
          />
        </div>

        <div class="user-info">
          <div class="nickname-and-badge" style="display: flex; align-items: center">
            <!-- 닉네임은 따로 받아오기 -->
            <div style="font-size: 1.5rem; font-weight: bold">
              작성자: {{ getNickname(recomment['user-id']) }}
            </div>
            <!-- 뱃지는 따로 받아오기 -->
            <div style="margin-left: 10px">
              <img
                :src="getTierImage(recomment['user-id'])"
                alt="뱃지 이미지"
                width="30"
                height="30"
              />
            </div>
          </div>
          <div>
            {{ recomment['recomment-created-at'] }}
          </div>
        </div>
      </div>

      <!-- 댓글 내용 -->
      <div class="recomment-content" style="font-size: 1.8rem">
        {{ recomment['recomment-content'] }}
      </div>

      <div style="justify-content: flex-end">
        <button style="border-style: none; background: none; margin-top: 6px">답글</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const recomments = ref([])
</script>

<style scoped>
.recomment-container {
  display: flex;
  background-color: #fff5f5;
  width: 80%;
}
</style>
