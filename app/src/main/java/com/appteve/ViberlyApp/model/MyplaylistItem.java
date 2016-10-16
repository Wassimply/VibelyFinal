package com.appteve.ViberlyApp.model;

/**
 * Created by appteve on 15/08/16.
 */
public class MyplaylistItem {

    String idPlaylist;
    String namePlaylist;
    int isPrivate;

    public MyplaylistItem (String idPlaylist, String namePlaylist, int isPrivate){
        super();
        this.idPlaylist = idPlaylist;
        this.namePlaylist = namePlaylist;
        this.isPrivate = isPrivate;

    }

    public String getIdPlaylist(){
        return  idPlaylist;
    }
    public  void  setIdPlaylist(String idPlaylist){
        this.idPlaylist = idPlaylist;
    }
    public String getNamePlaylist(){
        return  namePlaylist;
    }
    public void setNamePlaylist(String namePlaylist){
        this.namePlaylist = namePlaylist;
    }
    public int getIsPrivate(){
        return isPrivate;
    }
    public void setIsPrivate(int isPrivate){
        this.isPrivate = isPrivate;
    }


}
