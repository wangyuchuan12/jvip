package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbasePurchaseConfigDao;
import com.zjmxdz.domain.TbasePurchaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbasePurchaseConfigService  extends BaseAbstractService<TbasePurchaseConfig> {

    @Autowired
    public TbasePurchaseConfigService(TbasePurchaseConfigDao tbasePurchaseConfigDao) {
        super(tbasePurchaseConfigDao);
    }
}
