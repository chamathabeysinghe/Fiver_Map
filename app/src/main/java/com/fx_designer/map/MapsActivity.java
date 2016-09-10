package com.fx_designer.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.fx_designer.map.database.DatabaseController;
import com.fx_designer.map.models.Position;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,SearchFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ImageButton searchButton;
    private ImageButton menuButton;
    private ViewGroup hiddenPanel;
    private Button addPositionButton;
    private boolean isPanelShown = false;

    private Marker customMarker;

    private EditText nameTextField;
    private EditText addressTextField;
    private EditText phoneTextField;
    SearchFragment fragment;
    ArrayList<Marker> markerArrayList=new ArrayList<>();
    ArrayList<Position> positionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hiddenPanel= (ViewGroup) findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        isPanelShown=false;

        searchButton= (ImageButton) findViewById(R.id.search_place_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragment= new SearchFragment();

                fragmentTransaction.add(R.id.main_view,fragment);
                fragmentTransaction.addToBackStack("New").commit();
            }
        });

        menuButton= (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpDown();
                addCustomMarker();
            }
        });

        addPositionButton= (Button) findViewById(R.id.addPositionButton);
        addPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDB();
            }
        });


        nameTextField= (EditText) findViewById(R.id.editText);
        addressTextField= (EditText) findViewById(R.id.editText2);
        phoneTextField= (EditText) findViewById(R.id.editText3);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        customMarker=mMap.addMarker(new MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void slideUpDown() {
        if(!isPanelShown) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            Log.e("No animation","No animation");
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            isPanelShown = true;
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }
    }

    public void addCustomMarker(){
        if(customMarker!=null){
            customMarker.remove();
        }
        LatLng position=mMap.getCameraPosition().target;
       customMarker=mMap.addMarker(new MarkerOptions().position(position).draggable(true).title("Place on location"));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchSubmit(String keywords) {
        for(Marker m:markerArrayList){
            m.remove();
        }
        markerArrayList.clear();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        positionsList=DatabaseController.getInstance(this).readFromDatabase(keywords);
        Log.e("Array", ""+positionsList.size());
        boolean first=true;
        for(Position p:positionsList){

            double lat=p.getLat();
            double lon=p.getLongitude();
//            Log.e()
            Log.e("Current Location","Lat: "+lat+"  Longitude: "+lon);
            LatLng currentPosition = new LatLng(lat, lon);
            String title=p.getName()+" "+p.getAddress()+" "+p.getTelephoneNumber();
            Marker m=mMap.addMarker(new MarkerOptions().position(currentPosition).draggable(false).title(title));
//            Marker m=mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLat(),p.getLongitude())).title(p.getName()));
            m.setTag(markerArrayList.size());
            markerArrayList.add(m);
            if(first){
                  mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                first=false;

            }
        }
        Log.e("Search","Done the searching");
    }




    private void addToDB(){
        DatabaseController databaseController=DatabaseController.getInstance(this);
        String name=nameTextField.getText().toString();
        String address=addressTextField.getText().toString();
        String phone=phoneTextField.getText().toString();
        double lat=customMarker.getPosition().latitude;
        double longitude=customMarker.getPosition().longitude;
        databaseController.insertPosition(lat,longitude,name,address,phone);

        Toast.makeText(this,"Add to db "+customMarker.getPosition().toString(),Toast.LENGTH_SHORT).show();
    }
}
