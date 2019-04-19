package com.zjmxdz.domain.vo;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
@Data
public class PurchaseRecordVo {
    private String id;
    private String userId;
    private String usrename;
    private BigDecimal amount;
    private String goodId;
    private Timestamp buytime;
    private Long integral;
    private Long peas;
}
