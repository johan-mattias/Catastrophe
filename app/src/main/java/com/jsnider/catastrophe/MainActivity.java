package com.jsnider.catastrophe;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText searchEditText;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        searchEditText = findViewById(R.id.searchEditText);

        queue = Volley.newRequestQueue(this);
    }

    public void search(View view) {
        String query = searchEditText.getText().toString();
        resultTextView.setText(R.string.napping);
        //new SearchAsyncTask(resultTextView).execute(query);

        String url ="https://www.flickr.com/services/rest/?" +
                "method=flickr.photos.search&" +
                "api_key=3e0b862c934cf2ae6c6e8f55268e44c5&" +
                "tags=cats&" +
                "tag_mode=all&" +
                "format=json&" +
                "nojsoncallback=1";

        //from: https://developer.android.com/training/volley/simple.html
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resultTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
