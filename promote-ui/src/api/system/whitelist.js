import request from '@/utils/request'

// 查詢白名單列表
export function listWhitelist(query) {
  return request({
    url: '/system/whitelist/list',
    method: 'get',
    params: query
  })
}

// 查詢白名單詳細
export function getWhitelist(id) {
  return request({
    url: '/system/whitelist/' + id,
    method: 'get'
  })
}

// 新增白名單
export function addWhitelist(data) {
  return request({
    url: '/system/whitelist',
    method: 'post',
    data: data
  })
}

// 修改白名單
export function updateWhitelist(data) {
  return request({
    url: '/system/whitelist',
    method: 'put',
    data: data
  })
}

// 刪除白名單
export function delWhitelist(id) {
  return request({
    url: '/system/whitelist/' + id,
    method: 'delete'
  })
}

// 匯出白名單
export function exportWhitelist(query) {
  return request({
    url: '/system/whitelist/export',
    method: 'get',
    params: query
  })
}