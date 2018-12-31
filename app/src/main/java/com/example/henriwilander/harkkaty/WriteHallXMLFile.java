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

public class WriteHallXMLFile {

    private static WriteHallXMLFile fileWriter = new WriteHallXMLFile();

    public static WriteHallXMLFile getInstance() {
        return fileWriter;
    }

    public WriteHallXMLFile() {

    }

    public void writeNewFile(int hallID, String hallName, String purpose_, int capacity_, int status_) {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        {
            try {
                documentBuilder = documentFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                Element root = document.createElement("halls");
                document.appendChild(root);

                Element hall= document.createElement("hall");
                root.appendChild(hall);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(hallName));
                hall.appendChild(name);

                Element purpose = document.createElement("purpose");
                purpose.appendChild(document.createTextNode(purpose_));
                hall.appendChild(purpose);

                Element gymID = document.createElement("hallID");
                gymID.appendChild(document.createTextNode(Integer.toString(hallID)));
                hall.appendChild(gymID);

                Element capacity = document.createElement("capacity");
                capacity.appendChild(document.createTextNode(Integer.toString(capacity_)));
                hall.appendChild(capacity);

                Element status = document.createElement("status");
                status.appendChild(document.createTextNode(Integer.toString(status_)));
                hall.appendChild(status);

                // create the xml file
                //transform the DOM Object to an XML File
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(Environment.getExternalStorageDirectory() + "/hallFile.xml");
                transformer.transform(domSource, streamResult);

            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }
    // If XML-file already exists, new information are only added to the file
    public void WriteToExistingXML(int hallID, String hallName, String purpose_, int capacity_, int status_) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = null;
            document = documentBuilder.parse((new File(Environment.getExternalStorageDirectory() + "/hallFile.xml")));

            Node root = document.getFirstChild();

            Element hall= document.createElement("hall");

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(hallName));
            hall.appendChild(name);

            Element purpose = document.createElement("purpose");
            purpose.appendChild(document.createTextNode(purpose_));
            hall.appendChild(purpose);

            Element gymID = document.createElement("hallID");
            gymID.appendChild(document.createTextNode(Integer.toString(hallID)));
            hall.appendChild(gymID);

            Element capacity = document.createElement("capacity");
            capacity.appendChild(document.createTextNode(Integer.toString(capacity_)));
            hall.appendChild(capacity);

            Element status = document.createElement("status");
            status.appendChild(document.createTextNode(Integer.toString(status_)));
            hall.appendChild(status);

            root.appendChild(hall);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(Environment.getExternalStorageDirectory() + "/hallFile.xml");
            transformer.transform(domSource, streamResult);

        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

}