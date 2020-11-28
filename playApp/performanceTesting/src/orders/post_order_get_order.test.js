import OrderApiClient from "../order_api_client";

export let options = {
  duration: '10s',
  vus: 10
}

export default function () {
  const orderId = OrderApiClient.postOrder()
  OrderApiClient.getOrder(orderId)
}
