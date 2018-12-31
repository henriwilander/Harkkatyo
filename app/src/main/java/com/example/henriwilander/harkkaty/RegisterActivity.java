package com.example.henriwilander.harkkaty;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText firstName;
    EditText surname;
    EditText password;
    EditText age;
    Button register;
    String userFirstName;
    String userSurname;
    String userUsername;
    String userPassword;
    int userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editText6);
        firstName = findViewById(R.id.editText7);
        surname = findViewById(R.id.editText8);
        password = findViewById(R.id.editText9);
        age = findViewById(R.id.editText10);
        register = findViewById(R.id.button6);

        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        userFirstName = firstName.getText().toString();
        userSurname = surname.getText().toString();
        userUsername = username.getText().toString();
        userPassword = password.getText().toString();
        userAge = Integer.parseInt(age.getText().toString());

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //If success then user is sent back to login page.
                    if(success) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        RegisterRequest registerRequest = new RegisterRequest(userUsername, userFirstName, userSurname, userAge, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }
}
