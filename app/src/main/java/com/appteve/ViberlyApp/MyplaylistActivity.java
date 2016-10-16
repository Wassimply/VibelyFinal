package com.appteve.ViberlyApp;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.adapter.MyplaylistAdapter;
import com.appteve.ViberlyApp.constant.Constant;
import com.appteve.ViberlyApp.model.MyplaylistItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyplaylistActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constant {

    final ArrayList<MyplaylistItem> myplaylistItem = new ArrayList<MyplaylistItem>();
    ListView myplistView ;
    private ProgressDialog pDialog;
    SharedPreferences sPref;
    MyplaylistAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplaylist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myplistView = (ListView) findViewById(R.id.myplaylistListView);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        View headerView = layoutInflater.inflate(R.layout.layout_header_myplaylist, null, false);

        myplistView.addHeaderView(headerView);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fetchDataMyPlaylist();

        MobileAds.initialize(getApplicationContext(), AD_BANNER_ID);

        AdRequest adRequest = new AdRequest.Builder().build();
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
        getMenuInflater().inflate(R.menu.myplaylist, menu);
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

            addPlaylist();
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

            Intent searchIntent = new Intent(MyplaylistActivity.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_myplaylist) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, MyplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_publicplaylist) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, PublicplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_like) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, LikeActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_search) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, SearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_track) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, TrackSearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_top) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, TrendActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_offline) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, OfflineActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_profile) {

            Intent searchIntent = new Intent(MyplaylistActivity.this, ProfileActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void fetchDataMyPlaylist(){

        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_ALL_USER_PLIST;

        myplaylistItem.clear();

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray trendArray = response.getJSONArray("respon");

                            for (int i = 0; i < trendArray.length(); i++) {

                                JSONObject trackz = (JSONObject) trendArray.get(i);

                                int isPrivate = trackz.getInt("is_private");
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");

                                myplaylistItem.add(new MyplaylistItem(id,name,isPrivate));

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

            adapter = new MyplaylistAdapter(getApplicationContext(), myplaylistItem);

            myplistView.setAdapter(adapter);
            myplistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyplaylistActivity.this, TrackplaylistActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    ((AppController) MyplaylistActivity.this.getApplication()).setGLOBAL_PLAYLIST_USER_TRACK_ID(myplaylistItem.get(position - 1).getIdPlaylist());
                    ((AppController) MyplaylistActivity.this.getApplication()).setGLOBAL_PLAYLIST_NAME(myplaylistItem.get(position - 1).getNamePlaylist());
                    ((AppController) MyplaylistActivity.this.getApplication()).setGLOBAL_PLAYLIST_IS_PRIVATE(myplaylistItem.get(position - 1).getIsPrivate());

                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            });
            myplistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    final int posz = pos;

                    String name_playlist = myplaylistItem.get(pos - 1).getNamePlaylist();

                    new LovelyStandardDialog(MyplaylistActivity.this)
                            .setTopColorRes(R.color.colorGreen)
                            .setButtonsColorRes(R.color.colorBlack)
                            .setIcon(R.drawable.ic_menu_manage)
                            .setTitle("Delete playlist : "+ name_playlist )
                            .setMessage("Are you sure you want to delete this playlist?")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removePlaylist(posz - 1);
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

    public void addPlaylist(){

        new LovelyTextInputDialog(this, R.style.TintTheme)
                .setTopColorRes(R.color.colorGreen)
                .setTitle("Add new playlist")
                .setMessage("Message")
                .setIcon(R.drawable.ic_menu_gallery)

                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {

                        processAddPlaylist(text);


                    }
                })
                .show();


    }

    public void processAddPlaylist(String playlist_name){


        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_PLIST_ADD;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("user_id", user_id);
            params.put("name",playlist_name);
          //  params.put("is_private","1");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String ddr = response.getString("respon");

                            fetchDataMyPlaylist();

                        } catch (JSONException e) {

                        }






                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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

    public void removePlaylist (int pos){

        String id_playlist = myplaylistItem.get(pos).getIdPlaylist();

        String request = BASE_BACKEND_URL + ENDPOINT_PLIST_DELETE;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("id", id_playlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                         fetchDataMyPlaylist();

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
