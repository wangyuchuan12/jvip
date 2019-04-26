package com.zjmxdz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbasePurchaseConfigVo {
    private String id;
    private BigDecimal conditionLimitAmount;
    private BigDecimal conditionMaxAmount;
    private Integer rewardPeas;
    private BigDecimal rewardMoney;
    private Long rewardIntegral;
    private Integer rewardLevel;
}
