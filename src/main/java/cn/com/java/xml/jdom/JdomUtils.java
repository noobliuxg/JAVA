package cn.com.java.xml.jdom;


import cn.com.java.xml.Book;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.List;

public class JdomUtils {
    public static void main(String[] args) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();


        Document document = saxBuilder.build(JdomUtils.class.getClassLoader().getResource("book.xml"));

        Element element = document.getRootElement();
        System.out.println(element.getName());

        List list = element.getChildren("book");

        for (int i=0;i<list.size();i++){
            Element bookEle = (Element)list.get(i);
            Book book = new Book();
            book.setId(bookEle.getAttribute("id").getValue());
            List children = bookEle.getChildren();
            for (int j=0;j<children.size();j++){
                Element attrEle = (Element)children.get(j);
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
