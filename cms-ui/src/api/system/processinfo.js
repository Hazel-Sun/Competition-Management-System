import request from '@/utils/request'

// 查询过程文件管理列表
export function listProcessinfo(query) {
  return request({
    url: '/system/processinfo/list',
    method: 'get',
    params: query
  })
}

// 查询过程文件管理详细
export function getProcessinfo(fileId) {
  return request({
    url: '/system/processinfo/' + fileId,
    method: 'get'
  })
}

// 新增过程文件管理
export function addProcessinfo(data) {
  return request({
    url: '/system/processinfo',
    method: 'post',
    data: data
  })
}

// 修改过程文件管理
export function updateProcessinfo(data) {
  return request({
    url: '/system/processinfo',
    method: 'put',
    data: data
  })
}

// 删除过程文件管理
export function delProcessinfo(fileId) {
  return request({
    url: '/system/processinfo/' + fileId,
    method: 'delete'
  })
}

// 查询院系下拉树结构
export function compListSelect() {
  return request({
    url: '/system/processinfo/compList',
    method: 'get'
  })
}
// 查询院系下拉树结构
export function squadListSelect() {
  return request({
    url: '/system/processinfo/squadList',
    method: 'get'
  })
}

