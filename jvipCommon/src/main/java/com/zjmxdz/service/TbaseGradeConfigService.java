package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbaseLevelConfigDao;
import com.zjmxdz.domain.TbaseGradeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbaseGradeConfigService extends BaseAbstractService<TbaseGradeConfig> {
    @Autowired
    public TbaseGradeConfigService(TbaseLevelConfigDao jpaRepository) {
        super(jpaRepository);
    }

    public TbaseGradeConfig getMaxByIntegral(Long integral){
        StringBuffer sb = new StringBuffer();
        sb.append("select levelconfig_id as id,levelconfig_integral as integral,levelconfig_grade grade from tbase_level_config where levelconfig_integral>"+integral+" order by levelconfig_integral desc");
        List<TbaseGradeConfig> tbaseGradeConfigs = findAll(TbaseGradeConfig.class,sb.toString());

        if(tbaseGradeConfigs.size()>0){
            return tbaseGradeConfigs.get(0);
        }

        return null;
    }
}
