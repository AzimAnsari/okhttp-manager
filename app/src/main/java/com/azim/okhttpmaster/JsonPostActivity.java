package com.azim.okhttpmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class JsonPostActivity extends AppCompatActivity implements NetworkListener, View.OnClickListener {

    public static final String URL = "url";
    public static final String JSON = "json";

    private String url;
    private String json;

    private String callingAPI = "";

    private EditText tvURL, tvJSON;
    private TextView tvResponse;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_post);

        if (savedInstanceState != null) {
            readData(savedInstanceState);
        } else {
            readData(getIntent().getExtras());
        }

        tvResponse = findViewById(R.id.tvResponse);
        tvURL = findViewById(R.id.tvUrl);
        tvJSON = findViewById(R.id.tvMockTime);

        tvURL.setText(url);
        tvJSON.setText(json);

        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

        postJson();
    }

    private void readData(Bundle bundle) {
        url = bundle.getString(URL);
        json = bundle.getString(JSON);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(URL, url);
        outState.putString(JSON, json);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPost) {
            String url = tvURL.getText().toString().trim();
            String json = tvJSON.getText().toString().trim();

            if (TextUtils.isEmpty(url)) {
                tvResponse.setText("Please enter URL to POST");
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                tvResponse.setText("Given URL is invalid");
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
}
