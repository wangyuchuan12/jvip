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
    private String refereeAccount;
    private String refereeUserName;
    private BigDecimal amount;
    private BigDecimal totalAmount;
    private Long integral;
    private Integer grade;
    private Integer peas;
    private Integer subordinateNum;
    private Integer orderNum;

    private List<GradleNumVo> gradleNums;
}
