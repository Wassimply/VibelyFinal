package com.appteve.ViberlyApp.spotify;

/**
 * Created by joao on 25/09/16.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.moolajoo.joao.runmusicplayer.MainActivity;
import com.moolajoo.joao.runmusicplayer.database.DBFirebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by joao on 09/01/16.
 */
public class FetchSpotifyUserData extends AsyncTask<String, Void, JSONObject> {

    private String mToken;


    @Override
    protected JSONObject doInBackground(String... params) {
        mToken = params[0];

        try {
            String pathUser = "https://api.spotify.com/v1/me";

            try {
                return getUserDataFromJson(getStringFromURL(pathUser));
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }

        return null;
    }

    protected void onPostExecute(JSONObject user) {

        try {
            String username = user.get("id").toString();
//            Log.d("USER INFO", username);

            if (MainActivity.firstAccess()) {
                Log.d("FIRST TIME ", "USING. WELCOME");
                FetchSpotifySavedTracks service = new FetchSpotifySavedTracks();
                service.execute(mToken, username);
            } else {
                Log.d("WELCOME BACK ", username);
                DBFirebase.connectToFirebase(username);
            }


//            Log.d("USER INFO", user.get("display_name").toString());
//            Log.d("USER INFO", user.get("email").toString());

//            JSONArray images = user.getJSONArray("images");
//            JSONObject avatarURL = images.getJSONObject(0);
//            Log.d("USER INFO", url.get("url").toString());
//            SpotifyActivity.createUser(user.get("id").toString(), user.get("display_name").toString(), avatarURL.get("url").toString(), mToken, userSavedTracks);
//            Log.i("size", String.valueOf(userSavedTracks.size()));

        } catch (Exception e) {
            Log.e("e", e.getMessage());
        }

    }

    private String getStringFromURL(String path) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJsonStr = null;

        try {

            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept: ", "application/json");
            urlConnection.setRequestProperty("Authorization: ", "Bearer " + mToken);
            urlConnection.connect();

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

        } catch (IOException e) {
            Log.e("Error", "Error ", e);

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

    public static JSONObject getUserDataFromJson(String userJson)
            throws JSONException {
        JSONObject userObj = new JSONObject(userJson);
        return userObj;
    }

}
