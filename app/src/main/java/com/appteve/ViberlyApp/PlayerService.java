package com.appteve.ViberlyApp;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.appteve.ViberlyApp.constant.Constant;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eliza on 25/9/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,Constant {

    MediaPlayer mediaPlayer = null;
    final IBinder mBinder = new PlayerBinder();
    public static final String INTENT_BASE_NAME = "com.appteve.spotimusic.PlayerService";
    public static final String ACTION_PLAY_COMPLETED = INTENT_BASE_NAME + ".ACTION_PLAY_COMPLETED";
    public static final String ACTION_PLAY_CHANGE = "PLAY_CHANGE";
    public static final String ACTION_PLAY_START = "PLAY_START";
    private ArrayList<AudioItem> songArray = new ArrayList<AudioItem>();

    int durt = 0;
    int currentSelected;
    String currentActivity;
    String track_url_start;
    String trackID;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

        if (mp == mediaPlayer) {
            mediaPlayer.start();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();

        songArray = (ArrayList<AudioItem>)bundle.getSerializable("SONGS");
        currentSelected = intent.getIntExtra("SELECTED",0);
        currentActivity = intent.getStringExtra("ACTIVITY");




        String urlTrack =  BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_MUSICF + songArray.get(currentSelected ).getFile();
        track_url_start = urlTrack.replaceAll(" ", "%20");

        if (currentActivity.equals("offline")){

            track_url_start = songArray.get(currentSelected).getFile();

        }

        Intent intents = new Intent();
        intents.setAction(ACTION_PLAY_START);
        intents.putExtra("SELECT",currentSelected);
        sendBroadcast(intents);





        mediaPlayer= new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(track_url_start);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        durt = mediaPlayer.getDuration();
                    }
                });



            } catch (Exception e) {}
        }



        return Service.START_NOT_STICKY;
    }

    public void changeTrack(int position){

        currentSelected = position;


        if (currentActivity.equals("main")){

            Random r = new Random();
            int i1 = r.nextInt(songArray.size());
            currentSelected = i1 ;



        }

        if (currentActivity.equals("offline")){

            track_url_start = songArray.get(currentSelected).getFile();


        } else {

            String urlTrack =  BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_MUSICF + songArray.get(currentSelected ).getFile();
            track_url_start = urlTrack.replaceAll(" ", "%20");
        }


        Intent intent = new Intent();
        intent.setAction(ACTION_PLAY_CHANGE);
        intent.putExtra("SELECT",position );
        intent.putExtra("ARTIST", songArray.get(currentSelected).getTitle());
        intent.putExtra("TRACKID",songArray.get(currentSelected).getTrackId());
        sendBroadcast(intent);







        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(track_url_start);
                mediaPlayer.prepareAsync();


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        durt = mediaPlayer.getDuration();
                    }
                });



            } catch (Exception e) {}
        }
    }

    public boolean mPausePlay(int position) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return false;
        } else {
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
            return true;
        }
    }



    public void seekPosition(int position) {
        mediaPlayer.seekTo(position);
        mediaPlayer.start();
    }

    public int maxDurations(){


        return durt;
    }

    public int currentPositions(){


        return  mediaPlayer.getCurrentPosition();
    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class PlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
       // release();

        if (currentSelected == songArray.size() - 1){

            currentSelected = 0;
        } else {

            currentSelected = currentSelected + 1;
        }

        if (currentActivity.equals("main")){

            Random r = new Random();
            int i1 = r.nextInt(songArray.size());
            currentSelected = i1;

        }


        if (currentActivity.equals("offline")){

            track_url_start = songArray.get(currentSelected).getFile();


        } else {

            String urlTrack =  BASE_AMAZON_ENDPOINT + BASE_BACKEND_AMAZON_MUSICF + songArray.get(currentSelected ).getFile();
            track_url_start = urlTrack.replaceAll(" ", "%20");
        }






        Intent intent = new Intent();
        intent.setAction(ACTION_PLAY_COMPLETED);

        intent.putExtra("SELECT",currentSelected );
        intent.putExtra("ARTIST", songArray.get(currentSelected).getTitle());
        intent.putExtra("TRACKID",songArray.get(currentSelected).getTrackId());

        sendBroadcast(intent);




        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(track_url_start);
                mediaPlayer.prepareAsync();


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        durt = mediaPlayer.getDuration();
                    }
                });



            } catch (Exception e) {}
        }


    }

    private void release() {
        if (this.mediaPlayer == null) {
            return;
        }

        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }

        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
}
