package com.appteve.ViberlyApp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.appteve.ViberlyApp.constant.Constant;
import com.appteve.ViberlyApp.playerMenuHelper.PopMenuAdapter;
import com.appteve.ViberlyApp.playerMenuHelper.PopMenuOptions;
import com.appteve.ViberlyApp.playlistHelper.PlaylistAdapter;
import com.appteve.ViberlyApp.playlistHelper.PlaylistOptions;
import com.bumptech.glide.Glide;
import com.cleveroad.audiowidget.AudioWidget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class PlayerActivity extends AppCompatActivity implements Constant {


    ArrayList<AudioItem> trackListArraySend = new ArrayList<AudioItem>();
    final  ArrayList<PlaylistOptions> resultPop = new ArrayList<PlaylistOptions>();
    private CropCircleTransformation cropCircleTransformation;
    private static final long UPDATE_INTERVAL = 1000;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    public static String RESTORE_CURRENT_TRACK = "restore_current_track";


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public BroadcastReceiver playReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == PlayerService.ACTION_PLAY_COMPLETED) {



                selected = intent.getIntExtra("SELECT",0);
                artistName = intent.getStringExtra("ARTIST");
                trackId = intent.getStringExtra("TRACKID");

                titleMusic.setText(artistName);

                String[] separated = artistName.split(" - ");
                String artist = separated[0]; // this will contain "Fruit"
                String clearReq  = artist.replaceAll(" ", "%20");

                if(activity.equals("offline")){

                } else {

                    coverImage(clearReq);
                    checkLike();

                }


            }

            if (intent.getAction() == PlayerService.ACTION_PLAY_CHANGE) {

                selected = intent.getIntExtra("SELECT",0);
                artistName = intent.getStringExtra("ARTIST");
                trackId = intent.getStringExtra("TRACKID");

                /// change title and find cover

                titleMusic.setText(artistName);

                String[] separated = artistName.split(" - ");
                String artist = separated[0]; // this will contain "Fruit"
                String clearReq  = artist.replaceAll(" ", "%20");

                if(activity.equals("offline")){

                } else {

                    coverImage(clearReq);
                    checkLike();

                }



            }

            if (intent.getAction() == PlayerService.ACTION_PLAY_START){

                selected = intent.getIntExtra("SELECT",0);


            }


        }
    };

    public static AudioWidget audioWidget;
    private SeekBar seekBar;

    InterstitialAd mInterstitialAd;
    Handler handler= new Handler();
    Context context;
    ProgressBar progressBar;
    ProgressDialog dialogz;
    String files;
    String names;
    String urls;
    String activity;
    String imagez;
    ImageView coverArtisiImage;
    String artistName;
    SharedPreferences sPref;
    String trackId;
    ImageButton playPauseButton;
    ImageView next;
    ImageView previous;
    Drawable drawable;
    private Timer timer;
    TextView totalTime;
    TextView currentTime;
    TextView titleMusic;
    private SharedPreferences preferences;
    String mediaUrl;
    String url_clear;
    int mDuration;
    PlayerService service;
    boolean bounded;
    int position;
    int selected;
    ImageView  likeButton;
    Uri outputFileUri;
    boolean registredRessiver;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_player);



        activity = getIntent().getExtras().getString("activity");

        service = (new PlayerService());



            trackListArraySend = ((AppController) PlayerActivity.this.getApplication()).getGLOBAL_AUDIO_ARRAY();
            selected = ((AppController) PlayerActivity.this.getApplication()).getPositionPlay();





        if (activity.equals("main")){

            Random r = new Random();
            int i1 = r.nextInt(trackListArraySend.size() );
            selected = i1;


        }

        artistName = trackListArraySend.get(selected).getTitle();
        trackId = trackListArraySend.get(selected).getTrackId();


        makeUI();


        if (activity.equals("offline")){



        } else {

            loadPlaylistOptions();
            makeFolder();
            startTrend();
            checkLike();


        }

        audioW();
        audioWidget.controller().start();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AD_INTERSTITIAL_ID);


        phonecall();
        verifyStoragePermissions(PlayerActivity.this);

        if(savedInstanceState==null) {
            // Stop service if it exists
            try {
                stopService(new Intent(PlayerActivity.this, PlayerService.class));

            } catch (Exception e) {}




                Intent i = new Intent(PlayerActivity.this, PlayerService.class);

                i.putExtra("URL", url_clear);
                i.putExtra("SONGS", trackListArraySend);
                i.putExtra("SELECTED", selected);
                i.putExtra("ACTIVITY", activity);

                startService(i);
                bindService(i, connection, Context.BIND_AUTO_CREATE);





        }else{
            selected = savedInstanceState.getInt(RESTORE_CURRENT_TRACK);
        }


        bindService(new Intent(PlayerActivity.this, PlayerService.class), connection, Context.BIND_AUTO_CREATE);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selected == 0){

                    selected = trackListArraySend.size() -1;

                } else {

                    selected = --selected;

                }

                changeTrack(selected);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selected == (trackListArraySend.size() - 1)){

                    selected = 0;

                } else {

                    selected = ++selected;

                }
                changeTrack(selected);




            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTrack();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) service.seekPosition(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });






    }



    public void audioW(){



        audioWidget = new AudioWidget.Builder(this)
                .lightColor(ContextCompat.getColor(this, R.color.colorBlack))
                .darkColor(ContextCompat.getColor(this, R.color.colorBlack))
                .expandWidgetColor(ContextCompat.getColor(this, R.color.colorBlack))
                .defaultAlbumDrawable(ContextCompat.getDrawable(this, R.drawable.defim_blk))
                .playDrawable(ContextCompat.getDrawable(this, R.drawable.playys))
                 .pauseDrawable(ContextCompat.getDrawable(this, R.drawable.pausse))
                .build();



       if (!audioWidget.isShown()) {


            audioWidget.show(100, 300);
        }

        audioWidget.controller().onControlsClickListener(new AudioWidget.OnControlsClickListener() {

            @Override
            public boolean onPlaylistClicked() {


                return true;
            }

            @Override
            public void onPreviousClicked() {
                // previous track button clicked
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (selected == 0){

                            selected = trackListArraySend.size() -1;

                        } else {

                            selected = --selected;

                        }

                        changeTrack(selected);

                    }
                }, 200);
            }

            @Override
            public boolean onPlayPauseClicked() {
                // return true to change playback state of widget and play button click animation (in collapsed state)
                pauseTrack();
                return true;

            }

            @Override
            public void onNextClicked() {
                // next track button clicked
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (selected == (trackListArraySend.size() - 1)){

                            selected = 0;

                        } else {

                            selected = ++selected;

                        }
                        changeTrack(selected);

                        changeTrack(selected);
                        //trackId = trackListArray.get(selected).getTrackId();

                    }
                }, 200);

            }

            @Override
            public void onAlbumClicked() {
                // album cover clicked
            }

            @Override
            public void onAlbumLongClicked() {
                // album cover long clicked
            }
            @Override
            public void onNextLongClicked() {
                // next track button long clicked
            }

            @Override
            public void onPlayPauseLongClicked() {
                // play/pause button long clicked
            }
            @Override
            public void onPreviousLongClicked() {
                // previous track button long clicked
            }
            @Override
            public void onPlaylistLongClicked() {
                // playlist button long clicked
            }

        });

        audioWidget.controller().onWidgetStateChangedListener(new AudioWidget.OnWidgetStateChangedListener() {
            @Override
            public void onWidgetStateChanged(@NonNull AudioWidget.State state) {
                // widget state changed (COLLAPSED, EXPANDED, REMOVED)
            }

            @Override
            public void onWidgetPositionChanged(int cx, int cy) {
                // widget position change. Save coordinates here to reuse them next time AudioWidget.show(int, int) called.
              /*  preferences.edit()
                        .putInt(KEY_POSITION_X, cx)
                        .putInt(KEY_POSITION_Y, cy)
                        .apply();*/
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();



         /*   if (!audioWidget.isShown()) {

                audioWidget.show(100, 300);
            } */

    }



    public void makeUI(){

        next = (ImageView) findViewById(R.id.playerNextBtn);
        previous = (ImageView) findViewById(R.id.playerFoewBtn);
        totalTime = (TextView) findViewById(R.id.totalTime);
        currentTime = (TextView) findViewById(R.id.currentTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        coverArtisiImage = (ImageView) findViewById(R.id.coverImage);
        playPauseButton =  (ImageButton) findViewById(R.id.playspause);
        likeButton = (ImageView) findViewById(R.id.imageLike) ;

        titleMusic = (TextView)findViewById(R.id.now_playing_text);


        if (activity.equals("offline")){

            trackId = "0";
            titleMusic.setText(artistName);
            likeButton.setVisibility(View.INVISIBLE);



        } else {

            titleMusic.setText(artistName);

            String[] separated = artistName.split(" - ");
            String artist = separated[0]; // this will contain "Fruit"
            String clearReq  = artist.replaceAll(" ", "%20");

            coverImage(clearReq);

        }

        Resources res = getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            drawable = res.getDrawable(R.drawable.pausse, getApplicationContext().getTheme());
        } else {
            drawable = res.getDrawable(R.drawable.pausse);


        }

        playPauseButton.setImageDrawable(drawable);

        cropCircleTransformation = new CropCircleTransformation(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {


            }
        }
    }

    public void makeFolder(){

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + SDCARD_FOLDER_W);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }
    }

    public  void phonecall(){

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                      service.mediaPlayer.pause();
                } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                   // service.mediaPlayer.start();

                } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }





    public ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.PlayerBinder mBinder= (PlayerService.PlayerBinder) iBinder;
            service = mBinder.getService();
            bounded= true;
            updateSeekbar();  //to-do
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("DISCONNECT", "DISCONNECTED");
            bounded = false;
        }
    };


    public void onDestroy() {
        super.onDestroy();
        if (bounded) {
            unbindService(connection);
            bounded = false;
        }

        unregisterReceiver(playReceiver);


    }

    public void pauseTrack(){
        if (!bounded) return;
        if (service.mPausePlay(position)) {

            Resources res = getResources();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
                drawable = res.getDrawable(R.drawable.pausse, getApplicationContext().getTheme());
            } else {
                drawable = res.getDrawable(R.drawable.pausse);
            }

            playPauseButton.setImageDrawable(drawable);
            audioWidget.controller().start();

        } else {

            Resources res = getResources();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
                drawable = res.getDrawable(R.drawable.playys, getApplicationContext().getTheme());
            } else {
                drawable = res.getDrawable(R.drawable.playys);
            }

            playPauseButton.setImageDrawable(drawable);
            audioWidget.controller().stop();

        }
    }

    public void changeTrack(int position){

        if (activity.equals("offline")){


            service.changeTrack(position);

        } else {


            startTrend();

            service.changeTrack(position);


        }

    }

  /*  public void continiousPlay(){

        if (selected == trackListArray.size() - 1){

            selected = 0;
        } else {

            selected = selected + 1;
        }

        if (activity.equals("main")){

            Random r = new Random();
            int i1 = r.nextInt(trackListArray.size() - 0 + 1) + 0;
            selected = i1;

        }

        if (activity.equals("offline")){

            String track = trackListArray.get(selected).getFile();
            String titles = trackListArray.get(selected).getTitle();
            titleMusic.setText(titles);

          //  service.changeTrack(track);


        } else {

            String track = BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_MUSICF + trackListArray.get(selected).getFile();
            String titles = trackListArray.get(selected).getTitle();
            titleMusic.setText(titles);
            artistName =  trackListArray.get(selected).getTitle();
            String urlsz = track.replaceAll(" ", "%20");

            String[] separated = titles.split(" - ");
            String artist = separated[0]; // this will contain "Fruit"
            String clearReq  = artist.replaceAll(" ", "%20");

            coverImage(clearReq);
            trackId = trackListArray.get(selected).getTrackId();
            checkLike();
            startTrend();

          //  service.changeTrack(urlsz);


        }




    } */

    protected void onPause() {
       // unregisterReceiver(playReceiver);
        super.onPause();

    }



    @Override
    public void onResume() {
        super.onResume();




        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlayerService.ACTION_PLAY_COMPLETED);
        intentFilter.addAction(PlayerService.ACTION_PLAY_CHANGE);
        intentFilter.addAction(PlayerService.ACTION_PLAY_START);

        PlayerActivity.this.registerReceiver(this.playReceiver, intentFilter);

      //  PlayerActivity.this.registerReceiver(this.playReceiver, intentFilter);



          /*  if (!audioWidget.isShown()) {

                audioWidget.show(100, 300);
            } */




    }

    @Override
    protected void onStop()
    {


        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RESTORE_CURRENT_TRACK, selected);
    }

    public void updateSeekbar(){
        handler.postDelayed(mUpdateSeekbar,100);
    }

    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if (bounded) {
                seekBar.setMax(service.maxDurations());
                position = service.currentPositions();
                seekBar.setProgress(position);
                currentTime.setText(getTimeString(service.currentPositions()));

                totalTime.setText(getTimeString(service.maxDurations()-service.currentPositions()));
                handler.postDelayed(mUpdateSeekbar, 100);
            }
        }
    };

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     /*   switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        } */
        return super.onOptionsItemSelected(item);
    }




    public void onBackPressed(){
        super.onBackPressed();


        if (activity.equals("main")){

            Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if (activity.equals("trend")){

            Intent intent = new Intent(PlayerActivity.this, TrendActivity.class);
            startActivity(intent);
        }

        if (activity.equals("myplaylist")){

            Intent intent = new Intent(PlayerActivity.this, TrackplaylistActivity.class);
            startActivity(intent);

        }
        if (activity.equals("like")){

            Intent intent = new Intent(PlayerActivity.this, LikeActivity.class);
            startActivity(intent);

        }
        if (activity.equals("pubplaylist")){

            Intent intent = new Intent(PlayerActivity.this, PublickTrackPlaylistActivity.class);
            startActivity(intent);

        }
        if (activity.equals("tracksearch")){

            Intent intent = new Intent(PlayerActivity.this, TrackSearchActivity.class);
            startActivity(intent);

        }

        if (activity.equals("search")){

            Intent intent = new Intent(PlayerActivity.this, SearchTrackActivity.class);
            startActivity(intent);

        }

        if(activity.equals("offline")){

            Intent intent = new Intent(PlayerActivity.this, OfflineActivity.class);
            startActivity(intent);


        }

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        audioWidget.hide();

        finish();

    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long minutes = ( millis % (1000*60*60) ) / (1000*60);
        long seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    public void downloadTrack(){

        String mediaUrl = BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_MUSICF + trackListArraySend.get(selected).getFile();
        String url_track = mediaUrl.replaceAll(" ", "%20");
        String title = trackListArraySend.get(selected).getTitle();

        dialogz = new ProgressDialog(PlayerActivity.this);
        dialogz.setMessage("Track: " +title+" download...");
        dialogz.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        dialogz.show();


        Ion.with(PlayerActivity.this)
                .load(url_track)

                .progressBar(progressBar)

                .progressDialog(dialogz)

                .progress(new ProgressCallback() {

                    @Override
                    public void onProgress(long downloaded, long total) {


                    }

                })
                .write(new File(Environment.getExternalStorageDirectory() +
                        File.separator + SDCARD_FOLDER_Z + artistName + ".mp3"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {

                        dialogz.dismiss();
                    }
                });


    }

    public void coverImage(String track){


        try
        {

            names = URLEncoder.encode(track, "UTF-8");

        }
        catch (UnsupportedEncodingException e)
        {

        }

        String url = "https://api.spotify.com/v1/search?q="+ track +"&type=artist";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONObject trackObject = response.getJSONObject("artists");
                            JSONArray itemarray = trackObject.getJSONArray("items");

                            if (itemarray.length() == 0){

                                imagez = DEF_IMAGE;
                                imageset(imagez);

                            } else {

                                JSONObject itrms = itemarray.getJSONObject(0);
                                JSONArray images = itrms.getJSONArray("images");
                                JSONObject imarr = images.getJSONObject(0);
                                String urlcover = imarr.getString("url");

                                imageset(urlcover);
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

    }

    public void imageset(String urli){

        Glide.with(this)
                .load(urli)
                .placeholder(R.drawable.defim_black)
                .animate( R.anim.slide_in_left)
                .into(coverArtisiImage);

    }

    public void menuClick(View view){

        if (activity.equals("offline")){

        } else {

            ArrayAdapter<PopMenuOptions> adapter = new PopMenuAdapter(this, loadDonationOptions());
            new LovelyChoiceDialog(this)
                    .setTopColorRes(R.color.colorGreen)
                    .setTitle("Track")
                    .setIcon(R.drawable.defim_min)
                    .setMessage(artistName)
                    .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<PopMenuOptions>() {
                        @Override
                        public void onItemSelected(int menuSelected, PopMenuOptions item) {

                            if (menuSelected == 0) {


                                likeTHisTrack();

                            }
                            if (menuSelected == 1) {


                                addTrackToPlaylist();

                            }
                            if (menuSelected == 2) {

                                sharez();


                            }

                            if (menuSelected == 3) {

                                downloadTrack();
                                audioWidget.hide();

                            }
                        }
                    })
                    .show();
        }


    }

    private void  loadPlaylistOptions() {


        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_USER_PLIST_LIST;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("user_id", user_id);
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
                                String name = trackz.getString("name");
                                String  id = trackz.getString("id");

                                resultPop.add(new PlaylistOptions(id,name));

                            }

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

    private List<PopMenuOptions> loadDonationOptions() {
        List<PopMenuOptions> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.donations);
        for (String op : raw) {
            String[] info = op.split("%");
            result.add(new PopMenuOptions(info[1], info[0]));
        }
        return result;
    }


    public void likeTHisTrack(){


        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_USER_TRACK_SAVE;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("user_id", user_id);
            params.put("track_id",trackId );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            int like = response.getInt("error");

                            int color = Color.parseColor("#18d018");
                            likeButton.setColorFilter(color);

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

    public void addTrackToPlaylist(){

        PlaylistAdapter adapter = new PlaylistAdapter(this, resultPop);

        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorGreen)
                .setTitle("Your")
                .setIcon(R.drawable.defim_min)
                .setMessage("Playlists")
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<PlaylistOptions>() {
                    @Override
                    public void onItemSelected(int position_add, PlaylistOptions item) {


                        String playlistId = resultPop.get(position_add).getPlaylistId();
                        processAddTrackInPlaylist (trackId, playlistId);



                    }
                })
                .show();

    }

    public void processAddTrackInPlaylist(String trackId, String playlistId) {

        String request = BASE_BACKEND_URL + ENDPOINT_ADD_TRACK_IN_PLIST;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("playlist_id", playlistId);
            params.put("track_id", trackId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("RESP", "Error: " + error.getMessage());
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


    ////// Check like

    public void checkLike(){

        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String user_id = sPref.getString(APP_PREFERENCES_ID,"");

        String request = BASE_BACKEND_URL + ENDPOINT_CHECK_LIKE;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("user_id", user_id);
            params.put("track_id",trackId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                           int like = response.getInt("error");

                            if (like == 1){   //no like


                                int color = Color.parseColor("#1872f5");
                                likeButton.setColorFilter(color);


                            } else {    ///like

                                int color = Color.parseColor("#18d018");
                                likeButton.setColorFilter(color);


                            }

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

    ///// timer, trend  and ads

    public void startTrend(){



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                processTrend();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        requestNewInterstitial();

                    }
                });

                requestNewInterstitial();
            }
        }, 5000);
    }

    public void processTrend(){



        String request = BASE_BACKEND_URL + ENDPOINT_POST_TREND;

        JSONObject params = new JSONObject();
        try {

            params.put("X-API-KEY", API_KEY);
            params.put("id",trackId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                request, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            int like = response.getInt("error");



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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
        if (mInterstitialAd.isLoaded()) {

            if (SHOW_AD){
                mInterstitialAd.show();
            }

        } else {
          //  beginPlayingGame();
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        imageView.buildDrawingCache();
        Bitmap bm = imageView.getDrawingCache();

        OutputStream fOut = null;

        try {
            File root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "folder_name" + File.separator);
            root.mkdirs();
            File imageFile = new File(root, "spotimusic.jpg");
            outputFileUri = Uri.fromFile(imageFile);
            fOut = new FileOutputStream(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return outputFileUri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sharez(){

        Uri bmpUri = getLocalBitmapUri(coverArtisiImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I listen now : " + trackListArraySend.get(selected).getTitle() + " in application Spotimusic . Download this: https://appteve.com");
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }


    }


}

