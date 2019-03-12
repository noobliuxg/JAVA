package cn.com.java.excel.usermodel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class ExcelUtils {

    public static XSSFWorkbook generateWorkBookByXssf(String excelPath) throws Exception {
        //文件
        OPCPackage opcPackage = OPCPackage.open(excelPath);//XSSFWorkbook ,file not more memory
        opcPackage = OPCPackage.open(new FileInputStream(excelPath));//XSSFWorkbook, InputStream, needs more memory
        XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
        opcPackage.close();

        return workbook;
    }

    public static HSSFWorkbook generateWorkBookByHssf(String excelPath) throws Exception{
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new File(excelPath));
        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem.getRoot(),false);

        poifsFileSystem = new POIFSFileSystem(new FileInputStream(excelPath));
        workbook = new HSSFWorkbook(poifsFileSystem.getRoot(),true);
        return workbook;
    }
}
