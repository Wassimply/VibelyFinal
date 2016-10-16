package com.appteve.ViberlyApp;

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
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class TrackplaylistActivity extends AppCompatActivity implements Constant {

    final ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();

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
        setContentView(R.layout.activity_trackplaylist);


        idPlaylist = ((AppController) TrackplaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_USER_TRACK_ID();
        playlistName = ((AppController) TrackplaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_NAME();
        is_Private = ((AppController) TrackplaylistActivity.this.getApplication()).getGLOBAL_PLAYLIST_IS_PRIVATE();


        stationList = (ListView) findViewById(R.id.trackList);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        headerView = layoutInflater.inflate(R.layout.layout_header_trackplist, null, false);


        stationList.addHeaderView(headerView);

        fetchData();
    }

    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(TrackplaylistActivity.this, MyplaylistActivity.class);
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


            TextView textHd = (TextView) headerView.findViewById(R.id.textHeads);
            textHd.setText(playlistName);
            TextView textHdCount = (TextView) headerView.findViewById(R.id.trackCount);

            if (audioItems.size() == 0){

                textHdCount.setText("Tracks: 0");

            } else {

                textHdCount.setText("Tracks: " + audioItems.size());
            }


            privateBtn = (Button) headerView.findViewById(R.id.playlistBtn);

            if (is_Private == 1){

                privateBtn.setText("Private");
            } else {
                privateBtn.setText("Public");
            }

          //  stationList.addHeaderView(headerView);



            adapter = new TrackplaylistAdapter(getApplicationContext(), audioItems);
            stationList.setAdapter(adapter);

            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(TrackplaylistActivity.this, PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                  //  intent.putExtra("track",audioItems);
                   // intent.putExtra("position",position - 1);

                    ((AppController) TrackplaylistActivity.this.getApplication()).setGLOBAL_AUDIO_ARRAY(audioItems);
                    ((AppController) TrackplaylistActivity.this.getApplication()).setPositionPlay(position -1 );
                    intent.putExtra("activity","myplaylist");


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

                    new LovelyStandardDialog(TrackplaylistActivity.this)
                            .setTopColorRes(R.color.colorGreen)
                            .setButtonsColorRes(R.color.colorBlack)
                            .setIcon(R.drawable.ic_menu_manage)
                            .setTitle("Delete track : "+ name_track )
                            .setMessage("Are you sure you want to delete this track?")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Toast.makeText(MyplaylistActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                    processRemoveTrack(posz - 1);
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

    public void playlistClickBtn (View v){


        if (is_Private == 1){

            privateBtn.setText("Public");
            ((AppController) TrackplaylistActivity.this.getApplication()).setGLOBAL_PLAYLIST_IS_PRIVATE(0);
            processPrivatePlaylist("0");


        } else {

            privateBtn.setText("Private");
            ((AppController) TrackplaylistActivity.this.getApplication()).setGLOBAL_PLAYLIST_IS_PRIVATE(1);
            processPrivatePlaylist("1");



        }


    }

    public void processPrivatePlaylist (String privates){


        String request = BASE_BACKEND_URL + ENDPOINT_SET_PRIVATE_PL;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("is_private", privates);
            params.put("id", idPlaylist);
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
                            System.out.println("PRIVATE??? --- " + trendArray);

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

    public void processRemoveTrack (int pos){

        String request = BASE_BACKEND_URL + ENDPOINT_DELETE_TRACK_IN_PL;

        String id_track = audioItems.get(pos).getTrackId();



        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("track_id", id_track);
            params.put("playlist_id", idPlaylist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(TrackplaylistActivity.this, "Track list deleted", Toast.LENGTH_SHORT).show();
                        System.out.println("DELETE??? --- " + response);
                        fetchData();



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



