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
@Table(name="tbase_resource")
public class TbaseResource {
    @Id
    @Column(name = "resource_id")
    private String id;

    @Column(name = "resource_filepath")
    private String filePath;

    @Column(name = "resource_name")
    private String name;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;
    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
