package com.appteve.ViberlyApp.model;

/**
 * Created by appteve on 18/08/16.
 */
public class SearchItem {

     String idSearchItem;
     String nameSearchItem;
     String imageSearchItem;

    public SearchItem(String idSearchItem, String nameSearchItem, String imageSearchItem){
        super();
        this.idSearchItem = idSearchItem;
        this.nameSearchItem = nameSearchItem;
        this.imageSearchItem = imageSearchItem;
    }

    public String getidSearchItem(){
        return  idSearchItem;
    }
    public void setidSearchItem (String idSearchItem){
        this.idSearchItem = idSearchItem;
    }
    public String getnameSearchItem(){
        return nameSearchItem;
    }
    public void setNameSearchItem(String nameSearchItem){
        this.nameSearchItem = nameSearchItem;
    }
    public String getImageSearchItem(){
        return  imageSearchItem;
    }
    public void setImageSearchItem(String imageSearchItem){
        this.imageSearchItem = imageSearchItem;
    }

}
