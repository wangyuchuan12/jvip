package com.zjmxdz.service;
import com.wyc.common.service.BaseAbstractService;
import com.zjmxdz.dao.TappPurchaseRecordDao;
import com.zjmxdz.domain.TappPurchaseRecord;
import com.zjmxdz.domain.vo.PurchaseRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TappPurchaseRecordService extends BaseAbstractService<TappPurchaseRecord> {
    @Autowired
    private TappPurchaseRecordDao tappPurchaseRecordDao;
    public TappPurchaseRecordService(TappPurchaseRecordDao tappPurchaseRecordDao) {
        super(tappPurchaseRecordDao);
    }

    public List<PurchaseRecordVo> records(String userId) {

        StringBuffer sb = new StringBuffer();

        sb.append("select");
        sb.append(" purcharserecord_id as id,");
        sb.append("purcharserecord_userid as userid,");
        sb.append("purcharserecord_amount as amount,");
        sb.append("purcharserecord_goodid as goodId,");
        sb.append("purcharserecord_goodname as goodname,");
        sb.append("purcharserecord_integral as integral,");
        sb.append("purcharserecord_peas as peas ");
        sb.append("from tapp_purchase_record where ");
        sb.append(" purcharserecord_userid="+userId);
        List<PurchaseRecordVo> purchaseRecordVos = findAll(PurchaseRecordVo.class,sb.toString());
        return purchaseRecordVos;
    }
}
