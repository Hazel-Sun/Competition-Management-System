import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询竞赛队伍列表
export function listSquad(query) {
  return request({
    url: '/system/squad/list',
    method: 'get',
    params: query
  })
}

// 查询竞赛队伍详细
export function getSquad(squadId) {
  return request({
    url: '/system/squad/' + parseStrEmpty(squadId),
    method: 'get'
  })
}

// 新增竞赛队伍
export function addSquad(data) {
  return request({
    url: '/system/squad',
    method: 'post',
    data: data
  })
}

// 修改竞赛队伍
export function updateSquad(data) {
  return request({
    url: '/system/squad',
    method: 'put',
    data: data
  })
}

// 删除竞赛队伍
export function delSquad(squadId) {
  return request({
    url: '/system/squad/' + squadId,
    method: 'delete'
  })
}

// 查询角色已授权用户列表
export function allocatedSquadUserList(query) {
  return request({
    url: '/system/squad/authUser/allocatedList',
    method: 'get',
    params: query
  })
}

// 查询角色未授权用户列表
export function unallocatedSquadUserList(query) {
  return request({
    url: '/system/squad/authUser/unallocatedList',
    method: 'get',
    params: query
  })
}

// 取消用户授权角色
export function authUserCancel(data) {
  return request({
    url: '/system/squad/authUser/cancel',
    method: 'put',
    data: data
  })
}

// 批量取消用户授权角色
export function authUserCancelAll(data) {
  return request({
    url: '/system/squad/authUser/cancelAll',
    method: 'put',
    params: data
  })
}

// 授权用户选择
export function authUserSelectAll(data) {
  return request({
    url: '/system/squad/authUser/selectAll',
    method: 'put',
    params: data
  })
}

// 查询院系下拉树结构
export function compListSelect() {
  return request({
    url: '/system/squad/compList',
    method: 'get'
  })
}

