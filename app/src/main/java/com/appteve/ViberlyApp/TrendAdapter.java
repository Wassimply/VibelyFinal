package com.appteve.ViberlyApp;

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
import com.appteve.ViberlyApp.constant.Constant;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by appteve on 10/08/16.
 */
public class TrendAdapter extends ArrayAdapter implements Constant {

    private final ArrayList<AudioItem> trendtracks;
    private final Context context;
    private  String imagez;

    public TrendAdapter(Context context, ArrayList<AudioItem> trendtracks) {
        super(context, R.layout.trend_item, trendtracks);
        this.trendtracks = trendtracks;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.trend_item, parent, false);
        TextView trackName = (TextView) item.findViewById(R.id.trendNameTrack);

        trackName.setText(trendtracks.get(position).getTitle());
        TextView countName = (TextView) item.findViewById(R.id.trendCount);
        countName.setText(trendtracks.get(position).getPlayCount());
       final ImageView categoryImage = (ImageView) item.findViewById(R.id.trendCoverTrack);


        /////////////////////////


        String trackTitleArtist = trendtracks.get(position).getTitle();

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

        ////////////////////////












        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    public String searchCover(String artist){

        String url = "https://itunes.apple.com/search?term="+ artist + "&limit=2";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONArray resultsz = response.getJSONArray("results");
                            JSONObject obg = resultsz.optJSONObject(0);
                            String image100 = obg.getString("artworkUrl100");



                            imagez = image100.replace("100x100bb","800x800bb");

                         //   System.out.println("IMAGE 100 ---- " + imagez );


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

        return imagez;

    }



}
