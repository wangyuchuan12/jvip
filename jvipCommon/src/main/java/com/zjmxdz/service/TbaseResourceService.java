package com.zjmxdz.service;

import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TbaseResourceDao;
import com.zjmxdz.domain.TbaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbaseResourceService extends BaseAbstractService<TbaseResource> {
    @Autowired
    public TbaseResourceService(TbaseResourceDao jpaRepository) {
        super(jpaRepository);
    }
}
