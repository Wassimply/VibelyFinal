package com.appteve.ViberlyApp.model;

/**
 * Created by appteve on 17/08/16.
 */
public class PublicPlaylistItem {

    public String id_playlist;
    public String namePlaylist;
    public String userOwener;

    public  PublicPlaylistItem(String id_playlist, String namePlaylist, String userOwener){
        super();
        this.id_playlist = id_playlist;
        this.namePlaylist = namePlaylist;
        this.userOwener = userOwener;

    }

    public String getId_playlist(){
        return id_playlist;
    }
    public void setId_playlist(String id_playlist){
        this.id_playlist = id_playlist;
    }
    public String getNamePlaylist(){
        return namePlaylist;
    }
    public void setNamePlaylist(String namePlaylist){
        this.namePlaylist = namePlaylist;
    }
    public String getUserOwener(){
        return userOwener;
    }
    public void setUserOwener(String userOwener){
        this.userOwener = userOwener;
    }


}
