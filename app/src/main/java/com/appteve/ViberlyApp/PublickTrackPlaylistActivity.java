package com.appteve.ViberlyApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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



public class PublickTrackPlaylistActivity extends AppCompatActivity implements Constant {

    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();
    private ProgressDialog pDialog;
    String idPlaylist;
    Context context;
    String playlistName;
    Button privateBtn;
    int is_Private;
    TrackplaylistAdapter adapter;
    ListView stationList;
    View headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publick_track_playlist);


        idPlaylist = ((AppController) PublickTrackPlaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_USER_TRACK_ID();
        playlistName = ((AppController) PublickTrackPlaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_NAME();
        is_Private = ((AppController) PublickTrackPlaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_IS_PRIVATE();

        stationList = (ListView) findViewById(R.id.publicktrackList);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        headerView = layoutInflater.inflate(R.layout.layout_header, null, false);

        stationList.addHeaderView(headerView);

        fetchData();
    }

    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(PublickTrackPlaylistActivity.this, PublicplaylistActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        finish();
    }



    public void fetchData(){

        String request = BASE_BACKEND_URL + ENDPOINT_TRAK_IN_PLIST + idPlaylist + "&X-API-KEY=" + API_KEY ;

        audioItems.clear();

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


            TextView textHd = (TextView) headerView.findViewById(R.id.text);
            textHd.setText(playlistName);
            TextView textHdCount = (TextView) headerView.findViewById(R.id.headerTitleB);

            if (audioItems.size() == 0){

                textHdCount.setText("Tracks: 0");

            } else {

                textHdCount.setText("Tracks: " + audioItems.size());
            }

            adapter = new TrackplaylistAdapter(getApplicationContext(), audioItems);
            stationList.setAdapter(adapter);

            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PublickTrackPlaylistActivity.this, PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    ((AppController) PublickTrackPlaylistActivity.this.getApplication()).setGLOBAL_AUDIO_ARRAY(audioItems);
                    ((AppController) PublickTrackPlaylistActivity.this.getApplication()).setPositionPlay(position - 1);
                    intent.putExtra("activity","pubplaylist");


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




