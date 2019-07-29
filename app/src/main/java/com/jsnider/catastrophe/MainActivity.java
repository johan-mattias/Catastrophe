package com.jsnider.catastrophe;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText searchEditText;
    private final LinkedList<String> mWordList = new LinkedList<>();

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

        String url ="https://www.flickr.com/services/rest/?" +
                "method=flickr.photos.search&" +
                "api_key=3e0b862c934cf2ae6c6e8f55268e44c5&" +
                "tags=cats," + query + "&" +
                "tag_mode=all&" +
                "format=json&" +
                "nojsoncallback=1";

        //from: https://developer.android.com/training/volley/simple.html
        // Request a jsonObjectRequest response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //resultTextView.setText("url" + "        Response is: "+ response.toString().substring(0,500));

                        JSONArray photoArray = null;
                        try {
                            photoArray = response.getJSONObject("photos").getJSONArray("photo");
                        } catch (JSONException e) {
                            Log.e("error", e.toString());
                        }

                        resultTextView.setText("url" + "        Response is: "+ photoArray.toString().substring(0,500));

                        for (int i = 0; i < 20; i++) {
                            String item = null;
                            try {
                                item = photoArray.getJSONObject(i+1).getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mWordList.addLast(item);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

}
