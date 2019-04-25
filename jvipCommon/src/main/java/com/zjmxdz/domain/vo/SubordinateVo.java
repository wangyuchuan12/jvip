package com.zjmxdz.domain.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SubordinateVo implements Serializable {
    private String id;
    //深度
    private Integer level;
    //下级用户
    private String subordinateUserid;
    //下级用户名称
    private String subordinateUsername;

    private String subordinateAccount;

    private String refereeUserId;

    //邀约人名称
    private String refereeName;

    //用户id
    private String userid;
    //用户
    private String name;
    //手机号码
    private String subordinatephonenumber;

    //等级
    private String subordinatGrade;

    //积分
    private String subordinateintegral;
    //总共金额
    private BigDecimal totalAmount;
    //邀请人数
    private Integer invitationnum;
    //购买金额
    private BigDecimal amount;

}
