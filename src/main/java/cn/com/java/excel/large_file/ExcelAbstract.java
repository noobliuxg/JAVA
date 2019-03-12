package cn.com.java.excel.large_file;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ExcelAbstract extends DefaultHandler {

    private SharedStringsTable table;

    private String lastContents;

    private boolean nextIsstring;

    private int curRow = 0;

    private String curCellName = "";

    protected Map<String,String> rowValueMap = new HashMap<>();

    public abstract void optRows(int curRow,Map<String,String> rowValueMap);

    public void readSheet(String filePath,int sheetNum) throws Exception{

        OPCPackage aPackage = OPCPackage.open(filePath);

        XSSFReader xssfReader = new XSSFReader(aPackage);//创建XSSFReader读取器

        SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();//获取SharedStringsTable

        XMLReader parser = getSheetParser(sharedStringsTable);//根据SharedStringsTable获取XMlReader读取器

        InputStream sheet = xssfReader.getSheet("rId" + sheetNum);//根据XSSFReader获取指定的Sheet

        InputSource source = new InputSource(sheet);//根据指定的sheet的InputStream文件

        parser.parse(source);//XMLReader解析sheet对应的InputSource

        sheet.close();//关闭资源

        aPackage.close();//关闭资源
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //c ==> 单元格
        if (qName.equals("c")){
            //如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            if (StringUtils.equals("s",cellType)){
                nextIsstring = true;
            }else{
                nextIsstring = false;
            }
        }

        //置空
        lastContents = "";

        String cellName = attributes.getValue("r");
        if (StringUtils.isNotEmpty(cellName)){
            curCellName = cellName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsstring){
            int index = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(table.getEntryAt(index)).toString();
        }

        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if (qName.equals("v")) {
            String value = lastContents.trim();
            value = value.equals("") ? " " : value;
            rowValueMap.put(curCellName, value);
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (qName.equals("row")) {
                optRows(curRow, rowValueMap);
                rowValueMap.clear();
                curRow++;
            }
        }
    }

    public void characters(char[] chars,int start,int length) {
        lastContents += new String(chars, start, length);
    }

    private XMLReader getSheetParser(SharedStringsTable sharedStringsTable) throws SAXException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

        this.table = sharedStringsTable;

        xmlReader.setContentHandler(this);

        return xmlReader;
    }
}
