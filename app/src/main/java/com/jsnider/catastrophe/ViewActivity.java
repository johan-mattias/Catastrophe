package com.jsnider.catastrophe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();

        String id = intent.getStringExtra(ListAdapter.ID);
        TextView textView = findViewById(R.id.idTextView);

        textView.setText(id);

    }
}
