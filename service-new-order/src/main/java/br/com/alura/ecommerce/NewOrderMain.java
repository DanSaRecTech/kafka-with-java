package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {
                for (var i = 0; i < 10; i++) {
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var order = Order.builder()
                            .userId(userId)
                            .orderId(orderId)
                            .amount(new BigDecimal(Math.random() * 5000 + 1))
                            .build();
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var subject = "Thank you for your order: ";
                    var body = "We are processing your order, Sr. ";
                    var email = Email.builder()
                            .subject(subject)
                            .body(getBody(body, order))
                            .build();
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }

    private static String getBody(String bodyEmail, Order order) {
        StringBuilder body = new StringBuilder();
        body.append(bodyEmail);
        body.append(order.getUserId());
        body.append("! ");
        body.append("The total amount of your order: ");
        body.append(order.getAmount());
        return body.toString();
    }
}
