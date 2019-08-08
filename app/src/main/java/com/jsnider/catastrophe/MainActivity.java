package com.jsnider.catastrophe;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ArrayList<String> mWordList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEditText = findViewById(R.id.searchEditText);
        mRecyclerView = findViewById(R.id.recyclerview);
        queue = Volley.newRequestQueue(this);

        // Restore the state.
        if (savedInstanceState != null) {
            mWordList = savedInstanceState.getStringArrayList("mWordList");
            urlList = savedInstanceState.getStringArrayList("urlList");

            if(mWordList != null && mWordList != null) {
                populateRecyclerView();
                hideKeyboard(mRecyclerView);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mWordList != null && mWordList != null) {
            outState.putStringArrayList("mWordList", mWordList);
            outState.putStringArrayList("urlList", urlList);
        }

    }

    public void hideKeyboard(View view) {
        //from: https://stackoverflow.com/questions/13593069/androidhide-keyboard-after-button-click/13593232
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void populateRecyclerView() {
        int spanCount;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 6;
        } else {
            spanCount = 3;
        }

        mAdapter = new ListAdapter(this, mWordList, urlList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
    }

    public void search(final View view) {

        hideKeyboard(view);

        mWordList.clear();
        urlList.clear();

        final String query = searchEditText.getText().toString();

        String url ="https://www.flickr.com/services/rest/?" +
                "method=flickr.photos.search&" +
                "api_key=3e0b862c934cf2ae6c6e8f55268e44c5&" +
                "tags=goat,goats," + query + "&" +
                "tag_mode=all&" +
                "format=json&" +
                "nojsoncallback=1";

        //from: https://developer.android.com/training/volley/simple.html
        // Request a jsonObjectRequest response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray photoArray = null;
                        int total = 0;
                        int max = 21;
                        try {
                            total = response.getJSONObject("photos").getInt("total");
                            if(total > 0) {
                                photoArray = response.getJSONObject("photos").getJSONArray("photo");
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = "Sad: there are no: " +
                                                    query +
                                                    " goats";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.e("error", e.toString());
                        }

                        String item = null;
                        String url = null;

                        if(total < max) {
                            max = total - (total % 3);
                        }

                        for (int i = 0; i < max; i++) {
                            try {
                                item = photoArray.getJSONObject(i+1).getString("id");
                                url = "https://farm" +
                                        photoArray.getJSONObject(i+1).getString("farm") +
                                        ".staticflickr.com/" +
                                        photoArray.getJSONObject(i+1).getString("server") +
                                        "/"+
                                        photoArray.getJSONObject(i+1).getString("id") +
                                        "_"+
                                        photoArray.getJSONObject(i+1).getString("secret") +
                                        "_b.jpg";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(item!=null && url!=null) {
                                mWordList.add(item);
                                urlList.add(url);
                            }
                        }

                        populateRecyclerView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorListener", "Error on responce listener");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

}
