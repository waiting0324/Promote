import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置項
 *
 * hidden: true                   // 當設定 true 的時候該路由不會再側邊欄出現 如401，login等頁面，或者如一些編輯頁面/edit/1
 * alwaysShow: true               // 當你一個路由下面的 children 宣告的路由大於1個時，自動會變成巢狀的模式--如元件頁面
 *                                // 只有一個時，會將那個子路由當做根路由顯示在側邊欄--如引導頁面
 *                                // 若你想不管路由下面的 children 宣告的個數都顯示你的根路由
 *                                // 你可以設定 alwaysShow: true，這樣它就會忽略之前定義的規則，一直顯示根路由
 * redirect: noRedirect           // 當設定 noRedirect 的時候該路由在麵包屑導航中不可被點選
 * name:'router-name'             // 設定路由的名字，一定要填寫不然使用<keep-alive>時會出現各種問題
 * meta : {
    roles: ['admin','editor']    // 設定該路由進入的許可權，支援多個許可權疊加
    title: 'title'               // 設定該路由在側邊欄和麵包屑中展示的名字
    icon: 'svg-name'             // 設定該路由的圖示，對應路徑src/icons/svg
    breadcrumb: false            // 如果設定為false，則不會在breadcrumb麵包屑中顯示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: '首頁',
        meta: { title: '首頁', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '個人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典資料', icon: '' }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: () => import('@/views/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '排程日誌' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit',
        component: () => import('@/views/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
