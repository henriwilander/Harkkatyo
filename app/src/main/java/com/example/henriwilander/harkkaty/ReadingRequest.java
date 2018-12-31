package com.example.henriwilander.harkkaty;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.Map;

public class ReadingRequest extends StringRequest {

    // Communicates between sever and reads data from SQL-database

    private static final String READING_REQUEST_URL = "https://henriwilander.000webhostapp.com/connect/userInfo.php";
    private Map<String, String> params;

    public ReadingRequest (Response.Listener<String> listener) {
        super(Request.Method.POST, READING_REQUEST_URL,listener, null);
    }
}


