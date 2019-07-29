package com.jsnider.catastrophe;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        searchEditText = findViewById(R.id.searchEditText);
    }

    public void search(View view) {
        String query = searchEditText.getText().toString();
        resultTextView.setText(R.string.napping);
        new SearchAsyncTask(resultTextView).execute(query);
    }
}

class SearchAsyncTask
        extends AsyncTask<String, Integer, String> {

    private WeakReference<TextView> resultTextView;

    SearchAsyncTask(TextView tv) {
        resultTextView = new WeakReference<>(tv);
    }

    @Override
    protected String doInBackground(String... strings) {

        String s = strings[0];

        Random r = new Random();
        int n = r.nextInt(11);

        int sleep = n * 200;

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "We're searching for a " + s + " cat!";
    }

    protected void onPostExecute(String result) {
        resultTextView.get().setText(result);
    }
}
