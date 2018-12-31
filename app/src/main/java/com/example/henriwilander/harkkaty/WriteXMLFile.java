package com.example.henriwilander.harkkaty;
import android.os.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WriteXMLFile {

    private static WriteXMLFile fileWriter = new WriteXMLFile();

    public static WriteXMLFile getInstance() {
        return fileWriter;
    }

    public WriteXMLFile () {

    }

    public void writeNewFile(int reservationID, int hallID, String date, String time, String userSurname, String specialFeature, String genre, String reservationType, int userID, int maxCapacity, int registrationCount) {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        Integer.toString(reservationID);
        {
            try {
                documentBuilder = documentFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                Element root = document.createElement("reservations");
                document.appendChild(root);

                Element reservation= document.createElement("reservation");
                root.appendChild(reservation);

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(reservationType));
                reservation.appendChild(type);

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(Integer.toString(reservationID)));
                reservation.appendChild(id);

                Element gymID = document.createElement("hallID");
                gymID.appendChild(document.createTextNode(Integer.toString(hallID)));
                reservation.appendChild(gymID);

                Element uID = document.createElement("userID");
                uID.appendChild(document.createTextNode(Integer.toString(userID)));
                reservation.appendChild(uID);

                Element reservationDate = document.createElement("date");
                reservationDate.appendChild(document.createTextNode(date));
                reservation.appendChild(reservationDate);

                Element reservationTime = document.createElement("time");
                reservationTime.appendChild(document.createTextNode(time));
                reservation.appendChild(reservationTime);

                Element surname = document.createElement("userSurname");
                surname.appendChild(document.createTextNode(userSurname));
                reservation.appendChild(surname);

                String tagName = "organizer";
                if(reservationType.equals("Group Reservation")) {
                    Element special = document.createElement(tagName);
                    special.appendChild(document.createTextNode(specialFeature));
                    reservation.appendChild(special);

                    Element sport = document.createElement("genre");
                    sport.appendChild(document.createTextNode(genre));
                    reservation.appendChild(sport);

                    Element capacity = document.createElement("capacity");
                    capacity.appendChild(document.createTextNode(Integer.toString(maxCapacity)));
                    reservation.appendChild(capacity);
                } else {
                    Element sport = document.createElement("genre");
                    sport.appendChild(document.createTextNode(genre));
                    reservation.appendChild(sport);

                    Element capacity = document.createElement("capacity");
                    capacity.appendChild(document.createTextNode(Integer.toString(maxCapacity)));
                    reservation.appendChild(capacity);

                    Element registrations = document.createElement("registrations");
                    registrations.appendChild(document.createTextNode(Integer.toString(registrationCount)));
                    reservation.appendChild(registrations);
                }
                // create the xml file
                //transform the DOM Object to an XML File
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(Environment.getExternalStorageDirectory() + "/reservationFile"+hallID+".xml");
                transformer.transform(domSource, streamResult);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }
    // muuta niin ettei tyhjää kirjoiteta tiedostoon.
    public void WriteToExistingXML(int reservationID, int hallID, String date, String time, String userSurname, String specialFeature, String genre, String reservationType, int userID, int maxCapacity, int registrationCount) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = null;
            document = documentBuilder.parse((new File(Environment.getExternalStorageDirectory() + "/reservationFile"+hallID+".xml")));

            Node root = document.getFirstChild();

            Element reservation= document.createElement("reservation");

            Element type= document.createElement("type");
            type.appendChild(document.createTextNode(reservationType));
            reservation.appendChild(type);

            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(Integer.toString(reservationID)));
            reservation.appendChild(id);

            Element gymID = document.createElement("hallID");
            gymID.appendChild(document.createTextNode(Integer.toString(hallID)));
            reservation.appendChild(gymID);

            Element uID = document.createElement("userID");
            uID.appendChild(document.createTextNode(Integer.toString(userID)));
            reservation.appendChild(uID);

            Element reservationDate = document.createElement("date");
            reservationDate.appendChild(document.createTextNode(date));
            reservation.appendChild(reservationDate);

            Element reservationTime = document.createElement("time");
            reservationTime.appendChild(document.createTextNode(time));
            reservation.appendChild(reservationTime);

            Element surname = document.createElement("userSurname");
            surname.appendChild(document.createTextNode(userSurname));
            reservation.appendChild(surname);

            String tagName = "organizer";
            if(reservationType.equals("Group Reservation")) {
                Element special = document.createElement(tagName);
                special.appendChild(document.createTextNode(specialFeature));
                reservation.appendChild(special);

                Element sport = document.createElement("genre");
                sport.appendChild(document.createTextNode(genre));
                reservation.appendChild(sport);

                Element capacity = document.createElement("capacity");
                capacity.appendChild(document.createTextNode(Integer.toString(maxCapacity)));
                reservation.appendChild(capacity);

                root.appendChild(reservation);
            } else {

                Element sport = document.createElement("genre");
                sport.appendChild(document.createTextNode(genre));
                reservation.appendChild(sport);

                Element capacity = document.createElement("capacity");
                capacity.appendChild(document.createTextNode(Integer.toString(maxCapacity)));
                reservation.appendChild(capacity);

                Element registrations = document.createElement("registrations");
                registrations.appendChild(document.createTextNode(Integer.toString(registrationCount)));
                reservation.appendChild(registrations);

                root.appendChild(reservation);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(Environment.getExternalStorageDirectory() + "/reservationFile"+hallID+".xml");
            transformer.transform(domSource, streamResult);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

}