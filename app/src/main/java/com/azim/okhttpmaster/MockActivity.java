package com.azim.okhttpmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.azim.library.NetworkListener;
import com.azim.library.NetworkManager;
import com.azim.library.NetworkRequest;
import com.azim.library.RequestMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockActivity extends AppCompatActivity implements View.OnClickListener, NetworkListener {

    public static final String URL = "url";
    public static final String JSON = "json";
    public static final String MOCK = "mock";

    private String url, json, mock;

    private String callingAPI = "";

    private EditText tvURL, tvJSON, tvMock;
    private TextView tvResponse;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        if (savedInstanceState != null) {
            readData(savedInstanceState);
        } else {
            readData(getIntent().getExtras());
        }

        tvResponse = findViewById(R.id.tvResponse);
        tvURL = findViewById(R.id.tvUrl);
        tvJSON = findViewById(R.id.json);
        tvMock = findViewById(R.id.tvMockTime);

        tvURL.setText(url);
        tvJSON.setText(json);
        tvMock.setText(mock);

        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

        postJson();
    }

    private void readData(Bundle bundle) {
        url = bundle.getString(URL);
        json = bundle.getString(JSON);
        mock = bundle.getString(MOCK);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPost) {
            String url = tvURL.getText().toString().trim();
            String json = tvJSON.getText().toString().trim();
            String time = tvMock.getText().toString().trim();


            if (TextUtils.isEmpty(url)) {
                tvResponse.setText("Please enter URL to POST");
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                tvResponse.setText("Given URL is invalid");
            } else if (isValidTime(time) == -1L) {
                tvResponse.setText("Invalid time. Time should be in milliseconds.");
            } else if (!isJSONValid(json)) {
                tvResponse.setText("Invalid JSON");
            } else {
                this.url = url;
                this.json = json;
                postJson();
            }
        }
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public long isValidTime(String time) {
        try {
            return Long.parseLong(time);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(URL, url);
        outState.putString(JSON, json);
        outState.putString(MOCK, mock);
    }

    private void postJson() {
        tvResponse.setText("Loading...");
        String baseURL = url.substring(0, url.lastIndexOf('/') + 1);
        String api = url.substring(url.lastIndexOf('/') + 1);
        callingAPI = api;

        NetworkRequest.Builder getRequest = new NetworkRequest.Builder();
        getRequest.setUrl(baseURL);
        getRequest.setApi(api);
        getRequest.setMethod(RequestMethod.METHOD_POST);
        getRequest.setJsonParam(json);
        getRequest.setDummyRequest(true);
        getRequest.setDummyRequestTimeoutMilliseconds(isValidTime(tvMock.getText().toString().trim()));
        getRequest.setMockJsonData(json);
        NetworkManager.getInstance().addToRequestQueue(getRequest.build(), this);
    }

    @Override
    public void onSuccess(String API, String response) {
        if (API.equals(callingAPI)) {
            if (response.startsWith("SyntaxError:"))
                onError(API, "Invalid JSON");
            else
                tvResponse.setText(response);
        }
    }

    @Override
    public void onError(String API, String errorMsg) {
        if (API.equals(callingAPI)) {
            tvResponse.setText(errorMsg);
        }
    }
}
