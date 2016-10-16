package com.appteve.ViberlyApp.constant;

/**
 * Created by appteve on 08/08/16.
 */
public interface Constant {



    String BASE_BACKEND_URL                 = "http://spotimusic.appteve.info/";                      // URL YOUR BACKEND
    String DEF_TRACK_SEARCH                 = "dj";                                                // default search query text
    String BASE_BACKEND_AMAZON_BUKET        = "imusicfiles";                                       // AMAZON S3 BkCET
    String DEF_IMAGE                        = "http://www.50thirdand3rd.com/wp-content/uploads/2013/12/vinyl-record-ipad-wallpaper.jpg";   // default image
    String AD_INTERSTITIAL_ID               = "ca-app-pub-3940256099942544/1033173712";
    String AD_BANNER_ID                     = "ca-app-pub-3940256099942544~3347511713";

    boolean SHOW_AD                         = false;




    ///////////////////////////////////////
    ///                                 ///
    ///     Never not change it         //////////////////////////////////////////////////////////////////////////
    ///                                 ///
    ///////////////////////////////////////

    String BASE_AMAZON_ENDPOINT             =     "https://" + BASE_BACKEND_AMAZON_BUKET + ".s3.amazonaws.com/";
    String BASE_BACKEND_AMAZON_MUSICF       =     "music/";
    String BASE_BACKEND_AMAZON_SIMAGEF      =     "image/";
    String BASE_BACKEND_AMAZON_ALBUM        =     "album/";
    String BASE_BACKEND_AMAZON_ARTIST       =     "artist/";

    //// user settings

    String APP_PREFERENCES = "appuser";
    String APP_PREFERENCES_NAME = "Nickname";
    String APP_PREFERENCES_EMAIL = "Email";
    String APP_PREFERENCES_PHOTO = "Photo";
    String APP_PREFERENCES_ID = "user_id";
    //// api endpoint

    String API_KEY                       =     "testkey";
    String ENDPOINT_USER_LOGIN           =     "endpoint/appusers/login/";
    String ENDPOINT_IMAGE_UPLOAD         =     "endpoint/image/upload/";
    String REQUEST_SUCCESS               =     "1";
    String ENDPOINT_USER_REGISTER        =     "endpoint/appusers/add/";
    String ENDPOINT_USER_INFO            =     "endpoint/appusers/userinfo/";
    String X_API_KEY                     =     "?X-API-KEY=";
    String UPLOADS_FOLDER                =     "uploads/";
    String ENDPOINT_POST_TREND           =     "endpoint/track/trendcount";
    String ENDPOINT_TREND                =     "endpoint/track/trend/";
    String ENDPOINT_ALL_STYLE            =     "endpoint/style/all/";
    String ENDPOINT_ALL_TRACK_BY_STYLE   =     "endpoint/track/alltrackbystyle/?id=";
    String ENDPOINT_ALL_USER_PLIST       =     "endpoint/playlist/alluserplaylist/";
    String ENDPOINT_PLIST_ADD            =     "endpoint/playlist/add/";
    String ENDPOINT_PLIST_DELETE         =     "endpoint/playlist/delete/";
    String ENDPOINT_USER_TRACK_SAVE      =     "endpoint/usertrack/save/";
    String ENDPOINT_USER_PLIST_LIST      =     "endpoint/playlist/alluserplaylist/";
    String ENDPOINT_ADD_TRACK_IN_PLIST   =     "endpoint/playtrack/add/";
    String ENDPOINT_TRAK_IN_PLIST        =     "endpoint/playtrack/alltrack/?id=";
    String ENDPOINT_SET_PRIVATE_PL       =     "endpoint/playlist/setprivate/";
    String ENDPOINT_DELETE_TRACK_IN_PL   =     "endpoint/playtrack/delete/";
    String ENDPOINT_USER_LIKE            =     "endpoint/usertrack/allusertrack/?id=";
    String ENDPOINT_TRACK_DISLIKE_ONE    =     "endpoint/usertrack/delete/?id_track=";
    String ENDPOINT_GET_PUBLIC_PL        =     "endpoint/playlist/allpublic/";
    String ENDPOINT_SEARCH_TRACK         =     "endpoint/track/search/";

    String ENDPOINT_SEARCH_ARTIST        =     "endpoint/artist/search/";
    String ENDPOINT_SEARCH_ALBUM         =     "endpoint/album/search/";
    String EBDPOINT_SEARCH_STYLE         =     "endpoint/style/search/";

    String ENDPOINT_USER_UPDATE          =     "endpoint/appusers/update/";
    String ENDPOINT_FORGOT_PASS          =     "endpoint/appusers/reset/?email=";
    String ENDPOINT_CHECK_LIKE           =     "endpoint/usertrack/checklike/";

    String TAG = "TAG";
    String SDCARD_FOLDER                 = "/Spotimusic/";
    String SDCARD_FOLDER_W               = "Spotimusic";
    String SDCARD_FOLDER_Z               = "Spotimusic/";








}
