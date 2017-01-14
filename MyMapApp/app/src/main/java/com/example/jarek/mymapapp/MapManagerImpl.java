package com.example.jarek.mymapapp;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by jarek on 12.01.2017.
 */

public class MapManagerImpl implements MVP_Main_Interface.MapManager{
    SupportMapFragment supportMapFragment;

    public MapManagerImpl(SupportMapFragment supportMapFragment) {
        this.supportMapFragment = supportMapFragment;
    }

    @Override
    public void setupMap(final MVP_Main_Interface.MapManagerResult callBack) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                callBack.onMapReady(googleMap);
            }
        });
    }
}
