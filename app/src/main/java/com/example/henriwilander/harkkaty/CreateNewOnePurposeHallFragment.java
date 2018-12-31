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


public class CreateNewOnePurposeHallFragment extends Fragment implements View.OnClickListener {

    EditText editTextName;
    EditText editTextPurpose;
    EditText editTextCapacity;
    private CreateNewOnePurposeHallFragmentListener listener;
    CharSequence input;
    int choice;
    Button button1;
    String name;
    String purpose;
    int capacity;

    // Interface communicates with Activity
    public interface CreateNewOnePurposeHallFragmentListener {
        void createNewOnePurposeHall(String name, String purpose, int capacity);
    }

    public CreateNewOnePurposeHallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_new_one_purpose_hall, container, false);
        editTextName = v.findViewById(R.id.editText);
        editTextPurpose = v.findViewById(R.id.editText3);;
        editTextCapacity = v.findViewById(R.id.editText18);

        button1 = (Button) v.findViewById(R.id.button17);

        button1.setOnClickListener(this);

        return v;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        // When button is pressed the information is transferred to Activity and the changes are made.
        name = editTextName.getText().toString();
        purpose = editTextPurpose.getText().toString();
        capacity = Integer.parseInt(editTextCapacity.getText().toString());
        listener.createNewOnePurposeHall(name, purpose, capacity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CreateNewOnePurposeHallFragmentListener) {
            listener = (CreateNewOnePurposeHallFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement CreateNewOnePurposeHallFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        }
    }
