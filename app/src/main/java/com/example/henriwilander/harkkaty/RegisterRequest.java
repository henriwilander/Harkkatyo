package com.example.henriwilander.harkkaty;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// This class makes request to PHP-file on the server and add new user to sql-database

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://henriwilander.000webhostapp.com/connect/register.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String firstName, String surname, int age, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL,listener, null);
        params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("username", username);
        params.put("age", age + "");
        params.put("surname", surname);
        params.put("password", password);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
