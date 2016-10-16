package com.appteve.ViberlyApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.appteve.ViberlyApp.adapter.TrackplaylistAdapter;
import com.appteve.ViberlyApp.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchTrackActivity extends AppCompatActivity implements Constant {

    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();

    String idSearch;
    String nameSearch;
    String imageSearch;
    String endpointSearch;
    ListView searchList;
    View headerView;
    TrackplaylistAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_track);

        idSearch = ((AppController) SearchTrackActivity.this.getApplication()).getGLOBAL_SEARCH_ID();
        nameSearch = ((AppController) SearchTrackActivity.this.getApplication()).getGLOBAL_SEARCH_NAME();
        imageSearch = ((AppController) SearchTrackActivity.this.getApplication()).getGLOBAL_SEARCH_IMAGE();
        endpointSearch = ((AppController) SearchTrackActivity.this.getApplication()).getGLOBAL_SEARCH_ENDPOINT();

        searchList = (ListView) findViewById(R.id.searchTracksLayout);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        headerView = layoutInflater.inflate(R.layout.layout_header, null, false);


        searchList.addHeaderView(headerView);

        fetchData(endpointSearch, idSearch);


    }

    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(SearchTrackActivity.this, SearchActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        finish();
    }

    public void fetchData(String sEndpoint, String ids){

        String request = BASE_BACKEND_URL + ENDPOINT_SEARCH_TRACK ;

        audioItems.clear();


        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put(sEndpoint,ids);
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

                                String file = trackz.getString("file");
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");
                                String  style = trackz.getString("style");
                                String  album = trackz.getString("album");
                                String artist =  trackz.getString("artist");
                                String playCount =  trackz.getString("plays");

                                audioItems.add(new AudioItem (file, name, artist, id,  playCount, album, style));

                            }

                            makeDraws();
                            System.out.println("MAKE DRAV - go");

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

    public void makeDraws(){

        System.out.println("MAKE DRAV - +++++++++");

        try {


            TextView textHd = (TextView) headerView.findViewById(R.id.text);
            textHd.setText(nameSearch);
            TextView textHdCount = (TextView) headerView.findViewById(R.id.headerTitleB);

            if (audioItems.size() == 0){

                textHdCount.setText("Tracks: 0");

            } else {

                textHdCount.setText("Tracks: " + audioItems.size());
            }

            System.out.println("MAKE DRAV 1");






            adapter = new TrackplaylistAdapter(getApplicationContext(), audioItems);
            searchList.setAdapter(adapter);

            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SearchTrackActivity.this, PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    System.out.println("MAKE DRAV 3");


                   // intent.putExtra("track",audioItems);
                   // intent.putExtra("position",position - 1);
                    ((AppController) SearchTrackActivity.this.getApplication()).setGLOBAL_AUDIO_ARRAY(audioItems);
                    ((AppController) SearchTrackActivity.this.getApplication()).setPositionPlay(position - 1);
                    intent.putExtra("activity","search");


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
