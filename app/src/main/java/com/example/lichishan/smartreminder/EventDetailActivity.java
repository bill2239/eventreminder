package com.example.karzzi.smartreminder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intent = getIntent();
        Events eventobj = (Events)intent.getSerializableExtra("Event_Obj");
        //Toast.makeText(getApplicationContext(), eventobj.location, Toast.LENGTH_LONG).show();
        TextView textview = (TextView) (findViewById(R.id.eventText));
        textview.setText(eventobj.title);
        textview = (TextView) (findViewById(R.id.locationText));
        textview.setText(eventobj.location);
        textview = (TextView) (findViewById(R.id.dateText));
        textview.setText(eventobj.date);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.arb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //startActivity(new Intent(MainActivity.this, MapsActivity.class));
               Snackbar.make(view, "Event reminder added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }


}
