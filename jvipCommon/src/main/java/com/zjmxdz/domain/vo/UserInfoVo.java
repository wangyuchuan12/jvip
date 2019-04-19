package com.zjmxdz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserInfoVo {
    private String id;
    private String username;
    private String phonenumber;
    private String name;
    private String refereeUserId;
    private String refereeUserName;
    private BigDecimal amount;
    private Long integral;
    private Integer grade;
    private Integer peas;
}