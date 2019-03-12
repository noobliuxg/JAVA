package cn.com.java.xml.dom;


import cn.com.java.xml.Book;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.List;

public class DomUtils {

    public static void main(String[] args){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//获取DocumentBuilderFactory建筑工程
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();//获取对应的documentBuilder

            URL resource = DomUtils.class.getClassLoader().getResource("book.xml");

            Document document = documentBuilder.parse(resource.getPath());//解析xml获取Document对象

            NodeList nodeList = document.getElementsByTagName("book");
            List<Book> list = null;
            if (nodeList!=null && nodeList.getLength()>=0){
                Book book = null;
                for (int i=0;i<nodeList.getLength();i++){
                    Node node = nodeList.item(i);
                    book = new Book();
                    NamedNodeMap attributes = node.getAttributes();
                    for (int j=0;j<attributes.getLength();j++){
                        book.setId(attributes.getNamedItem("id").getNodeValue());
                    }
                    NodeList childNodes = node.getChildNodes();
                    for(int j=0;j<childNodes.getLength();j++){
                        switch (childNodes.item(j).getNodeName()){
                            case "name":
                                book.setName(childNodes.item(j).getTextContent().trim());
                                break;
                            case "author":
                                book.setAuthor(childNodes.item(j).getTextContent().trim());
                                break;
                            case "year":
                                book.setYear(childNodes.item(j).getTextContent().trim());
                                break;
                            case "price":
                                book.setPrice(childNodes.item(j).getTextContent().trim());
                                break;
                            case "language":
                                book.setLanguage(childNodes.item(j).getTextContent().trim());
                                break;
                        }
                    }
                    System.out.println(book.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
