package com.appteve.ViberlyApp.model;

/**
 * Created by appteve on 14/08/16.
 */
public class RadioItem {

    private String idStyle;
    private String urlImage;
    private String nameStyle;

    public RadioItem (String idStyle, String urlImage, String nameStyle){
        super();
        this.idStyle = idStyle;
        this.urlImage = urlImage;
        this.nameStyle = nameStyle;

    }

    public  String getIdStyle (){
        return  idStyle;
    }
    public void setIdStyle(String idStyle){
        this.idStyle = idStyle;
    }
    public String getUrlImage(){
        return  urlImage;
    }
    public  void  setUrlImage(String urlImage){
        this.urlImage = urlImage;
    }
    public String getNameStyle(){
        return  nameStyle;
    }
    public void setNameStyle(String nameStyle){
        this.nameStyle = nameStyle;
    }
}
