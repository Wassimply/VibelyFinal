package com.appteve.ViberlyApp.spotify;

/**
 * Created by joao on 01/10/16.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.moolajoo.joao.runmusicplayer.database.DBFirebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchSpotifySavedTracks extends AsyncTask<String, Void, JSONObject> {

    private static String mToken;
    private static String username;
    private static boolean searchCompleted = false;
    private int offset = 0;
    private int limit = 50;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        offset = 0;// DBFirebase.getPlaylistSize(username);

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        mToken = params[0];
        username = params[1];
//        System.out.println("offset" + offset);
        try {
            String pathUser = "https://api.spotify.com/v1/me";
            while (!searchCompleted) {
                String pathTracks = pathUser + "/tracks?offset=" + offset + "&limit=" + limit;
                try {
                    getUserTracksFromJson(getStringFromURL(pathTracks));
                    offset += limit;
                } catch (JSONException e) {
                    Log.e("Erro", e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("User Tracks from Json", e.getMessage());
            //deu erro, conecta com o q tem...
        }
        return null;
    }

    protected void onPostExecute(JSONObject user) {

        try {
            DBFirebase.connectToFirebase(username);
        } catch (Exception e) {
            Log.e("e", e.getMessage());
        }

    }

    private static String getStringFromURL(String path) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJsonStr = null;

        try {
//            String path = "https://api.spotify.com/v1/me";

            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept: ", "application/json");
            urlConnection.setRequestProperty("Authorization: ", "Bearer " + mToken);
            urlConnection.setRequestProperty("Retry-After: ", "20");
            urlConnection.connect();
//            Log.i("code", String.valueOf(urlConnection.getResponseCode()));
//            Log.i("code", String.valueOf(urlConnection.getResponseMessage()));


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            userJsonStr = buffer.toString();

            Log.d("in background" , userJsonStr);

        } catch (IOException e) {
            Log.d("offset error", e.getMessage());
            Log.e("Error", "Error ", e);
//            Snackbar.make(getView(), "Error loading data, check your internet connection", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error", "Error closing stream", e);
                }
            }
        }
        return userJsonStr;
    }


    private static JSONObject getUserTracksFromJson(String userJson)
            throws JSONException {
        JSONObject userObj = new JSONObject(userJson);
        if (userObj.get("next").equals(null)) {
            searchCompleted = true;
            Log.d("Fetch User Tracks", "It's finally over");
        }
        JSONArray ar = userObj.getJSONArray("items");
        //while obj.get("next") != null, call task
//        System.out.println(userObj.get("total"));


        for (int i = 0; i < ar.length(); i++) {
            JSONObject j = (JSONObject) ar.getJSONObject(i).get("track");
            getSongInfoFromSongID(j.get("id").toString());
//            System.out.println(j.get("id"));

//            userSavedTracks.add(j.get("uri").toString());
//            System.out.println(j.get("uri"));

        }

        return userObj;
    }

    private static void getSongInfoFromSongID(String songID)
            throws JSONException {
        JSONObject userObj = new JSONObject(getStringFromURL("https://api.spotify.com/v1/audio-features/" + songID));
//        JSONObject song = userObj;
        DBFirebase.addToDatabase(username, userObj);
//        return userObj;
    }

}

