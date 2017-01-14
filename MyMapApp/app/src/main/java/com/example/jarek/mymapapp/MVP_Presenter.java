package com.example.jarek.mymapapp;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

/**
 * Created by jarek on 12.01.2017.
 */

public class MVP_Presenter implements MVP_Main_Interface.PresenterOps, MVP_Model.ModelMapResult, MVP_Model.ModelLocationResult, MVP_Model.ModelPlacesResult {
    private WeakReference<MVP_Main_Interface.ViewOps> mView;
    private MVP_Main_Interface.PresenterToModel mModel;
    private Handler handler;
    PlaceLocation currentLoc = new PlaceLocation(51.664324, 19.354708);
    PlacesResult placesResult;
    boolean isFirstMove = true;

    public MVP_Presenter(MVP_Main_Interface.ViewOps view) {
        mView = new WeakReference<>(view);
        handler = new Handler();
        mModel = new MVP_Model(new MapManagerImpl(getView().getSupportMapFragment()),
                new LocationManagerImpl(getView().getContext()),
                new FindPlaceImpl() );
    }


    private MVP_Main_Interface.ViewOps  getView() throws NullPointerException {
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    @Override
    public void onStartup(Bundle savedInstanceState) {
        getView().showSearchBar();
        getView().hideMap();
        getView().showProgressBar();
        getView().hideListView();
        mModel.prepareMapData(this);

    }

    @Override
    public void permissionGranted() {
        mModel.startGps(this);
    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        if(charSequence.length() <= 2) {
            getView().showMsg("Too short");
            return;
        }

        getView().hideMap();
        getView().showProgressBar();
        mModel.downloadPlaceData(charSequence, currentLoc, this);
    }

    @Override
    public void onSearchStateChanged(boolean b) {

    }

    @Override
    public void onPlaceSelected(int pos) {
        getView().hideListView();
        getView().showMap();
        getView().showSearchBar();

        getView().printMarkerAndMoveCamera(new LatLng(placesResult.asList().get(pos).getGeometry().getLocation().getLat(),
                        placesResult.asList().get(pos).getGeometry().getLocation().getLng()),
                placesResult.asList().get(pos).getName());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        getView().hideProgressBar();
        getView().showMap();
        getView().setupMap(map);
        mModel.startGps(this);

    }


    @Override
    public void onLocationChange(PlaceLocation location) {
        currentLoc = location;
        LatLng latLng = new LatLng(location.lat, location.lng);
        if (isFirstMove) {
            getView().printMarkerAndMoveCamera(latLng, "You");
            isFirstMove = false;
        }
    }

    @Override
    public void onLocationSetupFailed() {
        getView().requestGpsPermission();
    }

    @Override
    public void onPlacesResult(final PlacesResult result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getView().hideProgressBar();
                if(result.isOkay()) {
                    placesResult = result;
                    getView().hideSearchBar();
                    getView().showPlaceList(result);
                    getView().showListView();
                }
                else
                {
                    getView().showMap();
                    getView().showMsg("Error:" + result.getStatus());
                }
            }
        });


    }
}
