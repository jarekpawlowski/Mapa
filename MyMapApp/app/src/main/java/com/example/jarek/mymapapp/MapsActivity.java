package com.example.jarek.mymapapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements MVP_Main_Interface.ViewOps, MaterialSearchBar.OnSearchActionListener {

    private MVP_Main_Interface.PresenterOps mPresenter;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    ProgressBar progressBar;
    ListView listView;
    Button searchButton;
    EditText editText;
    String searchString;

    private MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        setupMVP();

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        //addListenerOnButton();

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        //FadingCircle doubleBounce = new FadingCircle();
        //progressBar.setIndeterminateDrawable(doubleBounce);

        mPresenter.onStartup(savedInstanceState);
    }

    private void setupMVP() {

        MVP_Presenter presenter = new MVP_Presenter(this);
        mPresenter = presenter;

    }

    @Override
    public Resources getViewResources() {
        return null;
    }

    @Override
    public void setupMap(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void printMarkerAndMoveCamera(LatLng latLng, String text) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title(text));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        }
        else {
            throw new NullPointerException("mMap is unavailable");
        }
    }

    @Override
    public SupportMapFragment getSupportMapFragment() {
        return mapFragment;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void requestGpsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1
        );
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1
        );
    }

    @Override
    public void hideMap() {
        findViewById(R.id.map).setVisibility(View.GONE);
    }

    @Override
    public void showMap() {
        findViewById(R.id.map).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.progress_bar).setVisibility(progressBar.GONE);
    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.progress_bar).setVisibility(progressBar.VISIBLE);
    }

    @Override
    public void hideListView() {
        findViewById(R.id.listView).setVisibility(View.GONE);
    }

    @Override
    public void showListView() {
        findViewById(R.id.listView).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchBar() {
        findViewById(R.id.searchBar).setVisibility(View.GONE);
    }

    @Override
    public void showSearchBar() {
       findViewById(R.id.searchBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaceList(PlacesResult placesResult) {

        listView = (ListView) this.findViewById(R.id.listView);

        List<String> list = new ArrayList<String>();

        for ( Place place : placesResult ) {
            list.add(place.getName());
        }

        PlacesList adapter = new
                PlacesList(getContext(), list.toArray(new String[0]) );
        listView.setAdapter(adapter);

        showListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {
                mPresenter.onPlaceSelected(pos);
            }
        });
    }

    @Override
    public void showMsg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("...","onRequestPermissionsResult");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mPresenter.permissionGranted();
        }
    }
/*
    public void addListenerOnButton(){
        searchButton = (Button) findViewById(R.id.button1);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.searchView1);
                String g = editText.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 5);
                    if (addresses != null && !addresses.equals(""))
                        //search(addresses);
                        listAdresses(addresses);

                } catch (Exception e) {

                }
            }
        });
    }*/

    public void listAdresses(List<Address> addresses){
        //ArrayList<String> values = new ArrayList<>();
        //for(int i = 0; i < addresses.size(); i++){
        //    values.add(i, addresses.get(i).getSubLocality());
        //}

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_places, R.id.label, values);
        //setListAdapter(adapter);

        //setListAdapter(adapter);
        listView = (ListView) this.findViewById(R.id.listView);

        List<String> list = new ArrayList<String>();

        for ( int i = 0; i < addresses.size(); i++ ) {
            list.add(addresses.get(i).getSubLocality());
        }

        PlacesList adapter = new
                PlacesList(getContext(), list.toArray(new String[0]) );
        listView.setAdapter(adapter);



    }
/*
    public void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        home_long = address.getLongitude();
        home_lat = address.getLatitude();
        latLng = new LatLng(address.getLatitude(), address.getLongitude());

        addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationTv.setText("Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude());
    }*/

   @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        mPresenter.onSearchConfirmed(charSequence);
    }

    @Override
    public void onSearchStateChanged(boolean b) {
        mPresenter.onSearchStateChanged(b);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
    }
}
