package com.example.jarek.mymapapp;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jarek on 12.01.2017.
 */

public class FindPlaceImpl implements MVP_Main_Interface.FindPlace {
    private static final String MAP_API =
    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=50000&keyword=%s&language=pl&key=AIzaSyASU1JCgaD2YfNDoS9pbIVv9iDlyBMg4sk";
    //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=5000&keyword=%s&language=pl&key=AIzaSyCkmvBBwr4VKAYyc3Ts72AcJZwy4Qti_Zk";
    //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=5000&keyword=%s&language=pl&key=AIzaSyBY5Y7nut3caPJIkAembksAOg7DWHsmMr4";

    private static final String LOCATION_PATTERN = "%f,%f";
    @Override
    public void getPlaceData(final CharSequence charSequence, final PlaceLocation currentLoc, final MVP_Main_Interface.FindPlaceResult findPlaceResult) {

        Thread thread = new Thread(new Runnable() {
            public Gson gson;

            @Override
            public void run() {
                URL url = null;
                OkHttpClient client = new OkHttpClient();
                //PlaceE placeResult = new PlaceE();
                try {

                    url = new URL(String.format(Locale.US, MAP_API,currentLoc.lat,currentLoc.lng,charSequence.toString()));

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("myLog", "Req to api: " + request.toString());

                    GsonBuilder gb = new GsonBuilder( );
                    gb.setFieldNamingPolicy( FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES );
                    this.gson = gb.create( );

                    PlacesResult res = this.gson.fromJson( response.body().string() , PlacesResult.class );
                    findPlaceResult.onPlacesResultDataReady(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
