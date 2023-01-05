import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manage.vue'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
      {path: 'testFile', name: '原卷管理', component: () => import('../views/testFile.vue')},
      {path: 'user', name: '学生管理', component: () => import('../views/User.vue')},
      {path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
      {path: 'userFile', name: '提交试卷管理', component: () => import('../views/userFile.vue')}
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Home.vue')
  },
  {
    path: '/test',
    name: 'Test',
    component: () => import('../views/front/Test.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) =>{
  localStorage.setItem("currentPathName", to.name) // 设置当前的路由名称
  store.commit("setPath") // 出发store的数据更新
  next() // 放行路由
})


export default router
