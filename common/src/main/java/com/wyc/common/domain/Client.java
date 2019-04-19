package com.wyc.common.domain;

import com.wyc.common.annotation.Condition;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="tcommon_client")
public class Client {
    @Id
    @Column
    private String id;
    @Column(name="user_id")
    @Condition
    private String userId;
    @Column
    private String token;
}
