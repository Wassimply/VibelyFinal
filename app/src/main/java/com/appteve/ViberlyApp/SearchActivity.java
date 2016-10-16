package com.appteve.ViberlyApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.adapter.SearchAdapter;
import com.appteve.ViberlyApp.constant.Constant;
import com.appteve.ViberlyApp.model.SearchItem;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MaterialSearchBar.OnSearchActionListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener,Constant {

    private MaterialSearchBar searchBar;
    private List<String> lastSearches;
    private FrameLayout dim;
    final ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    ListView searchList;

    String searchEndpoint;
    String endpoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEndpoint = ENDPOINT_SEARCH_ARTIST;
        endpoint = "artist_id";



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dim = (FrameLayout) findViewById(R.id.dim);

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
          searchBar.inflateMenu(R.menu.search_menu);
         searchBar.getMenu().setOnMenuItemClickListener(this);

        searchList = (ListView) findViewById(R.id.searchArtistLayout);

       // dataSearch(DEF_TRACK_SEARCH, searchEndpoint);
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_radio) {

            Intent searchIntent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_myplaylist) {

            Intent searchIntent = new Intent(SearchActivity.this, MyplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_publicplaylist) {

            Intent searchIntent = new Intent(SearchActivity.this, PublicplaylistActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_like) {

            Intent searchIntent = new Intent(SearchActivity.this, LikeActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_search) {

            Intent searchIntent = new Intent(SearchActivity.this, SearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_track) {

            Intent searchIntent = new Intent(SearchActivity.this, TrackSearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_top) {

            Intent searchIntent = new Intent(SearchActivity.this, TrendActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_offline) {

            Intent searchIntent = new Intent(SearchActivity.this, OfflineActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_profile) {

            Intent searchIntent = new Intent(SearchActivity.this, ProfileActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {


       /* if (enabled){
            dim.setVisibility(View.VISIBLE);
        }else {
            dim.setVisibility(View.GONE);
        }*/
    }

    //called when user confirms request
    @Override
    public void onSearchConfirmed(CharSequence text) {
//        startSearch(text.toString(), true, null, true);

        System.out.println("SEARCH: - " + text.toString());
        dataSearch(text.toString(), searchEndpoint);
    }

    //called when microphone icon clicked
    @Override
    public void onSpeechIconSelected() {
        // openVoiceRecognizer();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_album) {

            searchEndpoint = ENDPOINT_SEARCH_ALBUM;
            endpoint = "album_id";

        }
        if (id == R.id.action_artist) {

            searchEndpoint = ENDPOINT_SEARCH_ARTIST;
            endpoint = "artist_id";

        }
        if (id == R.id.action_style) {

            searchEndpoint = EBDPOINT_SEARCH_STYLE;
            endpoint = "style_id";

        }

        System.out.println(getClass().getSimpleName() + ": item clicked " + item.getTitle());
        return false;
    }

    public void dataSearch( String textsearch , String endpoint)  {

        searchItems.clear();

        String requestSearchTRack = BASE_BACKEND_URL + endpoint  ;

        System.out.println("SEARCH URL --- " + requestSearchTRack);

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("searchterm", textsearch);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                requestSearchTRack, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {




                        try {


                            JSONArray trendArray = response.getJSONArray("respon");
                            System.out.println(trendArray);

                            System.out.println(response);



                            for (int i = 0; i < trendArray.length(); i++) {

                                JSONObject trackz = (JSONObject) trendArray.get(i);


                                String file = trackz.getString("image");
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");

                                System.out.println("RES I - - - - -" + i);


                                searchItems.add(new SearchItem(id,name,file));

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



        };


        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public  void makeDraw(){


        System.out.println("SEARCH DRAWW - -- - - -   - " + searchItems.size() );

        try {



            SearchAdapter adapter = new SearchAdapter(getApplicationContext(), searchItems);
            searchList.setAdapter(adapter);

            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SearchActivity.this, SearchTrackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    System.out.println("POSICION: - " + position);


                    ((AppController) SearchActivity.this.getApplication()).setGLOBAL_SEARCH_ID(searchItems.get(position ).getidSearchItem());
                    ((AppController) SearchActivity.this.getApplication()).setGLOBAL_SEARCH_NAME(searchItems.get(position ).getnameSearchItem());
                    ((AppController) SearchActivity.this.getApplication()).setGLOBAL_SEARCH_IMAGE(searchItems.get(position ).getImageSearchItem());
                    ((AppController) SearchActivity.this.getApplication()).setGLOBAL_SEARCH_ENDPOINT(endpoint);






                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    finish();
                }
            });


        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "No File", Toast.LENGTH_LONG).show();
        }


    }



}

