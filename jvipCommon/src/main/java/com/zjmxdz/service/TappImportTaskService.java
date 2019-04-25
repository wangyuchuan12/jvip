package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TappImportTaskDao;
import com.zjmxdz.domain.TappImportTask;
import com.zjmxdz.domain.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TappImportTaskService extends BaseAbstractService<TappImportTask> {
    @Autowired
    public TappImportTaskService(TappImportTaskDao importTaskDao) {
        super(importTaskDao);
    }


    public List<TappImportTask> findAllTasks(int page,int size){
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("task_id as id,");
        sb.append("task_filename as filename,");
        sb.append("task_path as filePath,");
        sb.append("task_status as status,");
        sb.append("task_starttime as startTime,");
        sb.append("task_completetime as completeTime,");
        sb.append("task_username as username,");
        sb.append("task_type as type,");
        sb.append("create_at as createAt");
        sb.append(" from tapp_import_task  order by create_at desc limit "+page+","+size);
        List<TappImportTask> tasks = findAll(TappImportTask.class,sb.toString());
        return tasks;
    }

    public List<OrderVo> findAllOrders(String account, int page, int size) {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("order_id as id,");
        sb.append("order_name as name,");
        sb.append("order_amount as amount,");
        sb.append("order_account as account");
        sb.append(" from tapp_order  where order_account='"+account+"'");
        sb.append(" order by create_at desc limit "+page+","+size);
        List<OrderVo> orders = findAll(OrderVo.class,sb.toString());
        return orders;
    }
}
