package com.example.henriwilander.harkkaty;


import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReadHallXMLFile {

    private static ReadHallXMLFile fileReader = new ReadHallXMLFile();

    public static ReadHallXMLFile getInstance() {
        return fileReader;
    }

    BookingSystem bookingSystem = BookingSystem.getInstance();

    // When the app is started the data is read from XML and hall objects are created

    public ReadHallXMLFile() {

    }

    public void readFile() {
        try {
            File xmlFile = new File(Environment.getExternalStorageDirectory() + "/hallFile.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("hall");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String id = element.getElementsByTagName("hallID").item(0).getTextContent();
                    int newID = Integer.parseInt(id);
                    String capacity = element.getElementsByTagName("capacity").item(0).getTextContent();
                    int newCapacity = Integer.parseInt(capacity);
                    String purpose = element.getElementsByTagName("purpose").item(0).getTextContent();
                    String status = element.getElementsByTagName("status").item(0).getTextContent();
                    int newStatus = Integer.parseInt(status);
                    if(purpose.contains(",")) {
                        String[] purposes = purpose.split(",");
                        bookingSystem.createMultiPurposeHall(name, newCapacity, purposes,true, newStatus);
                    } else {
                        bookingSystem.createNewOnePurposeHall(name, purpose, newCapacity,true, newStatus);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | NullPointerException | SAXException
                e) {
            e.printStackTrace();
        }
    }

}
