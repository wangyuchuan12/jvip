package com.zjmxdz.domain.dto;

import com.zjmxdz.domain.TbaseIntegralConfig;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbaseIntegralConfigDto extends TbaseIntegralConfig {
    private BigDecimal CONDITION_LIMIT_AMOUNT;
    private BigDecimal CONDITION_MAX_AMOUNT;
}
