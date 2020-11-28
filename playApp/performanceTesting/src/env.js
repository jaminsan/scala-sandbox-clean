// NOTE: https://k6.io/docs/using-k6/environment-variables
const ORDER_API_BASE_URL = __ENV.ORDER_API_BASE_URL ? __ENV.ORDER_API_BASE_URL : 'http://localhost:9000'

export default {
  orderApiBaseUrl: ORDER_API_BASE_URL
}