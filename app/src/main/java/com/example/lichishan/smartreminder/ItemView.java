package com.example.karzzi.smartreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by KarZzI on 11/14/15.
 */
public class ItemView extends FrameLayout {

    public ItemView(Context context) {
        super(context);

        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.event_title, this);
    }


    public synchronized void setTitle(String title) {
        ((TextView) (findViewById(R.id.title))).setText(title);
    }

}
