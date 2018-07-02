package com.example.karzzi.smartreminder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    public static ArrayList<Events> events = new ArrayList<Events>();
    public static ArrayList<Events> saves = new ArrayList<Events>();
    public static ArrayList<Events> reminds = new ArrayList<Events>();
    public static int pagenumber;
    String url= "http://pastebin.com/raw.php?i=dR6TziGu";
    private static final String DATE = "date";
    private static final String EVENT = "event";
    private static final String LOCATION = "location";
    private static final String TAG = "events";
    JSONArray eventeles=null;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Events eventobj = events.get(position);
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("Event_Obj", eventobj);
        startActivity(intent);
    }

    private static class CustomArrayAdapter extends ArrayAdapter<Events> {

        /**
         * @param demos An array containing the details of the demos to be displayed.
         */
        public CustomArrayAdapter(Context context, ArrayList<Events> demos) {
            super(context, R.layout.event_title, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView itemView;
            if (convertView instanceof ItemView) {
                itemView = (ItemView) convertView;
            } else {
                itemView = new ItemView(getContext());
            }

            Events events = getItem(position);

            itemView.setTitle(events.title);

            return itemView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reminds.add();
                Snackbar.make(view, "Event reminder added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*events.add(new Events("EGSS: Math Boot Camp for the GRE and GMAT", "Language and Literature Building", "Sat Nov 14", 33.4209122, -111.9345518));
        events.add(new Events("German Unity Event: Film Screenings", "Katzin Concert Hall", "Sat Nov 14", 33.4209122, -111.9345518));
        events.add(new Events("Doctoral Student Recital: Beth Youngblood, Violin", "Recital Hall", "Sat Nov 14", 33.4209122, -111.9345518));
        events.add(new Events("Shared Undergraduate Student Recital: Kelly Davis and Erin Delaney, Flute","Recital Hall","Sat Nov 14", 33.4209122, -111.9345518));
        events.add(new Events("Studio E411 Viola Recital","See Description","Sat Nov 14", 33.4209122, -111.9345518));
        */
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(url);
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        ListView list = (ListView) findViewById(R.id.eventlist);
        ListAdapter adapter = new CustomArrayAdapter(this, events);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        registerForContextMenu(list);
        pagenumber = 0;
    }


    //@Override
    public void switchToView(ArrayList<Events> viewlist, int number){
        View v = findViewById(R.id.eventlist);
        ViewGroup parent = (ViewGroup) v.getParent();
        int index = parent.indexOfChild(v);
        parent.removeView(v);
        parent.addView(v, index);

        ListView list = (ListView) findViewById(R.id.eventlist);
        //Events[] saveList = new Events[30];
        ListAdapter adapter = new CustomArrayAdapter(this, viewlist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        pagenumber = number;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.about_info:
                Toast toast = Toast.makeText(getApplicationContext(),"Developed by Lichi and Shan!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.list_events) {
            switchToView(events,0);
        } else if (id == R.id.list_saved) {
            switchToView(saves,1);
        } else if (id == R.id.list_reminder) {
            switchToView(reminds,2);
        } else if (id == R.id.nav_share) {
            Toast toast = Toast.makeText(getApplicationContext(),"Shared", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_send) {
            Toast toast = Toast.makeText(getApplicationContext(),"Send", Toast.LENGTH_SHORT);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.long_press_option, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.save:
                saves.add(events.get(info.position));
                Toast.makeText(getApplicationContext(),"Event saved", Toast.LENGTH_SHORT).show();
                //switchToView(saves, 1);
                return true;

            case R.id.remind:
                reminds.add(events.get(info.position));
                //Toast.makeText(getApplicationContext(),"Reminder added", Toast.LENGTH_SHORT).show();
                //switchToView(reminds,2);
                Events remindobj = events.get(info.position);
                Intent ir = new Intent(this, LocationService.class);
                ir.putExtra("Event_Obj", remindobj);
                startService(ir);
                return true;
            case R.id.navi:
                Events naviobj = events.get(info.position);
                Intent in = new Intent(this, MapsActivity.class);
                in.putExtra("Event_Obj", naviobj);
                startActivity(in);
                return true;
            case R.id.delete:
                deleteOption(info.position);
                Intent id = new Intent(this, LocationService.class);
                //stopService(id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteOption(int index) {
        switch (pagenumber){
            case 0:
                Toast toast = Toast.makeText(getApplicationContext(),"Cannot delete item from event list", Toast.LENGTH_SHORT);
                toast.show();
                return;
            case 1:
                saves.remove(index);
                switchToView(saves,1);
                return;
            case 2:
                reminds.remove(index);
                switchToView(reminds, 2);
                return;
            default:
                return;
        }
    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // Creating service handler class instance
            String jsonstr=null;
            try {
                jsonstr=getData(url);

            }catch (IOException e){
                e.printStackTrace();
            }

            // Making a request to url and getting response
            String jsonStr =jsonstr;

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    eventeles = jsonObj.getJSONArray(TAG);
                    //events = new ArrayList<Events>();
                    // looping through All Contacts

                    for (int i = 0; i <eventeles.length(); i++) {
                        JSONObject oneObject = eventeles.getJSONObject(i);

                        String date_obj = oneObject.getString("date");
                        String event_obj = oneObject.getString("event");
                        String location_obj = oneObject.getString("location");
                        double latitude_obj = oneObject.getDouble("latitude");
                        double longitude_obj = oneObject.getDouble("longitude");
                        // tmp hashmap for single contact
                        //HashMap<String, String> event_ele = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        //event_ele.put(DATE, date_obj);
                        //event_ele.put(EVENT, event_obj);
                        //event_ele.put(LOCATION, location_obj);


                        // adding contact to contact list
                        //Events newEvent= new Events(date_obj,event_obj,location_obj);
                        //EventList.add(Events, newEvent);
                        Events temp=new Events(event_obj,location_obj,date_obj, latitude_obj, longitude_obj);
                        events.add(temp);
                        temp=null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        // onPostExecute displays the results of the AsyncT
        // ListView list = (ListView) findViewById(R.id.eventlist);
        protected void onPostExecute(String result) {
            switchToView(events,0);
        }



    }





    private String getData(String strUrl) throws IOException {

        StringBuffer response=new StringBuffer();
        URL url = new URL(strUrl);
        HttpURLConnection httpconn=(HttpURLConnection) url.openConnection();
        if(httpconn.getResponseCode()==HttpURLConnection.HTTP_OK){
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(httpconn.getInputStream(), "UTF-8"));
            String strline= null;
            while((strline=input.readLine()) != null){
                response.append(strline);
            }
            input.close();
        }
        return response.toString();
    }
}
