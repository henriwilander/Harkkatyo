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

public class MakeReservationsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    public static MakeReservationsFragment newInstance (ArrayList <String> genreList, ArrayList <String> nameList, ArrayList <String> reservationList, int userChoice) {
        MakeReservationsFragment fragment = new MakeReservationsFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("List",genreList);
        arguments.putStringArrayList("NameList", nameList);
        arguments.putStringArrayList("ReservationList", reservationList);
        arguments.putInt("UserChoice", userChoice);
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface MakeReservationsFragmentListener {
        void sendDataToActivity(String date, String time, String genre, String special, int choice, String hall);
    }

    ArrayList<String> spinnerList;
    ArrayList <String> genreList;
    ArrayList <String> nameList;
    ArrayList <String> reservationList;
    ArrayList <String> reservationList2;
    String listItems;
    String[] newGenreList;
    ArrayList<String> newGenreList2;
    ArrayList<String> dateList;
    private MakeReservationsFragmentListener listener;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    int userChoice;
    String genre;
    String date;
    String special;
    String time1;
    String time2;
    String time3;
    String hall;
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
    Button button1;
    String hallName;
    ListView listView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make_reservations, container, false);
        spinnerList = new ArrayList();
        genreList = new ArrayList();
        nameList = new ArrayList();
        newGenreList2 = new ArrayList();
        dateList = new ArrayList();
        reservationList = new ArrayList();
        timeArray = new ArrayList();
        reservationList2 = new ArrayList();
        dateList.add("Choose date:");
        textView1 = v.findViewById(R.id.textView4);
        textView2 = v.findViewById(R.id.textView6);
        seekBar1 = v.findViewById(R.id.seekBar);
        seekBar2 = v.findViewById(R.id.seekBar3);
        editText1 = v.findViewById(R.id.editText2);
        button = v.findViewById(R.id.button4);
        button.setOnClickListener(this);
        button1 = v.findViewById(R.id.button5);
        button1.setOnClickListener(this);
        listView = v.findViewById(R.id.listView3);
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

        if(getArguments()!= null) {
            genreList = getArguments().getStringArrayList("List");
            nameList = getArguments().getStringArrayList("NameList");
            reservationList = getArguments().getStringArrayList("ReservationList");
            userChoice = getArguments().getInt("UserChoice");
        }

        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner3 = (Spinner) v.findViewById(R.id.spinner4);
        spinner3.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, dateList));
        spinner.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, nameList));
        spinner.setOnItemSelectedListener(this);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        return v;

    }

        // All Possible times are added to list
    public void initializeTimeArray() {
        for(int i = 8; i<24; i++) {
            numberAsString = Integer.toString(i);
            time4 = numberAsString + ":00";
            timeArray.add(time4);
            time4 = "";
        }
    }

    public void createTimeArray() {
        hallName = hall;
        timeArray.clear();
        reservationList2.clear();
        initializeTimeArray();
        String dateUsed = spinner3.getSelectedItem().toString();
        if(dateUsed.equals("Choose date:")) {
            dateUsed = dateList.get(1);
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
                        if (time7.after(time5) && time7.before(time6) || n.equals(times[0])) {
                            index = timeArray.indexOf(n);
                            old = timeArray.get(index);
                            if(!old.contains("Reserved")) {
                                timeArray.set(index, old + " Reserved");
                            }
                        }
                    }
                    listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, timeArray));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, timeArray));
        }
    }

    public void seekBarFunction() {

        beginningProgress = 8;
        textView1.setText(progress + ":00");
        textView2.setText(progress + ":00");
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

                textView2.setText(beginningProgress2 + ":00");
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MakeReservationsFragment.MakeReservationsFragmentListener) {
            listener = (MakeReservationsFragment.MakeReservationsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement EditProfileFragmentListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                int stop = 0;
                genre = spinner2.getSelectedItem().toString();
                if(genre != "Choose genre:") {
                    createTimeArray();
                    time1 = textView1.getText().toString();
                    time2 = textView2.getText().toString();
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");

                    try {
                        for (String setti : timeArray) {
                            if (setti.contains("Reserved")) {
                                String[] times = setti.split(" ");
                                Date time4 = parser.parse(times[0]);
                                Date time5 = parser.parse(time1);
                                Date time6 = parser.parse(time2);
                                if (time4.after(time5) && time4.before(time6) || times[0].equals(time1)) {

                                    Toast.makeText(getActivity(), "You are can not make an overlapping shift.",
                                            Toast.LENGTH_LONG).show();
                                    stop = 1;
                                    break;
                                }
                            }

                        }
                } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(stop == 0) {
                        time3 = time1 + "-" + time2;
                        date = spinner3.getSelectedItem().toString();
                        if (userChoice == 1) {
                            special = "";
                            listener.sendDataToActivity(date, time3, genre, special, userChoice, hall);
                        } else if (userChoice == 2) {
                            special = editText1.getText().toString();
                            listener.sendDataToActivity(date, time3, genre, special, userChoice, hall);
                        }
                    }
                    }
                break;
            case R.id.button5:
                createTimeArray();
                listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, timeArray));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hall = parent.getItemAtPosition(position).toString();
        listItems= genreList.get(nameList.indexOf(hall));
        if (listItems.contains(",")) {
            newGenreList = listItems.split(",");
            newGenreList2.add("Choose genre:");
            for (String item : newGenreList) {
                if(item.contains(" ")) {
                    item = item.substring(1, item.length());
                }
                newGenreList2.add(item);
            }
            spinner2.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, newGenreList2));
        } else {
            newGenreList = new String[]{"Choose genre:", listItems};
            spinner2.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, newGenreList));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}