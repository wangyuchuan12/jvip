package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbaseIntegralConfigDao;
import com.zjmxdz.domain.TbaseIntegralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbaseIntegralConfigService extends BaseAbstractService<TbaseIntegralConfig> {
    @Autowired
    public TbaseIntegralConfigService(TbaseIntegralConfigDao jpaRepository) {
        super(jpaRepository);
    }
}
