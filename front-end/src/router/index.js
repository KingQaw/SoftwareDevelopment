import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/Login.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/register/Register.vue')
    },
    {
      path: '/user',
      name: 'User',
      component: () => import('@/views/user/Main.vue'),
      children: [
        {
          path: '/strategy',
          name: 'Strategy',
          component: () => import('@/views/user/strategy/Strategy.vue')
        },
        {
          path: '/userInfo',
          name: 'UserInfo',
          component: () => import('@/views/user/self/UserInfo.vue')
        },
        {
          path: '/resetPassword',
          name: 'ResetPassword',
          component: () => import('@/views/user/self/ResetPassword.vue')
        },
        {
          path: '/publishStrategy',
          name: 'PublishStrategy',
          component: () => import('@/views/user/strategy/PublishStrategy.vue')
        },
        {
          path: '/myStrategy',
          name: 'MyStrategy',
          component: () => import('@/views/user/strategy/MyStrategy.vue')
        },
        {
          path: '/myFavorite',
          name: 'MyFavorite',
          component: () => import('@/views/user/strategy/MyFavorite.vue')
        },
        {
          path: '/strategy/:id',
          name: 'StrategyDetail',
          component: () => import('@/views/user/strategy/StrategyDetail.vue')
        },
      ]
    },
    {
      path: '/admin',
      name: 'Admin',
      component: () => import('@/views/admin/Main.vue'),
      children: [
        {
          path: '/dataOverview',
          name: 'DataOverview',
          component: () => import('@/views/admin/DataOverview.vue')
        },
        {
          path: '/auditStrategy',
          name: 'AuditStrategy',
          component: () => import('@/views/admin/AuditStrategy.vue')
        },
        {
          path: '/manageStrategy',
          name: 'ManageStrategy',
          component: () => import('@/views/admin/ManageStrategy.vue')
        },
        {
          path: '/manageUser',
          name: 'ManageUser',
          component: () => import('@/views/admin/ManageUser.vue')
        },
        {
          path: '/loginRecord',
          name: 'LoginRecord',
          component: () => import('@/views/admin/LoginRecord.vue')
        },
        {
          path: '/auditStrategyDetail/:id',
          name: 'AuditStrategyDetail',
          component: () => import('@/views/admin/AuditStrategyDetail.vue')
        }, 
        {
          path: '/strategyDetail/:id',
          name: 'ShowStrategyDetail',
          component: () => import('@/views/admin/ShowStrategyDetail.vue')
        },
      ]
    },
    {
      path: '/:pathMatch(.*)',
      redirect: '/login',
    }
  ],
})

export default router
