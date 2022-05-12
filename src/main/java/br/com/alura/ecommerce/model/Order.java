package br.com.alura.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
public class Order {

    private final String orderId, userId;
    private BigDecimal amount;
}
