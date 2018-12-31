package com.example.henriwilander.harkkaty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ViewReservationsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // This fragment displays user's own reservations and also all reservations

    ArrayList <String> list;
    ArrayList <String> list2;
    ArrayList <String> nameList;
    String[] listViewArray1;
    String[] listViewArray2;
    ListView listView;
    String userChoice;
    ArrayList <String> listViewItems;
    public static ViewReservationsFragment newInstance (ArrayList <String> list, ArrayList <String> list2, ArrayList <String> nameList) {
        ViewReservationsFragment fragment = new ViewReservationsFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("List",list);
        arguments.putStringArrayList("List2",list2);
        arguments.putStringArrayList("NameList", nameList);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_reservations, container, false);
        listView = v.findViewById(R.id.listView);
        listViewItems = new ArrayList();

        if(getArguments()!= null) {
            list = getArguments().getStringArrayList("List");
            list2 = getArguments().getStringArrayList("List2");
            nameList = getArguments().getStringArrayList("NameList");
        }

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner3);
        spinner.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, nameList));
        spinner.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userChoice = parent.getItemAtPosition(position).toString();
        if(userChoice.equals("Own Reservations:")) {
            if(listViewItems.size() == 0) {
                for (String lister1 : list2) {
                    listViewArray2 = lister1.split("\n");
                    for (String lister3 : listViewArray2) {
                        listViewItems.add(lister3);
                    }
                }
            }
            listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, listViewItems));
        } else {
        for(String lister2 : list) {
            if (lister2.contains(userChoice)) {
                listViewArray1 = lister2.split("\n");
                break;
            }
        }
        try {
            listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, listViewArray1));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
