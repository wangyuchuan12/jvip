package com.zjmxdz.domain;


import com.wyc.common.annotation.Condition;
import com.wyc.common.annotation.Conditions;
import com.wyc.common.annotation.CreateAt;
import com.wyc.common.annotation.UpdateAt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.parser.Part;

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

    //金额范围，最小额度
    @Column(name = "integralconfig_condition_limitamount")
    @Conditions(@Condition(properties="CONDITION_LIMIT_AMOUNT",type= Part.Type.LESS_THAN_EQUAL))
    private BigDecimal conditionLimitAmount;


    //金额范围，最大额度
    @Column(name = "integralconfig_condition_maxamount")
    @Conditions(@Condition(properties="CONDITION_MAX_AMOUNT",type= Part.Type.GREATER_THAN))
    private BigDecimal conditionMaxAmount;

    @Column(name = "integralconfig_gradle")
    private Integer gradle;


    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
