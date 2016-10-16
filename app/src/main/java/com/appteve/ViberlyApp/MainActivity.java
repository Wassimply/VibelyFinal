package com.appteve.ViberlyApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.constant.Constant;
import com.appteve.ViberlyApp.adapter.RadioAdapter;
import com.appteve.ViberlyApp.model.RadioItem;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constant {

    private ProgressDialog pDialog;
    SharedPreferences userPreference;
    private GridView gridView;
    private RadioAdapter radioAdapter;




    final ArrayList<RadioItem>  radios = new ArrayList<RadioItem>();
    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }







        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createList();



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
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {

            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            intent.putExtra("activity","play");
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);



        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_radio) {

            Intent searchIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_myplaylist) {

            Intent searchIntent = new Intent(MainActivity.this, MyplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_publicplaylist) {

            Intent searchIntent = new Intent(MainActivity.this, PublicplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_like) {

            Intent searchIntent = new Intent(MainActivity.this, LikeActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_search) {

            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_track) {

            Intent searchIntent = new Intent(MainActivity.this, TrackSearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_top) {

            Intent searchIntent = new Intent(MainActivity.this, TrendActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_offline) {

            Intent searchIntent = new Intent(MainActivity.this, OfflineActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_profile) {

            Intent searchIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createList(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        String request = BASE_BACKEND_URL + ENDPOINT_ALL_STYLE + X_API_KEY + API_KEY;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                request, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray trendArray = response.getJSONArray("respon");

                            for (int i = 0; i < trendArray.length(); i++) {

                                JSONObject trackz = (JSONObject) trendArray.get(i);

                                String id = trackz.getString("id");
                                String styleName = trackz.getString("name");
                                String  imageFile = trackz.getString("image");

                                radios.add(new RadioItem (id,imageFile,styleName));


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

    public void makeDraw(){


        gridView = (GridView) findViewById(R.id.gridView);
        radioAdapter = new RadioAdapter(this, R.layout.grid_item_layout, radios );
        gridView.setAdapter(radioAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                RadioItem item = (RadioItem) parent.getItemAtPosition(position);
                String idStyle = item.getIdStyle();

                fetchDataTrack(idStyle);

            }
        });

    }

    public void fetchDataTrack(String id){

        String request = BASE_BACKEND_URL + ENDPOINT_ALL_TRACK_BY_STYLE + id + "&X-API-KEY=" + API_KEY;

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

                                JSONObject trackz = (JSONObject) trendArray.get(i);


                                String file = trackz.getString("file");
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");
                                String  style = trackz.getString("style");
                                String  album = trackz.getString("album");
                                String artist =  trackz.getString("artist");
                                String playCount =  trackz.getString("plays");

                                audioItems.add(new AudioItem (file, name, artist, id,  playCount, album, style));

                            }

                            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);

                            ((AppController) MainActivity.this.getApplication()).setGLOBAL_AUDIO_ARRAY(audioItems);
                            ((AppController) MainActivity.this.getApplication()).setPositionPlay(0);
                            intent.putExtra("activity","main");

                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);




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


}
