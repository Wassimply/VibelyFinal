package com.appteve.ViberlyApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.adapter.LikeAdapter;
import com.appteve.ViberlyApp.constant.Constant;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constant {

    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();
    SharedPreferences sPref;
    ListView stationList;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        stationList = (ListView) findViewById(R.id.likeListView);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        headerView = layoutInflater.inflate(R.layout.layout_header, null, false);

        stationList.addHeaderView(headerView);

        fetchData();
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
        getMenuInflater().inflate(R.menu.like, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_radio) {

            Intent searchIntent = new Intent(LikeActivity.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_myplaylist) {

            Intent searchIntent = new Intent(LikeActivity.this, MyplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_publicplaylist) {

            Intent searchIntent = new Intent(LikeActivity.this, PublicplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_like) {

            Intent searchIntent = new Intent(LikeActivity.this, LikeActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_search) {

            Intent searchIntent = new Intent(LikeActivity.this, SearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_track) {

            Intent searchIntent = new Intent(LikeActivity.this, TrackSearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_top) {

            Intent searchIntent = new Intent(LikeActivity.this, TrendActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_offline) {

            Intent searchIntent = new Intent(LikeActivity.this, OfflineActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_profile) {

            Intent searchIntent = new Intent(LikeActivity.this, ProfileActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fetchData(){

        audioItems.clear();

        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_USER_LIKE + user_id + "&X-API-KEY=" + API_KEY;

        System.out.println(request);

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                request, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray trendArray = response.getJSONArray("respon");

                            for (int i = 0; i < trendArray.length(); i++) {

                                JSONArray arrayz = trendArray.getJSONArray(i);


                                JSONObject trackz = (JSONObject) arrayz.get(0);

                                String file = trackz.getString("file");
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");
                                String  style = trackz.getString("style");
                                String  album = trackz.getString("album");
                                String artist =  trackz.getString("artist");
                                String playCount =  trackz.getString("plays");

                                audioItems.add(new AudioItem (file, name, artist, id,  playCount, album, style));

                            }

                            makeDraw();

                        } catch (JSONException e) {
                            //some exception handler code.
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("RESP", "Error: " + error.getMessage());
                // pDialog.hide();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public  void makeDraw(){

        try {



            TextView hesds = (TextView) headerView.findViewById(R.id.text);
            hesds.setText("Like Tracks");

            TextView hesds2 = (TextView) headerView.findViewById(R.id.headerTitleB);

            if (audioItems.size() == 0){

                hesds2.setText("Tracks: 0");

            } else {

                hesds2.setText("Tracks: " + audioItems.size());
            }

            LikeAdapter adapter = new LikeAdapter(getApplicationContext(), audioItems);
            stationList.setAdapter(adapter);

            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(LikeActivity.this, PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    ((AppController) LikeActivity.this.getApplication()).setGLOBAL_AUDIO_ARRAY(audioItems);
                    ((AppController) LikeActivity.this.getApplication()).setPositionPlay(position - 1);
                    intent.putExtra("activity","like");

                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            });

            stationList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    String name_track = audioItems.get(pos - 1).getTitle();
                    final int posz = pos;

                    new LovelyStandardDialog(LikeActivity.this)
                            .setTopColorRes(R.color.colorGreen)
                            .setButtonsColorRes(R.color.colorBlack)
                            .setIcon(R.drawable.ic_menu_manage)
                            .setTitle("Delete track : "+ name_track )
                            .setMessage("Are you sure you want to delete this track?")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    processDeleteLikeTrack(posz - 1);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();



                    return true;
                }
            });
        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "No File", Toast.LENGTH_LONG).show();
        }


    }

    public void processDeleteLikeTrack(int poz){

        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");


        String id_track = audioItems.get(poz).getTrackId();


        String request = BASE_BACKEND_URL + ENDPOINT_TRACK_DISLIKE_ONE + id_track + "&id_user=" + user_id +"&X-API-KEY=" + API_KEY;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        fetchData();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("RESP", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }
}
