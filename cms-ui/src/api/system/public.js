import request from '@/utils/request'

// 查询赛事宣传列表
export function listPublic(query) {
  return request({
    url: '/system/public/list',
    method: 'get',
    params: query
  })
}

// 查询赛事宣传详细
export function getPublic(publicId) {
  return request({
    url: '/system/public/' + publicId,
    method: 'get'
  })
}

// 新增赛事宣传
export function addPublic(data) {
  return request({
    url: '/system/public',
    method: 'post',
    data: data
  })
}

// 修改赛事宣传
export function updatePublic(data) {
  return request({
    url: '/system/public',
    method: 'put',
    data: data
  })
}

// 删除赛事宣传
export function delPublic(publicId) {
  return request({
    url: '/system/public/' + publicId,
    method: 'delete'
  })
}

// 查询院系下拉树结构
export function compListSelect() {
  return request({
    url: '/system/public/compList',
    method: 'get'
  })
}
