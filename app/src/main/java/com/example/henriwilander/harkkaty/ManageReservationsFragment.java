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
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Objects;

public class ManageReservationsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList <String> list;
    ArrayList <String> nameList;
    String[] listViewArray1;
    ListView listView;
    String userChoice;
    ArrayList <Integer> idList;
    String s;
    int id;
    Button button;
    ArrayList <String> listViewItems;
    ManageReservationsFragmentListener listener;

    // This fragment display all reservations and give change to delete them also


        // Interface communicates between fragment and activity
    public interface ManageReservationsFragmentListener {
        void manageReservations(int id);
    }

    public static ManageReservationsFragment newInstance (ArrayList <String> list, ArrayList <String> nameList, ArrayList <Integer> idList) {
        ManageReservationsFragment fragment = new ManageReservationsFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("List",list);
        arguments.putStringArrayList("NameList", nameList);
        arguments.putIntegerArrayList("IDlist", idList);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_reservations, container, false);
        listView = v.findViewById(R.id.listView);
        listViewItems = new ArrayList();

        if(getArguments()!= null) {
            list = getArguments().getStringArrayList("List");
            nameList = getArguments().getStringArrayList("NameList");
            idList = getArguments().getIntegerArrayList("IDlist");

        }


        Spinner spinner = (Spinner) v.findViewById(R.id.spinner3);
        spinner.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, nameList));
        spinner.setOnItemSelectedListener(this);

        button = v.findViewById(R.id.button21);

        button.setOnClickListener(this);

        // When user presses the listView objects the reservation is found by finding method using id and user's choice
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s = listView.getItemAtPosition(i).toString();
                id = findSelectedReservation(s);
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // When item is selected the listView Array is formed by using user's choice in spinner
        userChoice = parent.getItemAtPosition(position).toString();
        for(String lister : list) {
            if (lister.contains(userChoice)) {
                listViewArray1 = lister.split("\n");
                break;
            }
        }
        try {
            listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, listViewArray1));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        }

    public int findSelectedReservation(String s) {
        String textLabel = "reservationID: ";
        int ID = 0;
        for (int id : idList) {
            if (s.contains(textLabel + Integer.toString(id))) {
                ID = id;
            }
        }
        return ID;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ManageReservationsFragment.ManageReservationsFragmentListener) {
            listener = (ManageReservationsFragment.ManageReservationsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement ManageReservationsFragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        listener.manageReservations(id);
    }
}

