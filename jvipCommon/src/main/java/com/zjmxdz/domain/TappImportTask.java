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
@Table(name="tapp_import_task")
public class TappImportTask {
    @Id
    @Column(name = "task_id")
    private String id;

    @Column(name = "task_filename")
    private String filename;

    @Column(name = "task_path")
    private String filePath;

    @Column(name = "task_status")
    @Condition
    private Integer status;

    @Column(name = "task_starttime")
    private Timestamp startTime;

    @Column(name = "task_completetime")
    private Timestamp completeTime;

    @Column(name = "task_username")
    private String username;

    @Column(name = "task_type")
    private Integer type;

    @Column(name = "update_at")
    @UpdateAt
    private Timestamp updateAt;

    @Column(name = "create_at")
    @CreateAt
    private Timestamp createAt;
}
