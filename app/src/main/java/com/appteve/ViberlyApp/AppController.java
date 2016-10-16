package com.appteve.ViberlyApp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.appteve.ViberlyApp.constant.Constant;

import java.util.ArrayList;

/**
 * Created by appteve on 08/08/16.
 */
public class AppController extends Application implements Constant {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    SharedPreferences sPref;
    String user;

    private String GLOBAL_TRACK_TITLE;
    private String GLOBAL_USER_ID;
    private String GLOBAL_PLAYLIST_USER_TRACK_ID;
    private String GLOBAL_PLAYLIST_NAME;
    private int GLOBAL_PLAYLIST_IS_PRIVATE;

    private String GLOBAL_SEARCH_ID;
    private String GLOBAL_SEARCH_NAME;
    private String GLOBAL_SEARCH_IMAGE;
    private String GLOBAL_SEARCH_ENDPOINT;

    private String GLOBAL_TRACK_URL;

    private ArrayList<AudioItem> GLOBAL_AUDIO_ARRAY = new ArrayList<AudioItem>();
    private int positionPlay;




    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        checkUserLogin();



    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void checkUserLogin(){

        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(sPref.contains(APP_PREFERENCES_ID)) {

        } else {

            Intent searchIntent = new Intent(AppController.this, LoginActivity.class);
            searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(searchIntent);

        }

    }

    public String getGLOBAL_TRACK_TITLE(){

        return GLOBAL_TRACK_TITLE;
    }

    public void setGLOBAL_TRACK_TITLE(String GLOBAL_TRACK_TITLE){

        this.GLOBAL_TRACK_TITLE = GLOBAL_TRACK_TITLE;
    }

    public String getGLOBAL_PLAYLIST_USER_TRACK_ID(){
        return GLOBAL_PLAYLIST_USER_TRACK_ID;
    }
    public void setGLOBAL_PLAYLIST_USER_TRACK_ID(String GLOBAL_PLAYLIST_USER_TRACK_ID){
        this.GLOBAL_PLAYLIST_USER_TRACK_ID = GLOBAL_PLAYLIST_USER_TRACK_ID;
    }

    public String getGLOBAL_PLAYLIST_NAME(){
        return GLOBAL_PLAYLIST_NAME;
    }
    public void setGLOBAL_PLAYLIST_NAME(String GLOBAL_PLAYLIST_NAME){
        this.GLOBAL_PLAYLIST_NAME = GLOBAL_PLAYLIST_NAME;
    }
    public int getGLOBAL_PLAYLIST_IS_PRIVATE(){
        return GLOBAL_PLAYLIST_IS_PRIVATE;
    }
    public void setGLOBAL_PLAYLIST_IS_PRIVATE(int GLOBAL_PLAYLIST_IS_PRIVATE){
        this.GLOBAL_PLAYLIST_IS_PRIVATE = GLOBAL_PLAYLIST_IS_PRIVATE;
    }

    /// global search singleton

    public String getGLOBAL_SEARCH_ID(){
        return  GLOBAL_SEARCH_ID;
    }
    public void setGLOBAL_SEARCH_ID(String GLOBAL_SEARCH_ID){
        this.GLOBAL_SEARCH_ID = GLOBAL_SEARCH_ID;
    }
    public String getGLOBAL_SEARCH_NAME(){
        return GLOBAL_SEARCH_NAME;
    }
    public void setGLOBAL_SEARCH_NAME(String GLOBAL_SEARCH_NAME){
        this.GLOBAL_SEARCH_NAME = GLOBAL_SEARCH_NAME;
    }
    public String getGLOBAL_SEARCH_IMAGE(){
        return GLOBAL_SEARCH_IMAGE;
    }
    public void setGLOBAL_SEARCH_IMAGE (String GLOBAL_SEARCH_IMAGE){
        this.GLOBAL_SEARCH_IMAGE = GLOBAL_SEARCH_IMAGE;
    }
    public String getGLOBAL_SEARCH_ENDPOINT(){
        return GLOBAL_SEARCH_ENDPOINT;
    }
    public void setGLOBAL_SEARCH_ENDPOINT(String GLOBAL_SEARCH_ENDPOINT){
        this.GLOBAL_SEARCH_ENDPOINT = GLOBAL_SEARCH_ENDPOINT;
    }




    public ArrayList<AudioItem> getGLOBAL_AUDIO_ARRAY(){
        return  GLOBAL_AUDIO_ARRAY;
    }
    public void setGLOBAL_AUDIO_ARRAY(ArrayList<AudioItem> GLOBAL_AUDIO_ARRAY){
        this.GLOBAL_AUDIO_ARRAY = GLOBAL_AUDIO_ARRAY;
    }


    public int getPositionPlay(){
        return positionPlay;
    }
    public void setPositionPlay(int positionPlay){
        this.positionPlay = positionPlay;
    }

    public String getGLOBAL_TRACK_URL(){
        return GLOBAL_TRACK_URL;
    }
    public void setGLOBAL_TRACK_URL(String GLOBAL_TRACK_URL){
        this.GLOBAL_TRACK_URL = GLOBAL_TRACK_URL;
    }


}
