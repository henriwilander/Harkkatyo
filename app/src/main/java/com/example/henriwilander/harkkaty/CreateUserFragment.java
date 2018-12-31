package com.example.henriwilander.harkkaty;

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


public class CreateUserFragment extends Fragment implements View.OnClickListener {

    EditText username;
    EditText first_name;
    EditText surname;
    EditText age;
    EditText password;
    Button button;
    private CreateUserFragmentListener listener;
    String username_;
    String firstName;
    String surname_;
    int age_;
    String password_;

    public interface CreateUserFragmentListener {
        void createUser (String username, String firsName, String surname, int age,String password);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_user, container, false);

        username = v.findViewById(R.id.editText15);
        first_name = v.findViewById(R.id.editText16);
        surname= v.findViewById(R.id.editText17);
        age = v.findViewById(R.id.editText19);
        password = v.findViewById(R.id.editText21);
        button = v.findViewById(R.id.button16);

        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        username_ = username.getText().toString();
        firstName = first_name.getText().toString();
        surname_ = surname.getText().toString();
        age_ = Integer.parseInt(age.getText().toString());
        password_ = password.getText().toString();

        // When button is pressed, information is transferred to activity and there they are sent to RegisterRequest which communicates with the server
        listener.createUser(username_,firstName,surname_,age_,password_);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CreateUserFragment.CreateUserFragmentListener) {
            listener = (CreateUserFragment.CreateUserFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Implement CreateUserFragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

