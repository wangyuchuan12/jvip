package com.zjmxdz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVo {
    private String id;
    private String name;
    private BigDecimal amount;
    private String account;
}
