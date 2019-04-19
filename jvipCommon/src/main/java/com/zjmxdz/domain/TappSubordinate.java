package com.zjmxdz.domain;
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
    private String userid;

    //下级
    @Column(name = "subordinate_suborinateuserid")
    private String subordinateUserid;

    //深度
    @Column(name = "subordinate_level")
    private Integer level;

    //邀请人数
    @Column(name = "subordinate_invitationnum")
    private Integer invitationNum;

    @Column(name = "update_at")
    private Timestamp updateAt;
    @Column(name = "create_at")
    private Timestamp createAt;

}
