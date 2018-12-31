package com.example.henriwilander.harkkaty;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class ManageUsersFragment extends Fragment implements View.OnClickListener {

    EditText editTextPassword;
    EditText editTextUsername;
    TextView textView;
    private ManageUsersFragmentListener listener;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    ListView listView;
    String s;
    ArrayList<String> userInfoList;
    ArrayList<Integer> userIDList;
    ArrayList<Integer> userStatusList;
    String[] usernameList;
    String[] firstNameList;
    String[] surnameList;
    ArrayList<Integer> ageList;
    String[] passwordList;
    int id;
    int status;
    int age;
    int index;
    String username;
    String surname;
    String firstName;
    String password;

    // In this fragment adminuser can make changes to user's details

    public static ManageUsersFragment newInstance(ArrayList<String> userInfoList, ArrayList<Integer> userIDList, ArrayList<Integer> userStatusList, String[] usernameList, String[] firstNameList, String[] surnameList, ArrayList<Integer> ageList, String[] passwordList) {
        ManageUsersFragment fragment = new ManageUsersFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("UserInfoList", userInfoList);
        arguments.putIntegerArrayList("IDList", userIDList);
        arguments.putIntegerArrayList("Status", userStatusList);
        arguments.putStringArray("Username", usernameList);
        arguments.putStringArray("FirstName", firstNameList);
        arguments.putStringArray("Surname", surnameList);
        arguments.putIntegerArrayList("Age", ageList);
        arguments.putStringArray("Password", passwordList);
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface ManageUsersFragmentListener {
        void editUser(String username, String firstName, String surname, String password, int age, int id, int status,int delete);
    }

    public ManageUsersFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_manage_users, container, false);
        editTextPassword = v.findViewById(R.id.editText13);
        editTextUsername = v.findViewById(R.id.editText14);

        button1 = (Button) v.findViewById(R.id.button13);
        button2 = (Button) v.findViewById(R.id.button14);
        button3 = (Button) v.findViewById(R.id.button12);
        button4 = (Button) v.findViewById(R.id.button15);
        listView = v.findViewById(R.id.userInfoListview);

        ageList = new ArrayList();


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        if (getArguments() != null) {
            userInfoList = getArguments().getStringArrayList("UserInfoList");
            userIDList = getArguments().getIntegerArrayList("IDList");
            userStatusList = getArguments().getIntegerArrayList("Status");
            usernameList = getArguments().getStringArray("Username");
            firstNameList = getArguments().getStringArray("FirstName");
            surnameList = getArguments().getStringArray("Surname");
            ageList = getArguments().getIntegerArrayList("Age");
            passwordList = getArguments().getStringArray("Password");
        }

        for(String info : userInfoList) {
        }

        listView.setAdapter(new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, userInfoList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s = listView.getItemAtPosition(i).toString();
                id = findSelectedUser(s);
                index = userIDList.indexOf(id);
                age = ageList.get(index);
                status = userStatusList.get(index);
                username = usernameList[index];
                firstName = firstNameList[index];
                surname = surnameList[index];
                password = passwordList[index];
            }
        });
        return v;
    }

    public int findSelectedUser(String s) {
        String textLabel = "UserID: ";
        int ID = 0;
        for (int id : userIDList) {
            if (s.contains(textLabel + Integer.toString(id))) {
                ID = id;
            }
        }
        return ID;
    }
    // Changed values are transferred to the activity and from there to editRequest which communicates with server
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button13:
                password = editTextPassword.getText().toString();
                listener.editUser(username, firstName, surname, password, age, id, status,0);
                break;
            case R.id.button14:
                username = editTextUsername.getText().toString();
                listener.editUser(username, firstName, surname, password, age, id, status,0);
                break;
            case R.id.button12:
                if(status == 1) {
                    status = 0;
                } else if (status == 0) {
                    status = 1;
                }
                listener.editUser(username, firstName, surname, password, age, id, status,0);
                break;
            case R.id.button15:
                listener.editUser(username, firstName, surname, password, age, id, status,1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ManageUsersFragmentListener) {
            listener = (ManageUsersFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement ManageUsersFragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        }
    }
