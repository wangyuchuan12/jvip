package com.wyc.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyc.common.annotation.ExcelColumn;
import com.wyc.common.annotation.ExcelTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static <T> List<T> read(Class<T> type, InputStream inputStream,int sheetNum,int endsheetNum)throws Exception{

        ExcelTable excelTable = type.getAnnotation(ExcelTable.class);

        Integer start = excelTable.start();

        Workbook wb = null;

        try {
            wb = new HSSFWorkbook(inputStream);
        }catch (Exception e){
            wb = new XSSFWorkbook(inputStream);
        }

        List<T> list = new ArrayList<>();

        for(int j = sheetNum;j<endsheetNum;j++) {
            try {
                Sheet sheet = wb.getSheetAt(j);
                Field[] fields = type.getDeclaredFields();
                Map<Field, ExcelColumn> fieldMap = new HashMap<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    if (CommonUtil.isNotEmpty(excelColumn)) {
                        fieldMap.put(field, excelColumn);
                    }
                }

                Integer rowNum = sheet.getLastRowNum()+1;
                for (int i = start; i < rowNum; i++) {
                    T t = type.newInstance();
                    Row xssfRow = sheet.getRow(i);
                    for (Map.Entry<Field, ExcelColumn> filedEntry : fieldMap.entrySet()) {
                        Field field = filedEntry.getKey();
                        ExcelColumn excelColumn = filedEntry.getValue();
                        int index = excelColumn.index();
                        Cell xssfCell = xssfRow.getCell(index);
                        String value = null;
                        try {
                            value = xssfCell.getStringCellValue();
                        } catch (Exception e) {
                            try {
                                Double number = xssfCell.getNumericCellValue();
                                Long longNum = number.longValue();
                                value = String.valueOf(longNum);
                            } catch (Exception e2) {

                            }
                        }
                        Class<?> fieldType = field.getType();
                        if (fieldType.equals(String.class)) {
                            field.set(t, value);
                        } else if (fieldType.equals(BigDecimal.class)) {
                            field.set(t, new BigDecimal(value));
                        }
                    }
                    list.add(t);
                }
            }catch (Exception e){

            }
        }
        return list;
    }
}
