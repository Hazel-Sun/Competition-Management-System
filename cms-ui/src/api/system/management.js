import request from '@/utils/request'

// 查询竞赛信息管理列表
export function listManagement(query) {
  return request({
    url: '/system/management/list',
    method: 'get',
    params: query
  })
}

// 查询竞赛信息管理详细
export function getManagement(compId) {
  return request({
    url: '/system/management/' + compId,
    method: 'get'
  })
}

// 新增竞赛信息管理
export function addManagement(data) {
  return request({
    url: '/system/management',
    method: 'post',
    data: data
  })
}

// 修改竞赛信息管理
export function updateManagement(data) {
  return request({
    url: '/system/management',
    method: 'put',
    data: data
  })
}

// 删除竞赛信息管理
export function delManagement(compId) {
  return request({
    url: '/system/management/' + compId,
    method: 'delete'
  })
}
