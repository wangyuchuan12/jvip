package com.zjmxdz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbaseIntegralConfigVo {
    private String id;

    private Integer integral;

    private Integer gradle;

    private BigDecimal conditionLimitAmount;

    private BigDecimal conditionMaxAmount;
}
