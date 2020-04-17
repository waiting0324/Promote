import request from '@/utils/request'

// 查詢排程日誌列表
export function listJobLog(query) {
  return request({
    url: '/monitor/jobLog/list',
    method: 'get',
    params: query
  })
}

// 刪除排程日誌
export function delJobLog(jobLogId) {
  return request({
    url: '/monitor/jobLog/' + jobLogId,
    method: 'delete'
  })
}

// 清空排程日誌
export function cleanJobLog() {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete'
  })
}

// 匯出排程日誌
export function exportJobLog(query) {
  return request({
    url: '/monitor/jobLog/export',
    method: 'get',
    params: query
  })
}