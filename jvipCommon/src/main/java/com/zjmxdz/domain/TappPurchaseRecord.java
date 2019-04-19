package com.zjmxdz.domain;

import com.wyc.common.annotation.Condition;
import lombok.AllArgsConstructor;
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
@Table(name="tapp_purchase_record")
public class TappPurchaseRecord {
    @Id
    @Column(name = "purcharserecord_id")
    private String id;

    @Column(name = "purcharserecord_userid")
    @Condition
    private String userId;

    @Column(name = "purcharserecord_amount")
    private BigDecimal amount;

    @Column(name = "purcharserecord_goodid")
    private String goodId;

    @Column(name = "purcharserecord_goodname")
    private Integer goodname;

    @Column(name = "purcharserecord_buytime")
    private Timestamp buytime;

    //获得积分
    @Column(name = "purcharserecord_integral")
    private Long integral;

    //获得豆豆
    @Column(name = "purcharserecord_peas")
    private Long peas;

    @Column(name = "update_at")
    private Timestamp updateAt;
    @Column(name = "create_at")
    private Timestamp createAt;
}
