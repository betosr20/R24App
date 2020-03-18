package Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.r24app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    Marker lastClicked;
    List<Marker> markerList = new ArrayList<>();

    //To check which to delete if marker or heat
    Boolean activeMarker = true;
    Boolean activeHeatMap = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setear las actividades del boton te toggle de pines

        com.suke.widget.SwitchButton switchButtonPins;
        com.suke.widget.SwitchButton switchButtonHeatMap;

        switchButtonPins = (com.suke.widget.SwitchButton)
                findViewById(R.id.switchButtonPins);
        switchButtonHeatMap = (com.suke.widget.SwitchButton)
                findViewById(R.id.switchButtonHeat);

       switchButtonPins.setChecked(true);
       switchButtonHeatMap.setChecked(true);
        //SE COMENTA PORQUE FALTA PARA EL SPRINT
       /*
        switchButtonPins.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    activeMarker = true;
                    populatePins(mMap);

                }else{
                    clearPins(mMap, false, activeHeatMap);
                }
            }
        });

        switchButtonHeatMap.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    activeHeatMap = true;
                    addHeatMap(mMap);

                }else{
                    activeHeatMap = false;
                    clearHeatMap(mMap);
                }
            }
        });
        */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(MapActivity.this);


    }
    @Override
    public void onMapReady( final GoogleMap googleMap) {
        mMap = googleMap;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(FirebaseClasses.Report);
        System.out.println("ESTOY EN EL ON MAP READY");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Report report;
                    Marker marker;
                    LatLng latLng;
                    double latitude, longitude;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                        report = snapshot.getValue(Report.class);

                        System.out.println(report.getType());

                        latitude = Double.parseDouble(report.getLatitude());
                        longitude = Double.parseDouble(report.getLongitude());
                        latLng = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(report.getType())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        marker.setTag(0);
                        markerList.add(marker);

                    }
                    populatePins(googleMap);
                    addHeatMap(googleMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    public void populatePins(GoogleMap googleMap){
        mMap = googleMap;
        LatLng myHouse = new LatLng(9.930363, -84.027100);
        Marker marker1, marker2;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myHouse, 13));

        for (Marker m: markerList) {
            LatLng latLng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getBaseContext(),"Se ha seleccionado el marcador "+marker.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clearPins(GoogleMap googleMap, Boolean activeMarker, Boolean activeHeatMap) {
        if (!activeMarker) {
            googleMap.clear();
            this.activeMarker = false;
            if(this.activeHeatMap) {
                addHeatMap(googleMap);
            }
        }
    }

    private void addHeatMap(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> list = new ArrayList<>();

        LatLng rest1 = new LatLng(9.925602, -84.041371);
        list.add(rest1);
        LatLng rest2 = new LatLng(9.925898, -84.041281);
        list.add(rest2);
        LatLng rest3 = new LatLng(9.925834, -84.041007);
        list.add(rest3);
        LatLng rest4 = new LatLng(9.926180, -84.041536);
        list.add(rest4);
        LatLng rest5 = new LatLng(9.926051, -84.040785);
        list.add(rest5);
        LatLng rest6 = new LatLng(9.926364, -84.040937);
        list.add(rest6);


        HeatmapTileProvider mProvider;
        TileOverlay mOverlay;

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();

        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rest1, 13));
    }

    public void clearHeatMap(GoogleMap googleMap) {
        if (!activeHeatMap) {
            googleMap.clear();
            if(this.activeMarker) {
                populatePins(googleMap);
            }
        }else{
            addHeatMap(googleMap);
        }
    }
}
