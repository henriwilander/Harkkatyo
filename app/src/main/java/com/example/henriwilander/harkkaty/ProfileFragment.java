package com.example.henriwilander.harkkaty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    TextView textView;
    private String username;
    private String firstName;
    private String surname;
    private int age;

    // This very simple fragment only displays user information

    public static ProfileFragment newInstance (String firstName, String surname, int age, String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle arguments = new Bundle();
        arguments.putString("firstName",firstName);
        arguments.putString("surname",surname);
        arguments.putString("username",username);
        arguments.putInt("age",age);
        fragment.setArguments(arguments);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        if(getArguments()!= null) {
            username = getArguments().getString("username");
            firstName = getArguments().getString("firstName");
            surname = getArguments().getString("surname");
            age = getArguments().getInt("age");
        }

        textView = v.findViewById(R.id.textview);
        textView.setText("Username: "+username+"\nFirst name: "+ firstName+"\nSurname: "+surname+"\nAge: "+age);
        return v;
    }

}
