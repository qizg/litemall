import request from '@/utils/request'

export function listRecord(query) {
  return request({
    url: '/flashsales/listRecord',
    method: 'get',
    params: query
  })
}

export function listFlashSales(query) {
  return request({
    url: '/flashsales/list',
    method: 'get',
    params: query
  })
}

export function deleteFlashSales(data) {
  return request({
    url: '/flashsales/delete',
    method: 'post',
    data
  })
}

export function publishFlashSales(data) {
  return request({
    url: '/flashsales/create',
    method: 'post',
    data
  })
}

export function editFlashSales(data) {
  return request({
    url: '/flashsales/update',
    method: 'post',
    data
  })
}
