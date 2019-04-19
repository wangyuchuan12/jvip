package com.zjmxdz.domain.dto;
import com.zjmxdz.domain.TbasePurchaseConfig;
import lombok.Data;

import java.util.List;

@Data
public class TbasePurchaseConfigDto extends TbasePurchaseConfig {
    private List<Integer> CONDITION_GRADE_IN;
}
