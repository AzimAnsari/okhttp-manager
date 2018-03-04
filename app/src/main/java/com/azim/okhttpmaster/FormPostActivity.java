package com.azim.okhttpmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.azim.library.NetworkListener;
import com.azim.library.NetworkManager;
import com.azim.library.NetworkRequest;
import com.azim.library.RequestMethod;

import java.util.ArrayList;
import java.util.Map;

public class FormPostActivity extends AppCompatActivity implements NetworkListener {


    public static final String URL = "url";
    public static final String PARAMETERS = "params";
    private String url;
    private Map<String, String> params;
    private ListView list;
    private TextView tvUrl, response;
    private ListAdapter mAdapter;
    private String callingAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_post);

        readData(getIntent().getExtras());

        tvUrl = findViewById(R.id.etUrl);
        tvUrl.setText(url);

        response = findViewById(R.id.response);
        response.setMovementMethod(new ScrollingMovementMethod());
        list = findViewById(R.id.list);
        mAdapter = new ListAdapter(params);
        list.setAdapter(mAdapter);

        postJson();
    }

    private void readData(Bundle bundle) {
        url = bundle.getString(URL);
        //noinspection unchecked  Safe to read this as we are sure of sending from other activities
        params = (Map<String, String>) bundle.getSerializable(PARAMETERS);
    }

    private void postJson() {
        response.setText("Loading...");
        String baseURL = url.substring(0, url.lastIndexOf('/') + 1);
        String api = url.substring(url.lastIndexOf('/') + 1);
        callingAPI = api;

        NetworkRequest.Builder postRequest = new NetworkRequest.Builder();
        postRequest.setUrl(baseURL);
        postRequest.setApi(api);
        postRequest.setMethod(RequestMethod.METHOD_POST);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            postRequest.addParam(entry.getKey(), entry.getValue());
        }
        NetworkManager.getInstance().addToRequestQueue(postRequest.build(), this);
    }

    @Override
    public void onSuccess(String API, String response) {
        if (API.equals(callingAPI)) {
            this.response.setText(response);
        }
    }

    @Override
    public void onError(String API, String errorMsg) {
        if (API.equals(callingAPI)) {
            this.response.setText(errorMsg);
        }
    }

    private class ListAdapter extends BaseAdapter {

        private Map<String, String> params;
        private ArrayList<Map.Entry<String, String>> entries = new ArrayList<>();

        public ListAdapter(Map<String, String> params) {
            this.params = params;
            updateList();
        }

        private void updateList() {
            entries.clear();
            entries.addAll(this.params.entrySet());
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public Map.Entry<String, String> getItem(int position) {
            return entries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return entries.get(position).getKey().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder viewHolder;
            Map.Entry<String, String> item = getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                viewHolder = new Holder();
                viewHolder.key = convertView.findViewById(R.id.etKey);
                viewHolder.value = convertView.findViewById(R.id.etValue);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Holder) convertView.getTag();
            }
            viewHolder.key.setText(item.getKey());
            viewHolder.value.setText(item.getValue());

            return convertView;
        }

        private class Holder {
            TextView key, value;
        }

        public void addItem() {
            if (this.params.containsKey(""))
                return;
            this.params.put("", "");
            updateList();
            notifyDataSetChanged();
        }
    }
}
