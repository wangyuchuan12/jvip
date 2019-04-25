package com.zjmxdz.domain;


import com.wyc.common.annotation.Condition;
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
@Table(name="tapp_order")
public class TappOrder {
    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_amount")
    private BigDecimal amount;

    @Column(name = "order_refereeuseraccount")
    private String refereeAccount;

    @Column(name = "order_account")
    private String account;

    @Column(name = "order_status")
    private Integer status;

    @Column(name = "order_remarks")
    private String remarks;

    //错误类型 0表示没有导入用户
    @Column(name = "order_errortype")
    private Integer errortype;

    @Column(name = "order_taskid")
    @Condition
    private String taskId;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;

    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
