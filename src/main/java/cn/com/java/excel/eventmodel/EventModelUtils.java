package cn.com.java.excel.eventmodel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventModelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventModelUtils.class);
    public static void processFirstSheet(String fileName){
        try {
            OPCPackage opcPackage = OPCPackage.open(fileName, PackageAccess.READ);

            XSSFReader reader = new XSSFReader(opcPackage);

            SharedStringsTable stringsTable = reader.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(stringsTable);

            InputStream inputStream = reader.getSheetsData().next();
            InputSource inputSource = new InputSource(inputStream);
            parser.parse(inputSource);
        } catch (Exception e) {
            LOGGER.error("",e);
        }
    }

    private static XMLReader fetchSheetParser(SharedStringsTable stringsTable) throws ParserConfigurationException, SAXException {
        XMLReader xmlReader = SAXHelper.newXMLReader();//获取sax xml 读取器

        ContentHandler handler = new SheetHandler(stringsTable);//获取内容适配器

        xmlReader.setContentHandler(handler);

        return xmlReader;
    }

    /**
     * 依次调用：startElement，characters，endElement
     */
    static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sharedStringsTable;
        private String lastContents;
        private boolean nextIsString;
        private Map<Integer,String> cacheMap = new HashMap<>();

        private int sheetIndex = -1;
		private int curRow = 0;
		private int curCol = 0;
		private List<String> rowlist = new ArrayList<>();

        public SheetHandler(SharedStringsTable stringsTable) {
            this.sharedStringsTable = stringsTable;
        }

        public void optRow(int sheetIndex, int curRow, List<String> rowList){}

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            //c => Cell 单元格
            if (qName.equals("c")){
                if (LOGGER.isDebugEnabled()){
                    //print the Cell reference
                    //打印单元格的位置 如：A1,B1
                    LOGGER.debug(attributes.getValue("r")+"-");
                    //Figure out if the value is an index in the SST 如果下一个元素是 SST 的索引，则将nextIsString标记为true
                    //单元格类型
                    String cellType = attributes.getValue("t");
                    //cellType值 s:字符串 b:布尔 e:错误处理
                    if (StringUtils.equals("s",cellType)){
                        //标识为true 交给后续endElement处理
                        nextIsString = true;
                    }else {
                        nextIsString = false;
                    }
                }
            }
            //clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString){
                int index = Integer.parseInt(lastContents);
                lastContents = cacheMap.get(index);
                //如果内容为空 或者Cache中存在相同key 不保存到Cache中
                if (lastContents==null && !cacheMap.containsKey(index)){
                    lastContents = sharedStringsTable.getItemAt(index).getString();
                    cacheMap.put(index,lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if (qName.equals("v")){
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(lastContents);
                }
                rowlist.add(curCol++,lastContents);
            }else{
                if (qName.equals("row")){
                    //如果标签名称为 row , 已到行尾
                    if (LOGGER.isDebugEnabled()){
                        LOGGER.debug(cacheMap.size()+"");
                    }
                    optRow(sheetIndex,curRow,rowlist);
                    curRow++;
                    cacheMap.clear();
                }
            }
        }

        /**
         * 得到单元格对应的索引值或是内容值
         * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值;如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
         *
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents += new String(ch,start,length);
        }
    }
}
