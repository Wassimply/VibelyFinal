package com.appteve.ViberlyApp;

import java.io.Serializable;


/**
 * Created by appteve on 10/08/16.
 */
public class AudioItem  implements Serializable {

    String file;
    String title;
    String trackId;
    String artist;
    String playCount;
    String album;
    String style;

    public AudioItem   (String file, String title, String artist, String trackId,  String playCount, String album, String style){

        super();
        this.file = file;
        this.title = title;
        this.artist = artist;
        this.trackId = trackId;
        this.playCount = playCount;
        this.style = style;
        this.album = album;


    }

    public AudioItem(){

    }


    public  String getFile(){
        return file;
    }

    public void setFile(String file){
        this.file = file;
    }



    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getArtist (){
        return artist;
    }

    public  void  setArtist(String artist){
        this.artist = artist;
    }

    public  String getTrackId(){
        return trackId;
    }
    public void setTrackId(String trackId){
        this.trackId = trackId;
    }


    public String getPlayCount(){
        return playCount;
    }
    public void setPlayCount(String playCount){
        this.playCount = playCount;
    }

    public String getAlbum(){
        return album;
    }
    public void setAlbum(String album){
        this.album = album;
    }

    public String getStyle(){
        return style;
    }
    public void setStyle(String style){
        this.style = style;
    }






}
