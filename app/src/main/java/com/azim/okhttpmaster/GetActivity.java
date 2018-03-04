package com.azim.okhttpmaster;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.azim.library.NetworkListener;
import com.azim.library.NetworkManager;
import com.azim.library.NetworkRequest;
import com.azim.library.RequestMethod;

public class GetActivity extends AppCompatActivity implements NetworkListener, View.OnClickListener {

    private String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private TextView responseText;
    private EditText getURL;

    private String callingAPI = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        responseText = findViewById(R.id.responseText);
        responseText.setMovementMethod(new ScrollingMovementMethod());

        getURL = findViewById(R.id.getURL);
        if (savedInstanceState == null)
            getURL.setText(BASE_URL + APIs.GET_POSTS);

        Button btnGET = findViewById(R.id.btnGET);
        btnGET.setOnClickListener(this);

        if (savedInstanceState == null)
            callSimpleGET();
    }

    @SuppressLint("SetTextI18n")
    private void callSimpleGET() {
        responseText.setText("Loading...");
        NetworkRequest.Builder getRequest = new NetworkRequest.Builder();
        getRequest.setUrl(BASE_URL);
        getRequest.setApi(APIs.GET_POSTS);
        getRequest.setMethod(RequestMethod.METHOD_GET);
        NetworkManager.getInstance().addToRequestQueue(getRequest.build(), this);
    }

    @SuppressLint("SetTextI18n")
    private void getFromURL(String url) {
        String baseURL = url.substring(0, url.lastIndexOf('/') + 1);
        String api = url.substring(url.lastIndexOf('/') + 1);
        callingAPI = api;

        responseText.setText("Loading...");
        NetworkRequest.Builder getRequest = new NetworkRequest.Builder();
        getRequest.setUrl(baseURL);
        getRequest.setApi(api);
        getRequest.setMethod(RequestMethod.METHOD_GET);
        NetworkManager.getInstance().addToRequestQueue(getRequest.build(), this);
    }

    @Override
    public void onSuccess(String API, String response) {
        if (API.equals(APIs.GET_POSTS)) {
            responseText.setText(response);
        } else if (API.equals(callingAPI)) {
            responseText.setText(response);
        }
    }

    @Override
    public void onError(String API, String errorMsg) {
        if (API.equals(APIs.GET_POSTS)) {
            responseText.setText(errorMsg);
        } else if (API.equals(callingAPI)) {
            responseText.setText(errorMsg);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGET) {
            String url = getURL.getText().toString().trim();

            if (TextUtils.isEmpty(url)) {
                responseText.setText("Please enter URL to GET");
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                responseText.setText("Given URL is invalid");
            } else {
                getFromURL(url);
            }
        }
    }
}
