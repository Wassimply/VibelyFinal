package com.appteve.ViberlyApp.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.AppController;
import com.appteve.ViberlyApp.AudioItem;
import com.appteve.ViberlyApp.R;
import com.appteve.ViberlyApp.constant.Constant;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by appteve on 15/08/16.
 */


public class TrackplaylistAdapter extends ArrayAdapter implements Constant {

    private  final ArrayList<AudioItem> myPlaylistArray;
    private  final Context context;
    private  String imagez;

    public TrackplaylistAdapter(Context context, ArrayList<AudioItem> myPlaylistArray){
        super(context, R.layout.trackplaylist_item,myPlaylistArray);
        this.myPlaylistArray = myPlaylistArray;
        this.context = context;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.trackplaylist_item, parent, false);
        TextView playlistName = (TextView) item.findViewById(R.id.playlistNameTrack);

        playlistName.setText(myPlaylistArray.get(position).getTitle());

        final ImageView categoryImage = (ImageView) item.findViewById(R.id.playlistCoverTrack);

        String trackTitleArtist = myPlaylistArray.get(position).getTitle();

        String[] separated = trackTitleArtist.split(" - ");
        String clearReq  = separated[0].replaceAll(" ", "%20");

        String url = "https://api.spotify.com/v1/search?q="+ clearReq +"&type=artist";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONObject trackObject = response.getJSONObject("artists");
                            JSONArray itemarray = trackObject.getJSONArray("items");

                            if (itemarray.length() == 0){

                                String urlImageFile = DEF_IMAGE;

                                imagez = urlImageFile;

                            } else {

                                JSONObject itrms = itemarray.getJSONObject(0);
                                JSONArray images = itrms.getJSONArray("images");
                                JSONObject imarr = images.getJSONObject(0);
                                String urlcover = imarr.getString("url");

                                imagez = urlcover;

                                Glide.with(getContext())
                                        .load(imagez)
                                        .placeholder(R.drawable.defim)
                                        .centerCrop()
                                        .animate( R.anim.slide_in_left )
                                        .into(categoryImage);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ARRE", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

}
