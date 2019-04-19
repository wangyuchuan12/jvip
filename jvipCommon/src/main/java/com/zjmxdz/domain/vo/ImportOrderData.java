package com.zjmxdz.domain.vo;


import com.wyc.common.annotation.ExcelColumn;
import com.wyc.common.annotation.ExcelTable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ExcelTable(start = 1)
public class ImportOrderData {
    @ExcelColumn(index = 0)
    private String account;
    @ExcelColumn(index = 2)
    private String referencesId;
    @ExcelColumn(index = 3)
    private BigDecimal amount;
}
