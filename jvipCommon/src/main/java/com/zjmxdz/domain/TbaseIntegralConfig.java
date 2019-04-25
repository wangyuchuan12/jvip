package com.zjmxdz.domain;


import com.wyc.common.annotation.CreateAt;
import com.wyc.common.annotation.UpdateAt;
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
@Table(name="tbase_integral_config")
public class TbaseIntegralConfig {
    @Id
    @Column(name = "integralconfig_id")
    private String id;

    @Column(name = "integralconfig_integral")
    private Integer integral;

    @Column(name = "integralconfig_amount")
    private BigDecimal amount;

    @Column(name = "integralconfig_gradle")
    private Integer gradle;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
