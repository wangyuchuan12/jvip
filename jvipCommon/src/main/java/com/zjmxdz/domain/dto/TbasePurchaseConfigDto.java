package com.zjmxdz.domain.dto;
import com.zjmxdz.domain.TbasePurchaseConfig;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TbasePurchaseConfigDto extends TbasePurchaseConfig {

    private List<Integer> CONDITION_GRADE_IN;

    private BigDecimal CONDITION_LIMIT_AMOUNT;

    private BigDecimal CONDITION_MAX_AMOUNT;
}
