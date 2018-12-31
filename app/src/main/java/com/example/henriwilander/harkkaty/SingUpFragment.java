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

public class SingUpFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList <String> list;
    ArrayList <String> nameList;
    String[] listViewArray1;
    ListView listView;
    String userChoice;
    ArrayList <String> listViewArray2;
    ArrayList <String> listViewItems;
    ArrayList <Integer> idList;
    String s;
    String hallName;
    int reservationID;
    Button button1;
    private SingUpFragment.SingUpFragmentListener listener;

    public static SingUpFragment newInstance (ArrayList <String> list, ArrayList <String> nameList, ArrayList<Integer> idList) {
        SingUpFragment fragment = new SingUpFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("List",list);
        arguments.putStringArrayList("NameList", nameList);
        arguments.putIntegerArrayList("IDList", idList);
        fragment.setArguments(arguments);
        return fragment;
    }


    public interface SingUpFragmentListener {
        void sendReservationIDToActivity (int id, String hallName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sing_up_for_events, container, false);
        listView = v.findViewById(R.id.listview10);
        listViewItems = new ArrayList();
        listViewArray2 = new ArrayList();
        button1 = v.findViewById(R.id.button7);

        button1.setOnClickListener(this);

        if(getArguments()!= null) {
            list = getArguments().getStringArrayList("List");
            nameList = getArguments().getStringArrayList("NameList");
            idList = getArguments().getIntegerArrayList("IDList");
        }
        // When user Presses the listView object data is read and thus reservation id and hall name is captured
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s = listView.getItemAtPosition(i).toString();
                for(int id : idList) {
                    if(s.contains("reservationID: "+Integer.toString(id))) {
                        reservationID = id;
                        hallName = userChoice;
                    }
                }
            }
        });

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner6);
        spinner.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, nameList));
        spinner.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userChoice = parent.getItemAtPosition(position).toString();
        listViewArray2.clear();
        for(String lister : list) {
            if (lister.contains(userChoice)) {
                listViewArray1 = lister.split("\n");
                for(String listViewItem : listViewArray1) {
                    // User can sing up only common events
                    if(listViewItem.contains("Common Reservation")) {
                        listViewArray2.add(listViewItem);
                    }
                }
            }
        }
        listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, listViewArray2));
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SingUpFragment.SingUpFragmentListener) {
            listener = (SingUpFragment.SingUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement SingUpFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        System.out.println(reservationID + "      " + hallName);
        listener.sendReservationIDToActivity(reservationID, hallName);

    }
}
