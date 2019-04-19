package com.zjmxdz.domain;


import com.wyc.common.annotation.Condition;
import com.wyc.common.annotation.Conditions;
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
@Table(name="tbase_pconfig")
public class TbasePurchaseConfig {
    @Id
    @Column(name = "pconfig_id")
    private String id;

    //等级，如果为0表示没有这个要求
    @Column(name = "pconfig_condition_grade")
    @Conditions(@Condition(type= Part.Type.IN,properties = "CONDITION_GRADE_IN"))
    private Integer conditionGrade;

    //金额范围，最小额度
    @Column(name = "pconfig_condition_limitamount")
    private BigDecimal conditionLimitAmount;


    //金额范围，最大额度
    @Column(name = "pconfig_condition_maxamount")
    private BigDecimal conditionMaxAmount;

    //奖励豆子
    @Column(name = "pconfig_reward_peas")
    private Integer rewardPeas;

    //奖励金钱
    @Column(name = "pconfig_reward_money")
    private BigDecimal rewardMoney;

    //影响的等级差
    @Column(name = "pconfig_reward_diffgrade")
    private Integer rewardDiffGrade;

    //相差深度
    @Column(name = "pconfig_reward_level")
    private Integer rewardLevel;

    @Column(name = "update_at")
    private Timestamp updateAt;
    @Column(name = "create_at")
    private Timestamp createAt;

}
