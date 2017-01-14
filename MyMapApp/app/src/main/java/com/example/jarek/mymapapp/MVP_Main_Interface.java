package com.example.jarek.mymapapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jarek on 11.01.2017.
 */

public interface MVP_Main_Interface {

    interface ModelOperations {
        void downloadPlaceData(CharSequence charSequence);
    }

    /**
     * Required View methods available to MVP_Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     *      MVP_Presenter to View
     */
    interface ViewOps {

        Resources getViewResources();
        void setupMap(GoogleMap map);
        void printMarkerAndMoveCamera(LatLng latLng, String text);

        SupportMapFragment getSupportMapFragment();


        Context getContext();

        void requestGpsPermission();

        void hideMap();
        void showMap();

        void hideProgressBar();
        void showProgressBar();

        void hideListView();

        void showListView();

        void hideSearchBar();

        void showSearchBar();

        void showPlaceList(PlacesResult place);
        void showMsg(String s);
    }

    /**
     * Operations offered to View to communicate with MVP_Presenter.
     * Process user interaction, sends data requests to MVP_Model, etc.
     *      View to MVP_Presenter
     */
    interface PresenterOps {
        void onStartup(Bundle savedInstanceState);

        void permissionGranted();

        void onSearchConfirmed(CharSequence charSequence);

        void onSearchStateChanged(boolean b);

        void onPlaceSelected(int pos);
    }



    /**
     * Operations offered to MVP_Model to communicate with MVP_Presenter
     * Handles all data business logic.
     *      MVP_Presenter to MVP_Model
     */
    interface PresenterToModel {
        void prepareMapData(MVP_Model.ModelMapResult callBack);

        void startGps(MVP_Model.ModelLocationResult result);


        void downloadPlaceData(CharSequence charSequence, PlaceLocation currentLoc, MVP_Model.ModelPlacesResult result);
    }

    interface MapManager {
        void setupMap(MVP_Main_Interface.MapManagerResult result);
    }
    interface MapManagerResult {
        void  onMapReady(GoogleMap map);

    }


    interface LocationManager {
        void initGps(MVP_Main_Interface.LocationManagerResult result);
    }
    interface LocationManagerResult {
        void onLocationChange(PlaceLocation location);
        void onLocationSetupFailed();
    }

    interface FindPlace {

        void getPlaceData(CharSequence charSequence, PlaceLocation currentLoc, FindPlaceResult findPlaceResult);
    }

    interface FindPlaceResult {
        void onPlacesResultDataReady(PlacesResult result);
    }
}
