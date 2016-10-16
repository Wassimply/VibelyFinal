package com.appteve.ViberlyApp.database;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moolajoo.joao.runmusicplayer.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by joao on 26/09/16.
 */

public class DBFirebase {
    public static final String TAG = DBFirebase.class.getSimpleName();
    ;
    private static final ArrayList<String> playlist = new ArrayList<String>();
    private static Context mContext;
    private static FirebaseDatabase db;

    public DBFirebase() {

    }

    public DBFirebase(Context context) {
        mContext = context;
        FirebaseApp.initializeApp(mContext);

    }

    public ArrayList<String> getPlaylist() {
        return playlist;
    }

    public static int getPlaylistSize(String username) {
        return playlist.size();
    }

    public static void connectToFirebase(String spotifyUsername) {
//        FirebaseApp.initializeApp(mContext);

        db = FirebaseDatabase.getInstance();
        Log.d(TAG + " db", db.getReference().child(spotifyUsername).toString());
        DatabaseReference myRef = db.getReference(spotifyUsername);

        Query query = myRef.orderByChild("tempo");

        //read from ordered database
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long playlistSize = dataSnapshot.getChildrenCount();
                    Log.d(TAG, "There are " + playlistSize + " songs");
                    //save sharedPreferences here with the size, so next time the app fetch the data
                    //dont go from the beginning

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (!postSnapshot.child("tempo").getValue().toString().equals("0")) {
                            playlist.add(postSnapshot.child("uri").getValue().toString());
//                            System.out.println(postSnapshot.child("tempo").getValue() + " - " + postSnapshot.child("id").getValue());
                        }

                    }
//                    long seed = System.nanoTime();
//                    Collections.shuffle(playlist, new Random(seed));
                    MainActivity.dismissDialog();
                    MainActivity.setPlaylist(getWalkingPlaylist(), getRunningPlaylist());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void addToDatabase(String spotifyUsername, JSONObject song) throws JSONException {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        Log.d(TAG + " db", db.getReference().child(spotifyUsername).toString());
        DatabaseReference myRef = db.getReference(spotifyUsername);
//        Log.d("ref", myRef.toString());
        try {
            myRef.child(song.get("id").toString()).setValue(toMap(song));
        } catch (Exception e) {
            Log.e("Add Firebase DB", e.getMessage());
        }
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else {
            return json;
        }
    }

    private static List<String> getWalkingPlaylist() {
        List<String> list = playlist.subList(0, 100);
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));

        return list;
    }

    public static List<String> getRunningPlaylist() {
        List<String> list = playlist.subList(playlist.size() - 100, playlist.size() - 1);
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));

        return list;
    }

}
