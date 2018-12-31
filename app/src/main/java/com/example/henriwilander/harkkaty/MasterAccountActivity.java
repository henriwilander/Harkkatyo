package com.example.henriwilander.harkkaty;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MasterAccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EditProfileFragment.EditProfileFragmentListener, MakeReservationsFragment.MakeReservationsFragmentListener, EditReservationsFragment.EditReservationsFragmentListener, SingUpFragment.SingUpFragmentListener, ManageUsersFragment.ManageUsersFragmentListener,CreateUserFragment.CreateUserFragmentListener, CreateNewOnePurposeHallFragment.CreateNewOnePurposeHallFragmentListener,CreateNewMultiPurposeHallFragment.CreateNewMultiPurposeHallFragmentListener,ManageHallsFragment.ManageHallsFragmentListener, ManageReservationsFragment.ManageReservationsFragmentListener {
    private DrawerLayout drawer;
    private MakeReservationsFragment makeReservationsFragment;
    private ManageUsersFragment manageUsersFragment;
    private ManageHallsFragment manageHallsFragment;
    private ManageReservationsFragment manageReservationsFragment;
    String str;
    int size;
    ArrayList<String> reservationList;
    ArrayList<String> userReservationList;
    ArrayList<String> hallNames;
    ArrayList<String> genreList;
    ArrayList<String> reservations;
    ArrayList<String> listOFDates;
    ArrayList<String> userReservationInformationList;
    ArrayList<String> userInformationArray;
    ArrayList<Integer> userIdList;
    ArrayList<Integer> statusList;
    ArrayList<Integer> ageList;
    String[] userIDs;
    String[] usernames;
    String[] firstNames;
    String[] surnames;
    String[] ages;
    String[] passwords;
    String[] statuses;
    String userInformation;
    boolean status;


    BookingSystem bookingSystem = BookingSystem.getInstance();
    ReadXMLFile readXMLFile = ReadXMLFile.getInstance();
    ReadHallXMLFile readHallXMLFile = ReadHallXMLFile.getInstance();

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservationList = new ArrayList();
        userReservationList = new ArrayList();
        hallNames = new ArrayList();
        genreList = new ArrayList();
        reservations = new ArrayList();
        listOFDates = new ArrayList();
        userReservationInformationList = new ArrayList();
        userInformationArray = new ArrayList();
        userIdList = new ArrayList();
        statusList = new ArrayList();
        ageList = new ArrayList();

        // DateList is initialized.

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = 0; i < 3; i++) {
            Calendar c = Calendar.getInstance();
            c.clear(Calendar.HOUR_OF_DAY);
            c.clear(Calendar.AM_PM);
            c.clear(Calendar.MINUTE);
            c.clear(Calendar.SECOND);
            c.clear(Calendar.MILLISECOND);
            Date dt = new Date();
            c.setTime(dt);
            c.add(Calendar.DATE, i);
            dt = c.getTime();
            String dt1 = format.format(dt);
            listOFDates.add(dt1);
        }

        Intent intent = getIntent();

        String u_firstName = intent.getStringExtra("first_name");
        String u_surname = intent.getStringExtra("surname");
        String u_username = intent.getStringExtra("username");
        int u_age = intent.getIntExtra("age", 0);
        String u_password = intent.getStringExtra("password");
        int u_id = intent.getIntExtra("id", 0);
        int u_status = intent.getIntExtra("status", 1);

        if(u_status == 1) {
            status = true;
        } else {
            status = false;
        }

        user = new User(u_firstName, u_surname, u_username, u_id, status, u_password, u_age);

        setContentView(R.layout.activity_master_account);

        // Hall information is read from XML-file
        readHallXMLFile.readFile();


        // Reservation information is read from XML-file

        for (int ID : bookingSystem.getHallIdList()) {
            readXMLFile.readFile(ID);
        }

        // Setting toolbar as actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Creating the drawer and setting item selected listener for navigation view menu
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // // Userinfo is transferred to the fragment and default view is initialized
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DefaultPageFragment()).commit();
            navigationView.setCheckedItem(R.id.profile);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
        }

        // It takes some time to read the data from the database. So it is smart to do this when the application starts.
        setManageUsersFragment();
    }

    @Override
    // Handling click events on our menu items

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DefaultPageFragment()).commit();
                break;
            case R.id.editProfile:
                manageUsersFragment = ManageUsersFragment.newInstance(userInformationArray, userIdList, statusList, usernames, firstNames, surnames, ageList, passwords);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, manageUsersFragment).commit();
                break;
            case R.id.createUser:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateUserFragment()).commit();
                break;
            case R.id.createOnePurposeHall:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateNewOnePurposeHallFragment()).commit();
                break;
            case R.id.createMultiPurposeHall:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateNewMultiPurposeHallFragment()).commit();
                break;
            case R.id.manageHalls:
                ArrayList<Integer> idList;
                String[] hallInfo;
                hallInfo = bookingSystem.getHallInformation().split("\n");
                idList = bookingSystem.getHallIdList();
                manageHallsFragment = ManageHallsFragment.newInstance(hallInfo, idList);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, manageHallsFragment).commit();
                break;
            case R.id.makeCommonReservation:
                genreList.clear();
                hallNames.clear();
                reservations.clear();

                for (int ID : bookingSystem.getHallIdList()) {
                    String[] checkList = bookingSystem.getData(ID, 1, 1).split("\n");
                    for (String date : checkList) {
                        if (date.contains(listOFDates.get(0)) || date.contains(listOFDates.get(1)) || date.contains(listOFDates.get(2))) {
                            reservations.add(date);
                            // If the reservation occurs in next three days it is added to list.
                        }
                    }
                }
                for (int ID : bookingSystem.getHallIdList()) {
                    // When hall is not in use it is not added to list and user is not able to make reservations these halls.
                    if(bookingSystem.findHallById(ID).getStatus() == 1) {
                        hallNames.add(bookingSystem.findHallById(ID).getName());
                    }

                    // In case of multiReservation the Array must be converted to String before adding to list

                    if (!bookingSystem.findHallById(ID).getMultiOrNot()) {
                        genreList.add(bookingSystem.findHallById(ID).getPurpose());
                    } else {
                        str = Arrays.toString(bookingSystem.findHallById(ID).getGenres());
                        size = str.length();
                        str = str.substring(1, size - 1);
                        genreList.add(str);
                    }

                }

                // UserChoice == 1 means that user has selected Common Reservation
                makeReservationsFragment = MakeReservationsFragment.newInstance(genreList, hallNames, reservations, 1);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, makeReservationsFragment).commit();
                break;

            case R.id.makeGroupReservation:
                genreList.clear();
                hallNames.clear();
                reservations.clear();
                // When hall is not in use it is not added to list and user is not able to make reservations to these halls.
                for (int ID : bookingSystem.getHallIdList()) {
                    String[] checkList = bookingSystem.getData(ID, 1, 1).split("\n");
                    for (String date : checkList) {
                        if (date.contains(listOFDates.get(0)) || date.contains(listOFDates.get(1)) || date.contains(listOFDates.get(2))) {
                            reservations.add(date);
                        }
                    }
                }
                for (int ID : bookingSystem.getHallIdList()) {

                    // When hall is not in use it is not added to list and user is not able to make reservations these halls.

                    if(bookingSystem.findHallById(ID).getStatus() == 1) {
                        hallNames.add(bookingSystem.findHallById(ID).getName());
                    }
                    if (!bookingSystem.findHallById(ID).getMultiOrNot()) {
                        genreList.add(bookingSystem.findHallById(ID).getPurpose());
                    } else {
                        str = Arrays.toString(bookingSystem.findHallById(ID).getGenres());
                        size = str.length();
                        str = str.substring(1, size - 1);
                        genreList.add(str);
                    }
                }
                // UserChoice == 2 means that user has selected Group Reservation
                makeReservationsFragment = MakeReservationsFragment.newInstance(genreList, hallNames, reservations, 2);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, makeReservationsFragment).commit();
                break;

            case R.id.allReservations:
                hallNames.clear();
                reservationList.clear();
                userReservationList.clear();
                ArrayList<Integer> userIDList;
                userIDList = bookingSystem.getAllReservationIDs();
                for (int ID : bookingSystem.getHallIdList()) {
                    hallNames.add(bookingSystem.findHallById(ID).getName());
                    // When userID == 0 and x = != 0, all information is read from reservation list and added to the arrayList
                    reservationList.add(bookingSystem.getData(ID, 0, 1));
                }
                manageReservationsFragment = ManageReservationsFragment.newInstance(reservationList, hallNames, userIDList);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, manageReservationsFragment).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // This method transfers data from database to the readingRequest to MasterUserActivity.
    @Override
    public void onInputSent(CharSequence input, int choice) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //If success then user is gonna see TOAST_MESSAGE
                    if (success) {
                        Toast.makeText(MasterAccountActivity.this,
                                "Profile updated successfully!", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MasterAccountActivity.this,
                                "Updating failed!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (choice == 1) {
            user.setFirstName(input.toString());
        } else if (choice == 2) {
            user.setSurname(input.toString());
        } else if (choice == 3) {
            int number = Integer.parseInt(input.toString());
            user.setAge(number);
        }

        UpdatingRequest updatingRequest = new UpdatingRequest(user.getUsername(), user.getFirstName(), user.getSurname(), user.getPassword(), user.getAge(), user.getuserID(), -("false".indexOf("" + user.getStatus())), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MasterAccountActivity.this);
        queue.add(updatingRequest);
    }

    // These methods transfers data from fragments to the Activity.
    // Im telling more about these inside of the fragments.
    @Override
    public void sendDataToActivity(String date, String time, String genre, String special, int choice, String hall) {
        int hallID = bookingSystem.findHallByName(hall).getId();
        bookingSystem.implementReservations(hallID, date, time, user.getSurname(), genre, special, choice, user.getuserID());
    }

    @Override
    public void sendChangesToActivity(String date, String time, String genre, String special, String hall, int id) {
        int choice = 0;
        if (date.equals("") && time.equals("") && genre.equals("") && special.equals("") && hall.equals("")) {
            bookingSystem.executeDeletingMethod(id);
        } else {
            if (bookingSystem.findReservationHall(hall, id)) {
                bookingSystem.implementChanges(date, time, genre, special, hall, id);
            } else {
                if (special.equals("")) {
                    choice = 1;
                } else {
                    choice = 2;
                }
                bookingSystem.executeDeletingMethod(id);
                bookingSystem.implementReservations(bookingSystem.findHallByName(hall).getId(), date, time, user.getSurname(), genre, special, choice, user.getuserID());
            }
        }
    }

    @Override
    public void sendReservationIDToActivity(int id, String hallName) {
        Reservation reservation = bookingSystem.findHallByName(hallName).findReservationById(id);
        if (reservation.getCapacity() == ((CommonReservation) reservation).getRegistrations()) {
            Toast.makeText(MasterAccountActivity.this,
                    "The registration is not made because the event is full!", Toast.LENGTH_LONG).show();
        } else {
            ((CommonReservation) reservation).addRegistrations();
            reservation.createNewRegistration();
            bookingSystem.findHallByName(hallName).implementChangesToXMLFile();
        }
    }

    @Override
    public void editUser(String username, String firstName, String surname, String password, int age, int id, int status, int delete) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //If success then user is gonna see TOAST_MESSAGE and changes made are read from the database again.
                    if (success) {
                        Toast.makeText(MasterAccountActivity.this,
                                "Profile updated successfully!", Toast.LENGTH_LONG).show();
                        userInformationArray.clear();
                        userIdList.clear();
                        statusList.clear();
                        usernames = null;
                        firstNames = null;
                        surnames = null;
                        ageList.clear();
                        passwords = null;
                        setManageUsersFragment();
                    } else {
                        Toast.makeText(MasterAccountActivity.this,
                                "Updating failed!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (delete == 1) {
            DeletingRequest deletingRequest = new DeletingRequest(id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MasterAccountActivity.this);
            queue.add(deletingRequest);
        } else {
            UpdatingRequest updatingRequest = new UpdatingRequest(username, firstName, surname, password, age, id, status, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MasterAccountActivity.this);
            queue.add(updatingRequest);
        }

    }

    public void setManageUsersFragment() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarray = new JSONArray(response);

                    String replaceString = response.replace('"', ' ');
                    String replaceString2 = replaceString.replace(" ", "");
                    String replaceString3 = replaceString2.replace(" ", "");
                    String replaceString4 = replaceString3.replace("[", "");
                    String[] userList = replaceString4.split("]");

                    userIDs = userList[0].split(",");
                    usernames = userList[1].split(",");
                    firstNames = userList[2].split(",");
                    surnames = userList[3].split(",");
                    ages = userList[4].split(",");
                    passwords = userList[5].split(",");
                    statuses = userList[6].split(",");

                    for (String i : userIDs) {
                        userIdList.add(Integer.valueOf(i));
                    }

                    for (String i : statuses) {
                        statusList.add(Integer.valueOf(i));
                    }

                    for (String i : ages) {
                        ageList.add(Integer.valueOf(i));
                    }

                    for (int i = 0; userIDs.length > i; i++) {
                        String status = "";
                        if (statuses[i].equals("1")) {
                            status = "Active";
                        } else {
                            status = "Suspended";
                        }
                        userInformation = "UserID: " + userIDs[i] + " Username: " + usernames[i] + " Name: " + firstNames[i] + " " + surnames[i] + " Age: " + ages[i] + " Password: " + passwords[i] + " Status: " + status;
                        userInformationArray.add(userInformation);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ReadingRequest readingRequest = new ReadingRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MasterAccountActivity.this);
        queue.add(readingRequest);
    }

    @Override
    public void createUser(String username, String firsName, String surname, int age, String password) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    // IF SUCCESS THEN USER IS INFORMED BY TOAST MESSAGE AND INFORMATION WHICH IS USED BY APP ARE ALSO UPDATED

                    if(success) {
                        Toast.makeText(MasterAccountActivity.this,
                                "User created!", Toast.LENGTH_LONG).show();
                        userInformationArray.clear();
                        userIdList.clear();
                        statusList.clear();
                        usernames = null;
                        firstNames = null;
                        surnames = null;
                        ageList.clear();
                        passwords = null;
                        setManageUsersFragment();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MasterAccountActivity.this);
                        builder.setMessage("User creation failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(username, firsName, surname, age, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MasterAccountActivity.this);
        queue.add(registerRequest);
    }

    @Override
    public void createNewOnePurposeHall(String name, String purpose, int capacity) {
        bookingSystem.createNewOnePurposeHall(name, purpose, capacity,false, 1);
    }

    @Override
    public void createNewMultiPurposeHall(String name, String[] purposes, int capacity) {
        bookingSystem.createMultiPurposeHall(name,capacity,purposes,false, 1);
    }

    @Override
    public void implementChanges(int id, int capacity, String purpose, String name, int delete, int change_status, int choice) {
        if(choice == 1) {
            bookingSystem.findHallById(id).editName(name);
        } else if (choice == 2) {
            if(purpose.contains(",")) {
                String[] purposes = purpose.split(",");
                ((MultiPurposeHall) bookingSystem.findHallById(id)).setNewGenres(purposes);
            } else {
                ((OnePurposeHall) bookingSystem.findHallById(id)).setNewPurpose(purpose);
            }
        } else if (choice == 3) {
            bookingSystem.findHallById(id).editCapacity(capacity);
        } else if (choice == 4) {
            bookingSystem.deleteHall(id);
        } else if (choice == 5) {
            if(bookingSystem.findHallById(id).getStatus() == 1) {
                bookingSystem.findHallById(id).changeStatus(0);
            } else {
                bookingSystem.findHallById(id).changeStatus(1);
            }
        }
    }

    @Override
    public void manageReservations(int id) {
        bookingSystem.executeDeletingMethod(id);
        Toast.makeText(MasterAccountActivity.this,
                "Chosen reservation has been deleted!", Toast.LENGTH_LONG).show();
    }
}