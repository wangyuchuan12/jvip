package com.zjmxdz.domain.vo;

import com.wyc.common.annotation.ExcelColumn;
import com.wyc.common.annotation.ExcelTable;
import lombok.Data;

@Data
@ExcelTable(start = 1)
public class ImportUserData {
    @ExcelColumn(index = 0)
    private String index;
    @ExcelColumn(index = 1)
    private String account;
    @ExcelColumn(index = 2)
    private String name;
    @ExcelColumn(index = 3)
    private String department;
    @ExcelColumn(index = 4)
    private String post;
    @ExcelColumn(index = 5)
    private String referencesAccount;
}
