package com.example.henriwilander.harkkaty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditReservationsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<String> list;
    ArrayList<String> nameList;
    String[] listViewArray;
    ListView listView;
    String s;
    ArrayList<String> listViewItems;
    ArrayList<Integer> IDList;
    ArrayList <String> genreList;
    ArrayList <String> reservationList;
    ArrayList <String> reservationList2;
    String listItems;
    String[] newGenreList;
    ArrayList<String> newGenreList2;
    ArrayList<String> dateList;
    private EditReservationsFragment.EditReservationsFragmentListener listener;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    String genre;
    String date;
    String special;
    String time1;
    String time2;
    String time3;
    String hall;
    String type;
    int progress;
    int beginningProgress;
    int progress2;
    int max;
    int beginningProgress2;
    TextView textView1;
    TextView textView2;
    EditText editText1;
    SeekBar seekBar1;
    SeekBar seekBar2;
    Button button;
    String numberAsString;
    String time4;
    ArrayList<String> timeArray;
    String hallName;
    Button button1;
    ArrayList<String> userReservationInformationList;
    String[] oldValues;
    int id;

    public static EditReservationsFragment newInstance(ArrayList<String> list, ArrayList<String> nameList, ArrayList<Integer> IDs, ArrayList<String> userReservationInformationList, ArrayList<String> genreList, ArrayList<String> reservationList) {
        EditReservationsFragment fragment = new EditReservationsFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("List", list);
        arguments.putStringArrayList("NameList", nameList);
        arguments.putIntegerArrayList("IDList", IDs);
        arguments.putStringArrayList("UserReservationInformationList", userReservationInformationList);
        arguments.putStringArrayList("GenreList", genreList);
        arguments.putStringArrayList("ReservationList", reservationList);
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface EditReservationsFragmentListener {
        void sendChangesToActivity (String date, String time, String genre, String special, String hall, int id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_reservations_fragment, container, false);
        listView = v.findViewById(R.id.listview10);
        listViewItems = new ArrayList();
        genreList = new ArrayList();
        newGenreList2 = new ArrayList();
        dateList = new ArrayList();
        reservationList = new ArrayList();
        timeArray = new ArrayList();
        reservationList2 = new ArrayList();

        dateList.add("Choose New Date:");
        textView1 = v.findViewById(R.id.textView7);
        textView2 = v.findViewById(R.id.textView8);
        seekBar1 = v.findViewById(R.id.seekBar2);
        seekBar2 = v.findViewById(R.id.seekBar4);
        editText1 = v.findViewById(R.id.editText5);
        button = v.findViewById(R.id.button8);
        button.setOnClickListener(this);
        button1 = v.findViewById(R.id.button9);
        button1.setOnClickListener(this);

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
            dateList.add(format.format(dt));
        }

        initializeTimeArray();

        seekBarFunction();

        // User's reservations, hall's names, reservationIds, information from user's reservations, genres and all reservations are transferred from Activity
        if (getArguments() != null) {
            list = getArguments().getStringArrayList("List");
            nameList = getArguments().getStringArrayList("NameList");
            IDList = getArguments().getIntegerArrayList("IDList");
            userReservationInformationList = getArguments().getStringArrayList("UserReservationInformationList");
            genreList = getArguments().getStringArrayList("GenreList");
            reservationList = getArguments().getStringArrayList("ReservationList");
        }
        initializeListView();

        spinner = (Spinner) v.findViewById(R.id.spinner5);
        spinner3 = (Spinner) v.findViewById(R.id.spinner8);
        spinner3.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, dateList));
        spinner.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, nameList));
        spinner.setOnItemSelectedListener(this);
        spinner2 = (Spinner) v.findViewById(R.id.spinner7);

        // All information about user's previous reservations are displayed in listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s = listView.getItemAtPosition(i).toString();
                id = findSelectedReservation(s);
                String[] userInfo;
                for(String string : userReservationInformationList) {
                    userInfo = string.split("\n");
                    for(String string1 : userInfo) {
                        if(string1.contains(Integer.toString(id))) {
                            oldValues = string1.split(" ");
                            date = oldValues[0];
                            time3 = oldValues[1];
                            type = oldValues[2] + " "+oldValues[3];
                            genre = oldValues[4];
                            if (type.equals("Group Reservation")) {
                                special = oldValues[5];
                                hall = oldValues[6];
                            } else if (type.equals("Common Reservation")) {
                                special = "";
                                hall = oldValues[5];
                            }
                            break;
                        }

                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String hall1 = parent.getItemAtPosition(position).toString();
        if(!hall1.equals("Choose New Hall:")) {
            hall = parent.getItemAtPosition(position).toString();
            listItems = genreList.get(nameList.indexOf(hall)-1);
            // In case of multiPurposeHall all different genres are in string and they are spited to array
            if (listItems.contains(",")) {
                newGenreList = listItems.split(",");
                newGenreList2.add("Choose New Genre:");
                for (String item : newGenreList) {
                    if (item.contains(" ")) {
                        item = item.substring(1, item.length());
                    }
                    newGenreList2.add(item);
                }
                // Genres are displayed in spinners
                spinner2.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, newGenreList2));
            } else {
                newGenreList = new String[]{"Choose New Genre:", listItems};
                spinner2.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, newGenreList));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void initializeListView() {
        for (String lister1 : list) {
            listViewArray = lister1.split("\n");
            for (String lister3 : listViewArray) {
                listViewItems.add(lister3);
            }
        }
        listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, listViewItems));
    }

    // Finds the id of selected reservation
    public int findSelectedReservation(String s) {
        String textLabel = "ReservationID: ";
        int ID = 0;
        for(int id : IDList) {
            if(s.contains(textLabel+Integer.toString(id))) {
                ID = id;
            }
        }
        return ID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button8:
                // In case of user is trying to make overlapping shift the stop will be 1 and execution of method is stopped
                int stop = 0;
                createTimeArray();
                time1 = textView1.getText().toString();
                time2 = textView2.getText().toString();
                    // klo:8:00 is default choice. if this is the case changes are not.
                    if(time1.equals("8:00") && time2.equals("8:00")) {
                        String[] times1 = time3.split("-");
                        time1 = times1[0];
                        time2 = times1[1];
                    }
                    createTimeArray();
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    try {

                        for (String time_ : timeArray) {
                            if (time_.contains("Reserved")) {
                                String[] times = time_.split(" ");
                                Date time4 = parser.parse(times[0]);
                                Date time5 = parser.parse(time1);
                                Date time6 = parser.parse(time2);
                                if (time4.after(time5) && time4.before(time6) || times[0].equals(time1)) {

                                    Toast.makeText(getActivity(), "You can not make an overlapping shift.",
                                            Toast.LENGTH_LONG).show();
                                    stop = 1;
                                }
                            }

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(stop == 0) {
                        time3 = time1 + "-" + time2;
                        if(!spinner3.getSelectedItem().toString().equals("Choose New Date:")) {
                            date = spinner3.getSelectedItem().toString();
                        } if( spinner2.getSelectedItem() != null) {
                            if (!spinner2.getSelectedItem().toString().equals("Choose New Genre:")) {
                                genre = spinner2.getSelectedItem().toString();
                            }
                        }
                        if (type.equals("Common Reservation")) {
                            special = "";
                            listener.sendChangesToActivity(date, time3, genre, special, hall,id);
                        } else if (type.equals("Group Reservation")) {
                            if(editText1.getText() != null) {
                                special = editText1.getText().toString();
                            }
                            listener.sendChangesToActivity(date, time3, genre, special, hall,id);
                        }
                    }
                break;
            case R.id.button9:
                createTimeArray();
                listener.sendChangesToActivity("", "", "", "", "", id);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MakeReservationsFragment.MakeReservationsFragmentListener) {
            listener = (EditReservationsFragment.EditReservationsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement EditReservationsFragmentListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void seekBarFunction() {

        beginningProgress = 8;
        if(seekBar1.getProgress() == 0) {
            textView1.setText(beginningProgress + ":00");
            textView2.setText(beginningProgress + ":00");
        } else {
            textView1.setText(progress + ":00");
            textView2.setText(progress + ":00");
        }
        seekBar1.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress = seekBar1.getProgress() + beginningProgress;
                        textView1.setText(progress + ":00");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        beginningProgress2 = seekBar1.getProgress() + beginningProgress;
                        textView2.setText(beginningProgress2+ ":00");
                        seekBar2.setProgress(0);
                        if(beginningProgress2 == 23) {
                            max = 0;
                        } else if(beginningProgress2 == 22) {
                            max = 1;
                        } else if(beginningProgress2 == 21) {
                            max = 2;
                        } else {
                            max = 3;
                        }
                        seekBar2.setMax(max);
                    }
                }
        );

        textView2.setText(beginningProgress + ":00");
        seekBar2.setMax(beginningProgress2 + 3-8);
        seekBar2.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress2 = seekBar2.getProgress() + beginningProgress2;
                        textView2.setText(progress2 + ":00");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }
    public void createTimeArray() {
        hallName = hall;
        timeArray.clear();
        reservationList2.clear();
        initializeTimeArray();
        String dateUsed = spinner3.getSelectedItem().toString();
        if(dateUsed.equals("Choose New Date:")) {
            dateUsed = date;
        }
        for(String reservation : reservationList) {
            if(reservation.contains(hallName) && reservation.contains(dateUsed)) {
                reservationList2.add(reservation);
            }
        }
        if(!reservationList2.isEmpty()) {
            for (String reservation : reservationList2) {
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                String[] splittedString = reservation.split(" ");
                String[] times = splittedString[2].split("-");
                Date time5 = null;
                Date time6 = null;
                Date time7 = null;
                int index;
                String old;
                try {
                    time5 = parser.parse(times[0]);
                    time6 = parser.parse(times[1]);

                    for (String n : timeArray) {
                        time7 = parser.parse(n);
                        // If time is reserved then the old value is replaced with value which contains "Reserved"
                        if (time7.after(time5) && time7.before(time6) || n.equals(times[0])) {
                            index = timeArray.indexOf(n);
                            old = timeArray.get(index);
                            timeArray.set(index, old + " Reserved");
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // adds all times from 8:00 to 23:00 to the list
    public void initializeTimeArray() {
        for(int i = 8; i<24; i++) {
            numberAsString = Integer.toString(i);
            time4 = numberAsString + ":00";
            timeArray.add(time4);
            time4 = "";
        }
    }

}
