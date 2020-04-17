import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 建立axios例項
const service = axios.create({
  // axios中請求配置有baseURL選項，表示請求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超時
  timeout: 10000
})
// request攔截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['Authorization'] = 'Bearer ' + getToken() // 讓每個請求攜帶自定義token 請根據實際情況自行修改
    }
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

// 響應攔截器
service.interceptors.response.use(res => {
    const code = res.data.code
    if (code === 401) {
      MessageBox.confirm(
        '登入狀態已過期，您可以繼續留在該頁面，或者重新登入',
        '系統提示',
        {
          confirmButtonText: '重新登入',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('LogOut').then(() => {
          location.reload() // 為了重新例項化vue-router物件 避免bug
        })
      })
    } else if (code !== 200) {
      Notification.error({
        title: res.data.msg
      })
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
