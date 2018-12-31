package com.example.henriwilander.harkkaty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DefaultPageFragment extends Fragment {

    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        textView = v.findViewById(R.id.textview);
        textView.setText("Welcome to the admin's user interface!\nYou are able to manage all users and halls.");
        return v;
    }

}
