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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    EditText editTextFirstName;
    EditText editTextSurname;
    EditText editTextAge;
    TextView textView;
    private EditProfileFragmentListener listener;
    CharSequence input;
    int choice;
    Button button1;
    Button button2;
    Button button3;


    public interface EditProfileFragmentListener {
        void onInputSent(CharSequence input, int choice);
    }

    public EditProfileFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        editTextFirstName = v.findViewById(R.id.editText);
        editTextSurname = v.findViewById(R.id.editText3);;
        editTextAge = v.findViewById(R.id.editText4);
        textView = v.findViewById(R.id.textView1);

        button1 = (Button) v.findViewById(R.id.button);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        return v;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                input = editTextFirstName.getText();
                choice = 1;
                listener.onInputSent(input,choice);
                break;
            case R.id.button2:
                choice = 2;
                input = editTextSurname.getText();
                listener.onInputSent(input,choice);
                break;
            case R.id.button3:
                choice = 3;
                input = editTextAge.getText();
                listener.onInputSent(input,choice);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditProfileFragmentListener) {
            listener = (EditProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement EditProfileFragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        }
    }
