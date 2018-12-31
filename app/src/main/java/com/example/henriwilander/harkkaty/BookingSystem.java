package com.example.henriwilander.harkkaty;
import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BookingSystem {

    ArrayList<CommunityCenter> hallArrayList;
    ArrayList<Integer> hallIdList;

    private static BookingSystem bookingSystem = new BookingSystem();

    public static BookingSystem getInstance() {
        return bookingSystem;
    }

    WriteHallXMLFile fileWriter = WriteHallXMLFile.getInstance();


    public BookingSystem() {
        hallArrayList = new ArrayList();
        hallIdList = new ArrayList();
    }

    public void createNewOnePurposeHall(String name, String purpose, int capacity, boolean readFromFile, int status) {
        int id = hallArrayList.size()+1;
        String file = Environment.getExternalStorageDirectory() + "/hallFile.xml";
        File f = new File(file);

        // When app starts information will be read from the XML-file and the objects are created
        // So, when we are doing so, we don't want to add xml-file anything.
        if(f.exists()) {
            if(readFromFile) {
                hallArrayList.add(new OnePurposeHall(name, id, capacity, purpose, status));
            } else {
                hallArrayList.add(new OnePurposeHall(name, id, capacity, purpose, 1));
                fileWriter.WriteToExistingXML(id, name, purpose, capacity, status);
            }
        } else {
            if(readFromFile) {
                hallArrayList.add(new OnePurposeHall(name, id, capacity, purpose, status));
            } else {
                hallArrayList.add(new OnePurposeHall(name, id, capacity, purpose, 1));
                fileWriter.writeNewFile(id, name, purpose, capacity, status);
            }
        }
    }

    public void createMultiPurposeHall(String name, int capacity, String[] purposes, boolean readFromFile, int status) {
        int id = hallArrayList.size()+1;
        int lastIndex;
        String s = "";
        String file = Environment.getExternalStorageDirectory() + "/hallFile.xml";
        File f = new File(file);

        if(f.exists()) {
            if(readFromFile) {
                hallArrayList.add(new MultiPurposeHall(name, id, capacity, purposes, status));
            } else {
                hallArrayList.add(new MultiPurposeHall(name, id, capacity, purposes,1));

                // We cannot put String[] to the XML-file so we need to convert it to String temporarily
                for(String purpose : purposes) {
                    s = s + purpose + ",";
                }
                lastIndex = s.lastIndexOf(",");
                s = s.substring(0,lastIndex);
                fileWriter.WriteToExistingXML(id, name, s, capacity, status);
            }
        } else {
            if(readFromFile) {
                hallArrayList.add(new MultiPurposeHall(name, id, capacity, purposes, status));
            } else {
                hallArrayList.add(new MultiPurposeHall(name, id, capacity, purposes, 1));
                s = "";
                for(String purpose : purposes) {
                    s = s + purpose + ",";
                }
                lastIndex = s.lastIndexOf(",");
                s = s.substring(0,lastIndex);
                fileWriter.writeNewFile(id, name, s, capacity, status);
            }
        }
    }

    // This method implements the changes in reservations and writes them also to XML-file
    public void implementChanges(String date, String time, String genre, String special, String hall, int id) {
        for(CommunityCenter h : hallArrayList) {
                Reservation reservation = h.findReservationById(id);
                if(reservation != null) {
                    reservation.editDate(date);
                    reservation.editTime(time);
                    reservation.editGenre(genre);
                    if (reservation instanceof GroupReservation) {
                        ((GroupReservation) reservation).editOrganizer(special);
                    }
                }
        }
        findHallByName(hall).implementChangesToXMLFile();
    }

    // This method finds out is the reservation using specific hall
    public boolean findReservationHall(String hall, int id) {
        for(CommunityCenter h : hallArrayList) {
            Reservation reservation = h.findReservationById(id);
            if(reservation != null) {
                int hallID = reservation.getHallID();
                String hallName = findHallById(hallID).getName();
                return hallName.equals(hall);
            }
        }
        return false;
    }

    // This method deletes hall and destroys the XML-file where all reservations of the hall are stored.
    public void deleteHall(int id) {
        String file = Environment.getExternalStorageDirectory() + "/reservationFile" + id + ".xml";
        File file_ = new File(file);
        if (file_.delete()) {
        }
        hallArrayList.remove(findHallById(id));
        if(hallArrayList.size() == 0) {
            String file1 = Environment.getExternalStorageDirectory() + "/hallFile.xml";
            File file_1 = new File(file1);
            if(file_1.delete()) {
            }
        } else {
            implementChangesInHalls();
        }
    }

    // Deletes reservation
    public void executeDeletingMethod(int id) {
        for(CommunityCenter h : hallArrayList) {
            h.deleteReservation(id);
        }
    }

    public ArrayList<CommunityCenter> getHallArrayList() {
        return hallArrayList;
    }

    public int getHallArrayListSize() {
        return hallArrayList.size();
    }

    public CommunityCenter findHallById(int id) {
        for (CommunityCenter g : hallArrayList) {
            if (id == g.getId()) {
                return g;
            }
        }
        return null;
    }

    public CommunityCenter findHallByName(String name) {
        for (CommunityCenter g : hallArrayList) {
            if (name.equals(g.getName())) {
                return g;
            }
        }
        return null;
    }

    public ArrayList<Integer> getHallIdList() {
        hallIdList.clear();
        for (CommunityCenter g : hallArrayList) {
            hallIdList.add(g.getId());
        }
        return hallIdList;
    }

    public ArrayList<Integer> getAllReservationIDs () {
        ArrayList<Integer> hallIDs = getHallIdList();
        ArrayList<Integer> reservationIDs;
        reservationIDs = new ArrayList();
        for(int id : hallIDs) {
            for(int id1 : hallArrayList.get(id-1).getReservationIDList()) {
                reservationIDs.add(id1);
            }
        }
        return reservationIDs;
    }

    // This method implements reservations made by user
    public void implementReservations(int hallID, String date, String time, String userSurname, String genre, String special, int choice, int userID) {
        if (choice == 1) {
            findHallById(hallID).createNewCommonReservation(date, time, userSurname, genre, userID, 1, false);
        } else if (choice == 2) {
            findHallById(hallID).createNewGroupReservation(date, time, userSurname, special, genre, userID, false);
        }
    }

    // This method reads info from list and transfers it to Main.
    public String getHallInformation(){
        String textToListView = "";

        for(CommunityCenter c : hallArrayList) {
            int id = c.getId();
            int capacity = c.getCapacity();
            String name = c.getName();
            int status = c.getStatus();
            if(c instanceof MultiPurposeHall) {
                String s = "";
                int lastIndex;
                String[] purpose = c.getGenres();
                for(String purpose_ : purpose) {
                    s = s + purpose_ + ",";
                }
                lastIndex = s.lastIndexOf(",");
                s = s.substring(0,lastIndex);
                textToListView = textToListView + "Name: "+name+" Purpose(s): "+ s+" Capacity: "+capacity+" ID: "+id+ " Status: "+status+"\n";
            } else {
                String purpose = c.getPurpose();
                textToListView = textToListView + "Name: "+name+" Purpose(s): "+ purpose+" Capacity: "+capacity+" ID: "+id+ " Status: "+ status+"\n";
            }
        }
        return textToListView;
    }

    // This method reads info from list and transfers it to Main.
    public String getData(int index, int userid, int x) {
        String textToListView = "";

        for (Reservation g : findHallById(index).getReservationArrayList()) {
            String type = g.getType();
            int id = g.getResevationId();
            int hallID = g.getHallID();
            int userID = g.getUserID();
            int capacity = g.getCapacity();
            int registrations = 0;
            if(g.getType().equals("Common Reservation")) {
                registrations = ((CommonReservation) g).getRegistrations();
            }
            String date = g.getDate();
            String time = g.getTime();
            String userSurname = g.getUserSurname();
            String genre = g.getGenre();

            if (userid == 0) {
                if (type.equals("Group Reservation")) {
                    String organizer = g.getOrganizer();
                    textToListView = textToListView + findHallById(index).getName() + " " + date + " " + time + " " + type + " Reservation made by: " + userSurname + " Organization: " + organizer + " Genre: " + genre + " " + "reservationID: "+ id+ "\n";
                } else {
                    textToListView = textToListView + findHallById(index).getName() + " " + date + " " + time + " " + type + " Reservation made by: " + userSurname + " Genre: " + genre + " " + "reservationID: "+ id+ " Registrations: "+registrations+"/"+capacity+"\n";
                }
            } else if(userid == 1) {
                textToListView = textToListView + findHallById(index).getName() + " " + date + " " + time + "\n";
            }

            else if (userid != 0 && x != 0) {
                if (type.equals("Group Reservation") && userid == userID) {
                    String organizer = g.getOrganizer();
                    textToListView = textToListView + date + " " + time + " " + type + " Genre: " + genre + " Organization: " + organizer + " Location: " + findHallById(index).getName() + " ReservationID: " + id + "\n";
                } else if (type.equals("Common Reservation") && userid == userID) {
                    textToListView = textToListView + date + " " + time + " " + type + " Genre: " + genre + " Location: " + findHallById(index).getName() + " ReservationID: " + id + "\n";
                }

            } else if(userid != 0 && x == 0) {
                if (type.equals("Group Reservation") && userid == userID) {
                    String organizer = g.getOrganizer();
                    textToListView = textToListView + date + " " + time + " " + type + " " + genre + " "  + organizer+ " "+ findHallById(index).getName() + " " + id + "\n";
                } else if (type.equals("Common Reservation") && userid == userID) {
                    textToListView = textToListView + date + " " + time + " " + type + " " + genre + " " + findHallById(index).getName() + " " + id + "\n";
                }
            }
        }
        return textToListView;
    }

    public void implementChangesInHalls() {
        int x = 0;
        int hallID = 0;
        String hallName = "";
        String purpose_ = "";
        String[] purposes;
        int capacity = 0;
        String s = "";
        int lastIndex;
        int status = 0;

        for(CommunityCenter hall: hallArrayList) {
            hallID = hall.getId();
            hallName = hall.getName();
            status = hall.getStatus();
            if(hall instanceof MultiPurposeHall) {
                purposes = hall.getGenres();
                for(String purpose : purposes) {
                    s = s + purpose + ",";
                }
                lastIndex = s.lastIndexOf(",");
                s = s.substring(0,lastIndex);
                purpose_ = s;
            } else {
                purpose_ = hall.getPurpose();
            }
            capacity=hall.getCapacity();
            }

            // When implementing changes the first object will be send to writeNewFile
            // Then the old data will be replaced by new data and other objects are just added after them.

            if(x == 0) {
                if(capacity != 0) {
                    fileWriter.writeNewFile(hallID, hallName, purpose_, capacity, status);
                }
            } else {
            if(capacity != 0) {
                fileWriter.WriteToExistingXML(hallID, hallName, purpose_, capacity, status);
            }
            }
            x = x + 1;
        }
    }