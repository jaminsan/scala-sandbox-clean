import {check} from 'k6'
import http from 'k6/http'
import env from './env'

export default class OrderApiClient {

  static postOrder() {
    const url = env.orderApiBaseUrl + '/orders'

    const body =
      JSON.stringify({
          customerId: '8d07abfc-c3cf-4651-a77d-f6037377f06b',
          itemId: 'item1',
          quantity: 3
        }
      )

    const params = {
      headers: {
        'Content-type': 'application/json'
      }
    }

    const res = http.post(url, body, params)

    check(res, {
      'postOrder is status 200': (r) => r.status === 200
    })

    return JSON.parse(res.body).orderId
  }

  static getOrder(orderId) {
    const url = `${env.orderApiBaseUrl}/orders/${orderId}`

    const res = http.get(url)

    check(res, {
      'getOrder is status 200': (r) => r.status === 200
    })

    return JSON.parse(res.body)
  }
}