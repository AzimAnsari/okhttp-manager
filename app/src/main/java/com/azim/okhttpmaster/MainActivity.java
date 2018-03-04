package com.azim.okhttpmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button get1 = findViewById(R.id.get1);
        get1.setOnClickListener(this);
        Button mock = findViewById(R.id.mock);
        mock.setOnClickListener(this);
        Button post1 = findViewById(R.id.post1);
        post1.setOnClickListener(this);
        Button post2 = findViewById(R.id.post2);
        post2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get1:
                startActivity(new Intent(this, GetActivity.class));
                break;
            case R.id.mock: {
                Intent intent = new Intent(this, MockActivity.class);
                intent.putExtra(MockActivity.URL, "https://jsonplaceholder.typicode.com/posts");
                intent.putExtra(MockActivity.JSON, JsonBuilder.getSimpleJSON());
                intent.putExtra(MockActivity.MOCK, String.valueOf(2000L));
                startActivity(intent);
            }
            break;
            case R.id.post1: {
                Intent intent = new Intent(this, JsonPostActivity.class);
                intent.putExtra(JsonPostActivity.URL, "https://jsonplaceholder.typicode.com/posts");
                intent.putExtra(JsonPostActivity.JSON, JsonBuilder.getSimpleJSON());
                startActivity(intent);
            }
            break;
            case R.id.post2: {
                HashMap<String, String> params = new HashMap<>();
                params.put("custname", "I am a customer");
                params.put("custemail", "thisismy@email.com");
                params.put("comments", "I want your pizza with extra toppings");
                Intent intent = new Intent(this, FormPostActivity.class);
                intent.putExtra(FormPostActivity.URL, "https://httpbin.org/post");
                intent.putExtra(FormPostActivity.PARAMETERS, params);
                startActivity(intent);
            }
            break;
        }
    }
}
