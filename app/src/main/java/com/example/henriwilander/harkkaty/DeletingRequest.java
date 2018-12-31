package com.example.henriwilander.harkkaty;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeletingRequest extends StringRequest {

    // This class makes request to PHP-file on the server and deletes chosen user from db's user table.

    private static final String UPDATE_REQUEST_URL = "https://henriwilander.000webhostapp.com/connect/delete.php";
    private Map<String, String> params;

    public DeletingRequest (int user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, UPDATE_REQUEST_URL,listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id+"");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

