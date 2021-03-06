package com.zjmxdz.domain;


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
@Table(name="tbase_grade_config")
public class TbaseGradeConfig {
    @Id
    @Column(name = "levelconfig_id")
    private String id;

    @Column(name = "levelconfig_integral")
    private Long integral;

    @Column(name = "levelconfig_grade")
    private Integer grade;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
