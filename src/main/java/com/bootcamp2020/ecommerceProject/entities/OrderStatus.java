package com.bootcamp2020.ecommerceProject.entities;

import com.bootcamp2020.ecommerceProject.enums.FromStatus;
import com.bootcamp2020.ecommerceProject.enums.ToStatus;

import javax.persistence.*;

@Entity
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(mappedBy = "orderStatus")
    @JoinColumn(name = "OrderProductId")
    private OrderProduct orderProduct;

    @Enumerated(value = EnumType.STRING)
    private FromStatus fromStatus;
    @Enumerated(value = EnumType.STRING)
    private ToStatus toStatus;
}
