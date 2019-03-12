package cn.com.java.xml.dome4j;

import cn.com.java.xml.Book;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.Iterator;

public class Dom4jUtils {

    public static void main(String[] args) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(Dom4jUtils.class.getClassLoader().getResource("book.xml"));
        Element rootElement = document.getRootElement();

        Iterator<Element> iterator = rootElement.elementIterator("book");
        while (iterator.hasNext()){
            Element element = iterator.next();
            Book book = new Book();
            book.setId(element.attribute("id").getValue());
            Iterator<Element> elementIterator = element.elementIterator();
            while (elementIterator.hasNext()){
                Element attrEle = elementIterator.next();
                switch (attrEle.getName()){
                    case "name":
                        book.setName(attrEle.getText().trim());
                        break;
                    case "author":
                        book.setAuthor(attrEle.getText().trim());
                        break;
                    case "year":
                        book.setYear(attrEle.getText().trim());
                        break;
                    case "price":
                        book.setPrice(attrEle.getText().trim());
                        break;
                    case "language":
                        book.setLanguage(attrEle.getText().trim());
                        break;
                }
            }
            System.out.println(book);
        }
    }
}
