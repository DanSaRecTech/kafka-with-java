package br.com.alura.ecommerce;

import br.com.alura.ecommerce.model.Email;
import br.com.alura.ecommerce.model.Order;

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

                    var subject = "Thank you for your order!";
                    var body = "We are processing your order!";
                    var email = Email.builder()
                            .subject(subject)
                            .body(body)
                            .build();
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }
}
