import request from '@/utils/request'

// 查询报名表上传列表
export function listEntryinfo(query) {
  return request({
    url: '/system/entryinfo/list',
    method: 'get',
    params: query
  })
}

// 查询报名表上传详细
export function getEntryinfo(fileId) {
  return request({
    url: '/system/entryinfo/' + fileId,
    method: 'get'
  })
}

// 新增报名表上传
export function addEntryinfo(data) {
  return request({
    url: '/system/entryinfo',
    method: 'post',
    data: data
  })
}

// 修改报名表上传
export function updateEntryinfo(data) {
  return request({
    url: '/system/entryinfo',
    method: 'put',
    data: data
  })
}

// 删除报名表上传
export function delEntryinfo(fileId) {
  return request({
    url: '/system/entryinfo/' + fileId,
    method: 'delete'
  })
}
