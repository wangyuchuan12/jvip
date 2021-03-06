package com.zjmxdz.domain;
import com.wyc.common.annotation.Condition;
import com.wyc.common.annotation.CreateAt;
import com.wyc.common.annotation.UpdateAt;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="tapp_subordinate")
public class TappSubordinate {
    @Id
    @Column(name = "subordinate_id")
    private String id;

    //当前用户
    @Column(name = "subordinate_userid")
    @Condition
    private String userid;

    //下级
    @Column(name = "subordinate_suborinateuserid")
    @Condition
    private String subordinateUserid;

    //深度
    @Column(name = "subordinate_level")
    @Condition
    private Integer level;

    //邀请人数
    @Column(name = "subordinate_invitationnum")
    private Integer invitationNum;

    //邀约人id
    @Column(name = "subordinate_refereeuserid")
    private String refereeUserId;

    //邀约人名称
    @Column(name = "subordinate_refereename")
    private String refereeName;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;

}
