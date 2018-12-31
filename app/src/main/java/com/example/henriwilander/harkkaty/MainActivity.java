package com.example.henriwilander.harkkaty;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EditProfileFragment.EditProfileFragmentListener, MakeReservationsFragment.MakeReservationsFragmentListener, EditReservationsFragment.EditReservationsFragmentListener, SingUpFragment.SingUpFragmentListener {
    private DrawerLayout drawer;
    private ProfileFragment profileFragment;
    private SingUpFragment singUpFragment;
    private ViewReservationsFragment viewReservationsFragment;
    private MakeReservationsFragment makeReservationsFragment;
    private EditReservationsFragment editReservationsFragment;
    String str;
    int size;
    ArrayList<String> reservationList;
    ArrayList<String> userReservationList;
    ArrayList<String> hallNames;
    ArrayList<String> genreList;
    ArrayList<String> reservations;
    ArrayList<String> listOFDates;
    ArrayList<String> userReservationInformationList;
    BookingSystem bookingSystem = BookingSystem.getInstance();
    ReadXMLFile readXMLFile = ReadXMLFile.getInstance();
    ReadHallXMLFile readHallXMLFile = ReadHallXMLFile.getInstance();
    boolean status;

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


        // DateList is initialized.

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for(int i=0; i<3; i++) {
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
        setContentView(R.layout.activity_main);

        // Hall information is read from XML-file
        readHallXMLFile.readFile();

        // Reservation information is read from XML-file
        for(int ID : bookingSystem.getHallIdList()) {
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

        // // User info is transferred to the fragment and default view is initialized
        if (savedInstanceState == null) {
            profileFragment = ProfileFragment.newInstance(user.getFirstName(), user.getSurname(), user.getAge(), user.getUsername());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            navigationView.setCheckedItem(R.id.profile);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
        }

    }
    @Override
    // Handling click events on our menu items

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
                profileFragment = ProfileFragment.newInstance(user.getFirstName(), user.getSurname(), user.getAge(), user.getUsername());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                break;

            case R.id.editProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditProfileFragment()).commit();
                break;

            case R.id.makeCommonReservation:
                if(user.getStatus()) {

                    genreList.clear();
                    hallNames.clear();
                    reservations.clear();

                    for (int ID : bookingSystem.getHallIdList()) {
                        String[] checkList = bookingSystem.getData(ID, 1, 1).split("\n");
                        for (String date : checkList) {
                            if (date.contains(listOFDates.get(0)) || date.contains(listOFDates.get(1)) || date.contains(listOFDates.get(2))) {
                                reservations.add(date);
                                // If the reservation occurs in next three days it is added to list
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
                    makeReservationsFragment = MakeReservationsFragment.newInstance(genreList, hallNames, reservations, 1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, makeReservationsFragment).commit();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Your profile has been suspended. You can not make reservations until your profile has opened again.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.makeGroupReservation:
                if(user.getStatus()) {
                    genreList.clear();
                    hallNames.clear();
                    reservations.clear();

                    for (int ID : bookingSystem.getHallIdList()) {
                        String[] checkList = bookingSystem.getData(ID, 1, 1).split("\n");
                        for (String date : checkList) {
                            if (date.contains(listOFDates.get(0)) || date.contains(listOFDates.get(1)) || date.contains(listOFDates.get(2))) {
                                reservations.add(date);
                            }
                        }
                    }
                    // When hall is not in use it is not added to list and user is not able to make reservations to these halls.
                    for (int ID : bookingSystem.getHallIdList()) {
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
                } else {
                Toast.makeText(MainActivity.this,
                        "Your profile has been suspended. You can not make reservations until your profile has opened again.", Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.allReservations:
                hallNames.clear();
                reservationList.clear();
                userReservationList.clear();
                hallNames.add("Own Reservations:");
                for (int ID : bookingSystem.getHallIdList()) {
                    hallNames.add(bookingSystem.findHallById(ID).getName());
                    reservationList.add(bookingSystem.getData(ID, 0, 1));
                    userReservationList.add(bookingSystem.getData(ID, user.getuserID(), 1));
                }
                viewReservationsFragment = ViewReservationsFragment.newInstance(reservationList, userReservationList, hallNames);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewReservationsFragment).commit();
                break;

            case R.id.singUp:
                if(user.getStatus()) {
                    hallNames.clear();
                    reservationList.clear();
                    ArrayList<Integer> idList = null;
                    for (int ID : bookingSystem.getHallIdList()) {
                        hallNames.add(bookingSystem.findHallById(ID).getName());
                        // x = 1 only relevant information is added to the list.
                        reservationList.add(bookingSystem.getData(ID, 0, 1));
                    }
                    idList = bookingSystem.getAllReservationIDs();
                    singUpFragment = SingUpFragment.newInstance(reservationList, hallNames, idList);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, singUpFragment).commit();
                } else {
                Toast.makeText(MainActivity.this,
                        "Your profile has been suspended. You can not sing up events until your profile has opened again.", Toast.LENGTH_LONG).show();

            }
                break;

            case R.id.editReservations:
                hallNames.clear();
                genreList.clear();
                reservations.clear();
                userReservationList.clear();
                ArrayList<Integer> IDs = null;
                hallNames.add("Choose New Hall:");
                // x = 1 and userID = 1 only relevant information (name, date and time) is added to the list.
                for (int ID : bookingSystem.getHallIdList()) {
                    String[] checkList = bookingSystem.getData(ID, 1, 1).split("\n");
                    for (String date : checkList) {
                        if (date.contains(listOFDates.get(0)) || date.contains(listOFDates.get(1)) || date.contains(listOFDates.get(2))) {
                            reservations.add(date);
                        }
                    }
                }
                    for (int ID : bookingSystem.getHallIdList()) {
                        hallNames.add(bookingSystem.findHallById(ID).getName());
                        userReservationList.add(bookingSystem.getData(ID, user.getuserID(), 1));
                        userReservationInformationList.add(bookingSystem.getData(ID, user.getuserID(), 0));
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
                    IDs = bookingSystem.getAllReservationIDs();
                editReservationsFragment = EditReservationsFragment.newInstance(userReservationList, hallNames, IDs, userReservationInformationList, genreList, reservations);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReservationsFragment).commit();
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
    // These methods transfers data from fragments to the Activity.
    // Im telling more about these inside of the fragments.
    @Override
    public void onInputSent(CharSequence input, int choice) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //If success then user is gonna see TOAST_MESSAGE
                    if(success) {
                        Toast.makeText(MainActivity.this,
                                "Profile updated successfully!", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this,
                                "Updating failed!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (choice == 1) {
            user.setFirstName(input.toString());
        } else if(choice == 2) {
            user.setSurname(input.toString());
        } else if(choice==3) {
            int number = Integer.parseInt(input.toString());
            user.setAge(number);
        }

        UpdatingRequest updatingRequest = new UpdatingRequest(user.getUsername(), user.getFirstName(), user.getSurname(), user.getPassword(), user.getAge(), user.getuserID(), -("false".indexOf("" + user.getStatus())), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(updatingRequest);
    }

    @Override
    public void sendDataToActivity(String date, String time, String genre, String special, int choice, String hall) {
        int hallID = bookingSystem.findHallByName(hall).getId();
        bookingSystem.implementReservations(hallID, date, time, user.getSurname(), genre, special,choice,user.getuserID());
    }

    @Override
    public void sendChangesToActivity(String date, String time, String genre, String special, String hall, int id) {
        int choice = 0;
        if(date.equals("") && time.equals("") && genre.equals("") && special.equals("") && hall.equals("")) {
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
        if (reservation.getCapacity() ==  ((CommonReservation) reservation).getRegistrations()) {
            Toast.makeText(MainActivity.this,
                    "The registration is not made because the event is full!", Toast.LENGTH_LONG).show();
        } else {
            ((CommonReservation) reservation).addRegistrations();
            reservation.createNewRegistration();
            bookingSystem.findHallByName(hallName).implementChangesToXMLFile();
        }
    }
}