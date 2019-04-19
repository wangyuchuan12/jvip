package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TappOrderDao;
import com.zjmxdz.domain.TappOrder;
import com.zjmxdz.domain.TappPurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TappOrderService extends BaseAbstractService<TappOrder> {
    @Autowired
    private TappOrderDao tappOrderDao;
    public TappOrderService(TappOrderDao tappOrderDao) {
        super(tappOrderDao);
    }
}
