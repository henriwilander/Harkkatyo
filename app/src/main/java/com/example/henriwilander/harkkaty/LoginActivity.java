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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText password;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acticity);

        username = findViewById(R.id.editText11);
        password = findViewById(R.id.editText12);
        login = findViewById(R.id.button10);
        register = findViewById(R.id.button11);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    // Creating an intent which opens the register page when button is pressed.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button11:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                break;
            case R.id.button10:
                String userUsername = username.getText().toString();
                String userPassword = password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            //If success then user is sent to main page
                            if (success) {

                                String firstName = jsonResponse.getString("first_name");
                                String surname = jsonResponse.getString("surname");
                                String username = jsonResponse.getString("username");
                                int age = jsonResponse.getInt("age");
                                int id = jsonResponse.getInt("id");
                                String password = jsonResponse.getString("password");
                                int status = jsonResponse.getInt("status");

                                if(username.equals("master")) {

                                    Intent intent = new Intent(LoginActivity.this, MasterAccountActivity.class);

                                    intent.putExtra("first_name", firstName);
                                    intent.putExtra("surname", surname);
                                    intent.putExtra("username", username);
                                    intent.putExtra("age", age);
                                    intent.putExtra("id", id);
                                    intent.putExtra("status", status);
                                    intent.putExtra("password", password);

                                    LoginActivity.this.startActivity(intent);
                                }

                                else {

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                    intent.putExtra("first_name", firstName);
                                    intent.putExtra("surname", surname);
                                    intent.putExtra("username", username);
                                    intent.putExtra("age", age);
                                    intent.putExtra("id", id);
                                    intent.putExtra("status", status);
                                    intent.putExtra("password", password);
                                    LoginActivity.this.startActivity(intent);
                                }

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userUsername, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

        }
    }
}
