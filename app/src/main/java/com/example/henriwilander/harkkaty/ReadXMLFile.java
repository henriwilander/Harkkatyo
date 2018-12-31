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

public class ReadXMLFile {

    private static ReadXMLFile fileReader = new ReadXMLFile();

    public static ReadXMLFile getInstance() {
        return fileReader;
    }

    BookingSystem bookingSystem = BookingSystem.getInstance();

    public ReadXMLFile() {

    }

    // This method read XML-files and initialize reservationObjects.
    // Index is same as hall id and different files are read by using these ids.

    public void readFile(int index) {
        String textToListView = "";
        try {
            File xmlFile = new File(Environment.getExternalStorageDirectory() + "/reservationFile" + index + ".xml");
            String specialFeature = "";
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("reservation");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    int newID = Integer.parseInt(id);
                    String hallID = element.getElementsByTagName("hallID").item(0).getTextContent();
                    int newhallD = Integer.parseInt(hallID);
                    String userID = element.getElementsByTagName("userID").item(0).getTextContent();
                    int newUserID = Integer.parseInt(userID);
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    String time = element.getElementsByTagName("time").item(0).getTextContent();
                    String userSurname = element.getElementsByTagName("userSurname").item(0).getTextContent();
                    String genre = element.getElementsByTagName("genre").item(0).getTextContent();
                    String capacity = element.getElementsByTagName("capacity").item(0).getTextContent();
                    int newCapacity = Integer.parseInt(capacity);
                    switch (type) {
                        case "Group Reservation":
                            specialFeature = element.getElementsByTagName("organizer").item(0).getTextContent();
                                bookingSystem.findHallById(index).createNewGroupReservation(date, time, userSurname, specialFeature, genre, newUserID,true);

                            break;
                        case "Common Reservation":
                                String registrations = element.getElementsByTagName("registrations").item(0).getTextContent();
                                int newRegistrations = Integer.parseInt(registrations);
                                bookingSystem.findHallById(index).createNewCommonReservation(date, time, userSurname, genre, newUserID, newRegistrations, true);
                            break;
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | NullPointerException | SAXException
                e) {
            e.printStackTrace();
        }
    }
}