package com.example.jarek.mymapapp;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by jarek on 12.01.2017.
 */

public class MVP_Model implements MVP_Main_Interface.PresenterToModel {

    MVP_Main_Interface.MapManager mapManagerImpl;
    MVP_Main_Interface.LocationManager mLocationManagerImpl;
    MVP_Main_Interface.FindPlace mFindPlaceImpl;


    public MVP_Model(MVP_Main_Interface.MapManager mapManagerImpl, MVP_Main_Interface.LocationManager mLocationManagerImpl, MVP_Main_Interface.FindPlace mFindPlaceImpl) {
        this.mapManagerImpl = mapManagerImpl;
        this.mLocationManagerImpl= mLocationManagerImpl;
        this.mFindPlaceImpl = mFindPlaceImpl;
    }

    @Override
    public void prepareMapData(final ModelMapResult result) {
        mapManagerImpl.setupMap(new MVP_Main_Interface.MapManagerResult() {
            @Override
            public void onMapReady(GoogleMap map) {
                result.onMapReady(map);
            }
        });
    }

    @Override
    public void startGps(final ModelLocationResult result) {
        mLocationManagerImpl.initGps(new MVP_Main_Interface.LocationManagerResult() {
            @Override
            public void onLocationChange(PlaceLocation location) {
                result.onLocationChange(location);
            }

            @Override
            public void onLocationSetupFailed() {
                result.onLocationSetupFailed();
            }
        });
    }

    @Override
    public void downloadPlaceData(CharSequence charSequence, PlaceLocation currentLoc, final ModelPlacesResult results) {
        mFindPlaceImpl.getPlaceData(charSequence, currentLoc, new MVP_Main_Interface.FindPlaceResult(){

            @Override
            public void onPlacesResultDataReady(PlacesResult result) {
                results.onPlacesResult(result);
            }
        } );
    }

    public interface ModelMapResult {
        void onMapReady(GoogleMap map);
    }

    public interface ModelLocationResult {
        void onLocationChange(PlaceLocation location);
        void onLocationSetupFailed();
    }

    public interface ModelPlacesResult {

        void onPlacesResult(PlacesResult result);
    }

}
