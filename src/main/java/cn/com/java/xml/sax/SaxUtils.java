package cn.com.java.xml.sax;

import cn.com.java.xml.Book;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SaxUtils {

    public static void main(String[] args) throws ParserConfigurationException, SAXException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();//获取SAXParserFactory

        SAXParser saxParser = parserFactory.newSAXParser();

        try {
            saxParser.parse(SaxUtils.class.getClassLoader().getResourceAsStream("book.xml"),new ContentHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ContentHandler extends DefaultHandler{

        private Book book;
        private String value;
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            System.out.println("开始SAX解析");
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            System.out.println("结束SAX解析");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals("book")){
                book = new Book();
                book.setId(attributes.getValue("id"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            switch (qName){
                case "name":
                    book.setName(value);
                    break;
                case "author":
                    book.setAuthor(value);
                    break;
                case "year":
                    book.setYear(value);
                    break;
                case "price":
                    book.setPrice(value);
                    break;
                case "language":
                    book.setLanguage(value);
                    break;
            }
            if (qName.equals("book")){
                System.out.println(book);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            value = new String(ch,start,length);
        }
    }
}
