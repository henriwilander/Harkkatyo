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

public class ManageHallsFragment extends Fragment implements View.OnClickListener {

    String[] list;
    ListView listView;
    String s;
    ArrayList<Integer> IDList;
    private ManageHallsFragment.ManageHallsFragmentListener listener;
    String name = "";
    String purpose = "";
    int capacity = 0;
    Button button;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    EditText newName;
    EditText newPurpose;
    EditText newCapacity;
    int delete = 0;
    int change_status = 0;
    int id;
    int choice;


    public static ManageHallsFragment newInstance(String[] list, ArrayList<Integer> idList) {
        ManageHallsFragment fragment = new ManageHallsFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArray("List", list);
        arguments.putIntegerArrayList("IDList", idList);
        fragment.setArguments(arguments);
        return fragment;
    }

        // Interface communicates between activity and fragment
    public interface ManageHallsFragmentListener {
        void implementChanges (int id, int capacity, String purpose, String name, int delete, int change_status,int choice);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_halls, container, false);
        listView = v.findViewById(R.id.listview10);

        newName = v.findViewById(R.id.editText20);
        newPurpose = v.findViewById(R.id.editText22);
        newCapacity = v.findViewById(R.id.editText23);

        button = v.findViewById(R.id.button18);
        button.setOnClickListener(this);
        button1 = v.findViewById(R.id.button19);
        button1.setOnClickListener(this);
        button2 = v.findViewById(R.id.button20);
        button2.setOnClickListener(this);
        button3 = v.findViewById(R.id.button9);
        button3.setOnClickListener(this);
        button4 = v.findViewById(R.id.button8);
        button4.setOnClickListener(this);


        if (getArguments() != null) {
            list = getArguments().getStringArray("List");
            IDList = getArguments().getIntegerArrayList("IDList");
        }

        listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s = listView.getItemAtPosition(i).toString();
                id = findSelectedHall(s);

                            }
                        });
        return v;
    }


    public int findSelectedHall(String s) {
        String textLabel = "ID: ";
        int ID = 0;
        for(int id : IDList) {
            if(s.contains(textLabel+Integer.toString(id))) {
                ID = id;
            }
        }
        return ID;
    }

    // Depending on users choice the different features of halls are changed
    // Halls could also be deleted or freezed.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button18:
            name = newName.getText().toString();
            choice = 1;
            listener.implementChanges(id,capacity,purpose,name,delete,change_status,choice);
                break;
            case R.id.button19:
                purpose = newPurpose.getText().toString();
                choice = 2;
                listener.implementChanges(id,capacity,purpose,name,delete,change_status,choice);
                break;
            case R.id.button20:
                capacity = Integer.parseInt(newCapacity.getText().toString());
                choice = 3;
                listener.implementChanges(id,capacity,purpose,name,delete,change_status,choice);
                break;
            case R.id.button9:
                delete = 1;
                choice = 4;
                listener.implementChanges(id,capacity,purpose,name,delete,change_status,choice);
                break;
            case R.id.button8:
                change_status = 1;
                choice = 5;
                listener.implementChanges(id,capacity,purpose,name,delete,change_status,choice);
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MakeReservationsFragment.MakeReservationsFragmentListener) {
            listener = (ManageHallsFragment.ManageHallsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement ManageHallsFragmentListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    }

