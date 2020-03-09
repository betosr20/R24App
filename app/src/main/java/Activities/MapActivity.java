package Activities;

import android.os.Bundle;

import com.example.r24app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.suke.widget.SwitchButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Models.Constants.FirebaseClasses;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap mMap;
    Marker lastClicked;
    List<Marker> markerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setear las actividades del boton te toggle de pines

        com.suke.widget.SwitchButton switchButtonPins;
        switchButtonPins = (com.suke.widget.SwitchButton)
                findViewById(R.id.switchButtonPins);

        switchButtonPins.setChecked(true);

        switchButtonPins.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    populatePins(mMap);
                }else{
                    clearPins(mMap);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        populatePins(googleMap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    public void populatePins(GoogleMap googleMap){
        mMap = googleMap;
        LatLng mamiHouse = new LatLng(9.917467, -84.022291);
        LatLng myHouse = new LatLng(9.930363, -84.027100);
        Marker marker1, marker2;

        marker1 = mMap.addMarker(new MarkerOptions()
                .position(myHouse)
                .title("My house madafackas")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        marker1.setTag(0);
        markerList.add(marker1);

        marker2 = mMap.addMarker(new MarkerOptions()
                .position(mamiHouse)
                .title("Casa de mami")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        marker2.setTag(0);
        markerList.add(marker2);

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

    public void clearPins(GoogleMap googleMap){
        googleMap.clear();
    }
}
