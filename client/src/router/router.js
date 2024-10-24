import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import KakaoCallback from '@/components/user/sns/KakaoCallback.vue' // 콜백 처리용 컴포넌트
import NaverCallback from '@/components/user/sns/NaverCallback.vue' // 콜백 처리용 컴포넌트
import SignupView from '@/views/user/SignupView.vue'
import LoginView from '@/views/user/LoginView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/signup',
    name: 'Signup',
    component: SignupView
  },
  {
    path: '/login-page',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/question/:qid',
    name: 'question',
    component: () => import('../views/QuestionView.vue')
  },
  {
    path: '/question/result',
    name: 'result',
    component: () => import('../views/ResultView.vue')
  },
  {
    path: '/wiki',
    name: 'wiki',
    component: () => import('../views/WikiView.vue')
  },
  {
    path: '/wiki/:recipeId',
    name: 'wiki-detail',
    component: () => import('../views/WikiDetailView.vue')
  },
  {
    path: '/recipe-board',
    name: 'recipe-board',
    component: () => import('../views/RecipeBoardListView.vue') // 메인 레이아웃
  },
  {
    path: '/recipe-board/:board_id',
    name: 'recipe-board-detail',
    component: () => import('../views/RecipeBoardDetailView.vue'),
    props: true
  },
  {
    path: '/recipe-board/write',
    name: 'recipe-board-view',
    component: () => import('../views/RecipeBoardWriteView.vue'),
    props: true
  },
  {
    path: '/mypage',
    name: 'mypage',
    redirect: '/mypage/bookmarked',
    component: () => import('../views/MyPageView.vue'),
    children: [
      {
        path: 'bookmarked',
        name: 'BookmarkedRecipes',
        component: () => import('@/components/mypage/BookmarkedRecipes.vue')
      },
      {
        path: 'satisfied',
        name: 'SatisfiedRecipes',
        component: () => import('@/components/mypage/SatisfiedRecipes.vue')
      },
      {
        path: 'my-recipes',
        name: 'MyRecipes',
        component: () => import('@/components/mypage/MyRecipes.vue')
      }
    ]
  },
  {
    path: '/qna',
    name: 'qna',
    component: () => import('../views/QnaView.vue')
  },
  {
    path: '/qna/:num',
    name: 'qna-detail',
    component: () => import('../views/QnaDetailView.vue'),
    props: true
  },
  {
    path: '/qna/new',
    name: 'qna-new',
    component: () => import('../views/QnaCreateView.vue')
  },
  {
    path: '/qna/edit/:num',
    name: 'qna-edit',
    component: () => import('../views/QnaEditView.vue'),
    props: true
  },
  {
    path: '/kakaologin',
    name: 'KakaoCallback',
    component: KakaoCallback
  },
  {
    path: '/naverlogin',
    name: 'NaverCallback',
    component: NaverCallback
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
