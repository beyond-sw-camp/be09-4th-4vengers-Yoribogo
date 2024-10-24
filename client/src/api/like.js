import apiClient from '@/api/axios'

export const addLike = (userId, postId) => {
  return apiClient.post('/likes/like', { userId, postId })
}
