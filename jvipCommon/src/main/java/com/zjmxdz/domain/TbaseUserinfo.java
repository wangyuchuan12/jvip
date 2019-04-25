package com.zjmxdz.domain;

import com.wyc.common.annotation.Condition;
import com.wyc.common.annotation.Conditions;
import com.wyc.common.annotation.CreateAt;
import com.wyc.common.annotation.UpdateAt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.parser.Part;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="tbase_userinfo")
public class TbaseUserinfo implements Serializable {
    //id
    @Id
    @Column(name = "userinfo_id")
    private String id;
    //用户名
    @Column(name = "userinfo_username")
    @Condition
    private String username;
    //手机号码
    @Column(name = "userinfo_phonenumber")
    private String phonenumber;

    //总共购买金额
    @Column(name = "userinfo_totalamount")
    private BigDecimal totalAmount;

    //姓名
    @Column(name = "userinfo_name")
    @Conditions({@Condition,@Condition(properties="EQUALS_NAME",type = Part.Type.SIMPLE_PROPERTY)})
    private String name;
    //密码
    @Conditions({@Condition,@Condition(properties="EQUALS_PASSWORD",type = Part.Type.SIMPLE_PROPERTY)})
    @Column(name = "userinfo_password")
    private String password;
    //推荐人
    @Column(name = "userinfo_refereeeuseraccount")
    @Condition
    private String refereeAccount;
    @Column(name = "userinfo_amount")
    private BigDecimal amount;
    //分数
    @Column(name = "userinfo_integral")
    private Long integral;
    //等级
    @Column(name = "userinfo_grade")
    private Integer grade;
    //豆豆
    @Column(name = "userinfo_peas")
    private Integer peas;
    //邀请人数
    @Column(name = "userinfo_invitationnum")
    private Integer invitationnum;
    @Column(name = "userinfo_taskid")
    @Condition
    private String taskId;
    //有层级
    @Column(name = "userinfo_ishierarchy")
    @Condition
    private Integer isHierarchy;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
