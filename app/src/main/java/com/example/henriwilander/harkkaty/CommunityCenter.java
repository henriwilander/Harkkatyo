package com.example.henriwilander.harkkaty;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;

public abstract class CommunityCenter {

    private String name;
    private int id;
    private int capacity;
    private boolean multiPurpose;
    private int status;
    ArrayList<Reservation> reservationArrayList;

    WriteXMLFile fileWriter = WriteXMLFile.getInstance();

    public CommunityCenter(String name, int id, int capacity, boolean multiPurpose, int status) {
        this.name = name;
        this.id = id;
        this.capacity = capacity;
        this.multiPurpose = multiPurpose;
        this.status = status;
        reservationArrayList = new ArrayList();
    }

    public void deleteReservation(int id) {
        Reservation r = findReservationById(id);
        reservationArrayList.remove(r);
        implementChangesToXMLFile();
    }

    public Reservation findReservationById(int id) {
        for(Reservation r : reservationArrayList) {
            if(id == r.getResevationId()) {
                return r;
            }
        }
        return null;
    }

    public void editName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void changeStatus(int status) {
        this.status = status;
    }

    public void editCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getMultiOrNot() {
        return multiPurpose;
    }

    public String getPurpose(){
        return getPurpose();
    }

    public String[] getGenres() {
        return getGenres();
    }

    public ArrayList<Reservation> getReservationArrayList() {
        return reservationArrayList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getReservationIDList() {
        int rID;
        ArrayList<Integer> reservationIDList;
        reservationIDList = new ArrayList();
        for(Reservation r : reservationArrayList) {
            rID = r.getResevationId();
            reservationIDList.add(rID);
        }

        return reservationIDList;
    }

    // This method implements the changes made by user to XML.
    public void implementChangesToXMLFile() {
        int x = 0;
        int reservationID;
        String date;
        String time;
        String userSurname;
        String genre;
        int userID;
        String type;
        String organizer;
        int registrations;
        int capacity;

        // XML-file is redundant when list does not contain any objects.
        if (reservationArrayList.size() == 0) {
            String file = Environment.getExternalStorageDirectory() + "/reservationFile" + id + ".xml";
            File file_ = new File(file);
            if (file_.delete()) {
            }
        } else {

            for (Reservation reservation : reservationArrayList) {
                reservationID = reservation.getResevationId();
                date = reservation.getDate();
                time = reservation.getTime();
                userSurname = reservation.getUserSurname();
                genre = reservation.getGenre();
                userID = reservation.getUserID();
                type = reservation.getType();

                if (type.equals("Group Reservation")) {
                    organizer = reservation.getOrganizer();
                    registrations = 0;
                    capacity = reservation.getCapacity();
                } else {
                    organizer = "";
                    registrations = ((CommonReservation) reservation).getRegistrations();
                    capacity = reservation.getCapacity();
                }

                // When implementing changes the first object will be send to writeNewFile
                // Then the old data will be replaced by new data and other objects are just added after them.

                if (x == 0) {
                    fileWriter.writeNewFile(reservationID, id, date, time, userSurname, organizer, genre, type, userID, capacity, registrations);
                } else {
                    fileWriter.WriteToExistingXML(reservationID, id, date, time, userSurname, organizer, genre, type, userID, capacity, registrations);
                }
                x = x + 1;
            }
        }
    }

    public void createNewGroupReservation(String date, String time, String userSurname, String organizer, String genre, int userID, boolean readFromFile) {

        String file = Environment.getExternalStorageDirectory() + "/reservationFile"+id+".xml";
        File f = new File(file);
        int reservationID;
            reservationID = reservationArrayList.size() + id*100+1;
            if(f.exists()) {
                if(readFromFile) {
                    reservationArrayList.add(new GroupReservation(reservationID, id, date, time, userSurname, organizer, genre, userID, capacity));
                } else {
                    reservationArrayList.add(new GroupReservation(reservationID, id, date, time, userSurname, organizer, genre, userID, capacity));
                    fileWriter.WriteToExistingXML(reservationID, id, date, time, userSurname, organizer, genre, reservationArrayList.get(reservationArrayList.size()-1).getType(), userID, capacity, 0);
                }
            } else {

                if(readFromFile) {
                    reservationArrayList.add(new GroupReservation(reservationID, id, date, time, userSurname, organizer, genre, userID, capacity));
                } else {
                    reservationArrayList.add(new GroupReservation(reservationID, id, date, time, userSurname, organizer, genre, userID, capacity));
                    fileWriter.writeNewFile(reservationID, id, date, time, userSurname, organizer, genre, reservationArrayList.get(reservationArrayList.size() - 1).getType(), userID, capacity, 0);
                }
            }
        }

    public void createNewCommonReservation(String date, String time, String userSurname, String genre, int userID, int registrations, boolean readFromFile) {

        String file = Environment.getExternalStorageDirectory() + "/reservationFile"+id+".xml";
        File f = new File(file);
        int reservationID;
            reservationID = reservationArrayList.size() + id*100+1;
            if(f.exists()) {
                if(readFromFile) {
                    reservationArrayList.add(new CommonReservation(reservationID, id, date, time, userSurname, genre, userID, capacity, registrations));
                } else {
                    reservationArrayList.add(new CommonReservation(reservationID, id, date, time, userSurname, genre, userID, capacity, 1));
                    fileWriter.WriteToExistingXML(reservationID, id, date, time, userSurname, "", genre, reservationArrayList.get(reservationArrayList.size() - 1).getType(), userID, capacity, ((CommonReservation) findReservationById(reservationID)).getRegistrations());
                }
            } else {
                if(readFromFile) {
                    reservationArrayList.add(new CommonReservation(reservationID, id, date, time, userSurname, genre, userID, capacity, 1));
                } else {
                    reservationArrayList.add(new CommonReservation(reservationID, id, date, time, userSurname, genre, userID, capacity, 1));
                    fileWriter.writeNewFile(reservationID, id, date, time, userSurname, "", genre, reservationArrayList.get(reservationArrayList.size() - 1).getType(), userID, capacity, ((CommonReservation) findReservationById(reservationID)).getRegistrations());
                }

            }
        }
    }