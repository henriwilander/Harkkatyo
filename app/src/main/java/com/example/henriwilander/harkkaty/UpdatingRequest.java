package com.example.henriwilander.harkkaty;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdatingRequest extends StringRequest {

// This class makes request to PHP-file on the server and make updates to database

        private static final String UPDATE_REQUEST_URL = "https://henriwilander.000webhostapp.com/connect/update.php";
        private Map<String, String> params;

        public UpdatingRequest (String username, String firstName, String surname, String password, int age, int user_id, int status, Response.Listener<String> listener) {
            super(Request.Method.POST, UPDATE_REQUEST_URL,listener, null);
            params = new HashMap<>();
            params.put("first_name", firstName);
            params.put("surname", surname);
            params.put("age", age + "");
            params.put("username", username);
            params.put("password", password);
            params.put("user_id", user_id+"");
            params.put("status", status + "");
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
}
