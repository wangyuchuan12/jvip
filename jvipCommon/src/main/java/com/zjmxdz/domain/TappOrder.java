package com.zjmxdz.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="tapp_order")
public class TappOrder {
    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_amount")
    private BigDecimal amount;

    @Column(name = "order_refereeuseraccount")
    private String refereeAccount;

    @Column(name = "order_account")
    private String account;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "create_at")
    private Timestamp createAt;
}
