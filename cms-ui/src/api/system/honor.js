import request from '@/utils/request'

// 查询荣誉信息管理列表
export function listHonor(query) {
  return request({
    url: '/system/honor/list',
    method: 'get',
    params: query
  })
}

// 查询荣誉信息管理详细
export function getHonor(honorId) {
  return request({
    url: '/system/honor/' + honorId,
    method: 'get'
  })
}

// 新增荣誉信息管理
export function addHonor(data) {
  return request({
    url: '/system/honor',
    method: 'post',
    data: data
  })
}

// 修改荣誉信息管理
export function updateHonor(data) {
  return request({
    url: '/system/honor',
    method: 'put',
    data: data
  })
}

// 删除荣誉信息管理
export function delHonor(honorId) {
  return request({
    url: '/system/honor/' + honorId,
    method: 'delete'
  })
}

// 查询院系下拉树结构
export function compListSelect() {
  return request({
    url: '/system/honor/compList',
    method: 'get'
  })
}
// 查询院系下拉树结构
export function squadListSelect() {
  return request({
    url: '/system/honor/squadList',
    method: 'get'
  })
}
