import request from '@/utils/request'

// 登入方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    method: 'post',
    params: data
  })
}

// 獲取使用者詳細資訊
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 獲取驗證碼
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}