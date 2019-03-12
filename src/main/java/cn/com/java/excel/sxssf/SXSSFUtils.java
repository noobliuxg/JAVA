package cn.com.java.excel.sxssf;


import cn.com.java.excel.ExcelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SXSSFUtils {

    private static final Integer INIT_MEMORY_SIZE = 100;

    public static void writeExcel(String excelPath, String sheetName, List<Map<Integer,Object>> content) throws Exception {
        writeExcel(INIT_MEMORY_SIZE,excelPath,sheetName,content);
    }

    public static void writeExcel(int memorySize,String excelPath, String sheetName, List<Map<Integer,Object>> content) throws Exception {
        if (StringUtils.isBlank(excelPath)){
            throw new ExcelException("list to excel, but this is excelPath is null");
        }
        if (content==null || content.size()<=0){
            throw new ExcelException("content is null");
        }
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(memorySize);
        SXSSFSheet sheet = sxssfWorkbook.createSheet();

        for (int rowNum = 0; rowNum < content.size(); rowNum++) {
            SXSSFRow sheetRow = sheet.createRow(rowNum);
            for (int cellNum=0;cellNum<=content.get(rowNum).size();cellNum++){
                SXSSFCell sxssfCell = sheetRow.createCell(cellNum);
                setCellValue(sxssfCell,content.get(rowNum).get(cellNum));
            }

            //write 100 record to disk
            if (memorySize<0 && (rowNum % INIT_MEMORY_SIZE ==0)){
                sheet.flushRows();
            }
        }

        FileOutputStream fos = new FileOutputStream(excelPath);
        sxssfWorkbook.write(fos);
        fos.close();

        sxssfWorkbook.dispose();
        sxssfWorkbook.close();
    }

    public static void setCellValue(SXSSFCell cell,Object value){
        if (value instanceof Integer) {
            cell.setCellValue((Integer)value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue(((Float) value).floatValue());
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        }
    }

}
