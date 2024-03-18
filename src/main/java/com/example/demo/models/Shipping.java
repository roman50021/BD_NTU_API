package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderInfo orderInfo;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    private LocalDateTime orderReceivedTime;
    private LocalDateTime readyTime;
    private LocalDateTime courierAssignedTime;
    private LocalDateTime courierPickupTime;
    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;
}
