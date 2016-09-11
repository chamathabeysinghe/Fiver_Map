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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
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

    //google map object
    private GoogleMap mMap;

    //app bar buttons
    private ImageButton searchButton;
    private ImageButton menuButton;

    //component in add new location pannel
    private ViewGroup hiddenPanel;
    private EditText nameTextField;
    private EditText addressTextField;
    private EditText phoneTextField;
    private EditText categoryTextField;
    private EditText latTextField;
    private EditText longTextFiText;
    private Button addPositionButton;

    //variable for app
    private boolean isPanelShown = false;


    //fragment for searching
    private SearchFragment fragment;

    //markers and positions list
    private ArrayList<Marker> markerArrayList=new ArrayList<>();
    private ArrayList<Position> positionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //set the hidden add position pannel
        hiddenPanel= (ViewGroup) findViewById(R.id.hidden_panel);
        hiddenPanel.setVisibility(View.INVISIBLE);
        isPanelShown=false;

        //initialize search button
        searchButton= (ImageButton) findViewById(R.id.search_place_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAddLocationPanel();
                setToolBarInvisible();
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragment= new SearchFragment();

                fragmentTransaction.add(R.id.main_view,fragment);
                fragmentTransaction.addToBackStack("New").commit();


            }
        });

        //initialize add position panel show button
        menuButton= (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpDown();
            }
        });


        //initialize add position in hidden panel
        addPositionButton= (Button) findViewById(R.id.addPositionButton);
        addPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDB();
                slideUpDown();


            }
        });


        nameTextField= (EditText) findViewById(R.id.editText);
        addressTextField= (EditText) findViewById(R.id.editText2);
        phoneTextField= (EditText) findViewById(R.id.editText3);
        categoryTextField= (EditText) findViewById(R.id.editText5);
        latTextField= (EditText) findViewById(R.id.editText7);
        longTextFiText= (EditText) findViewById(R.id.editText6);

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

    //run animations for showing and hiding the add location panel
    private void slideUpDown() {
        hideKeyboard();
        if(!isPanelShown) {
            // Show the panel
            clearText();

            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            Log.e("No animation","No animation");
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            isPanelShown = true;
            menuButton.setImageResource(R.drawable.ic_close_black_24dp);

        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
            menuButton.setImageResource(R.drawable.ic_add_black_24dp);
        }
    }

    //hide the add location panel
    private void hideAddLocationPanel(){
        // Hide the Panel
        if(!isPanelShown)return;
        Animation bottomDown = AnimationUtils.loadAnimation(this,
                R.anim.bottom_down);

        hiddenPanel.startAnimation(bottomDown);
        hiddenPanel.setVisibility(View.INVISIBLE);
        isPanelShown = false;
        menuButton.setImageResource(R.drawable.ic_add_black_24dp);
    }



    //hiding the keyboard functionality
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override

    /**
     * called when fragment submit a search query
     * search database and add markers to selected location
     */
    public void onSearchSubmit(ArrayList<Position> positions) {
        setToolBarVisible();
        for(Marker m:markerArrayList){
            m.remove();
        }
        markerArrayList.clear();

        hideKeyboard();

        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        positionsList=positions;
        boolean first=true;
        for(Position p:positionsList){

            double lat=p.getLat();
            double lon=p.getLongitude();
            LatLng currentPosition = new LatLng(lat, lon);
            String title=p.getName()+" - "+p.getAddress()+" - "+p.getTelephoneNumber();
            Marker m=mMap.addMarker(new MarkerOptions().position(currentPosition).draggable(false).title(title));
            m.setTag(markerArrayList.size());
            markerArrayList.add(m);
            if(first){
                  mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                first=false;

            }
        }
    }



    //write to custom position to database
    private void addToDB(){
        try{
            DatabaseController databaseController=DatabaseController.getInstance(this);
            String name=nameTextField.getText().toString();
            String address=addressTextField.getText().toString();
            String phone=phoneTextField.getText().toString();
            String category=categoryTextField.getText().toString();
            double lat=Double.parseDouble(latTextField.getText().toString());
            double longitude=Double.parseDouble(longTextFiText.getText().toString());
            databaseController.insertPosition(lat,longitude,name,category,address,phone);

            Toast.makeText(this,"Add location to db ",Toast.LENGTH_SHORT).show();
        }
        catch (NumberFormatException e){
            Toast.makeText(this,"Invalid input ",Toast.LENGTH_SHORT).show();
        }


    }

    public void setToolBarVisible(){
        findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
    }
    public void setToolBarInvisible(){
        findViewById(R.id.app_bar).setVisibility(View.INVISIBLE);
    }

    private void clearText(){
        nameTextField.setText("");
        addressTextField.setText("");
        phoneTextField.setText("");
        latTextField.setText("");
        longTextFiText.setTag("");
        categoryTextField.setText("");
    }
}
