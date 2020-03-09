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

import Models.Constants.FirebaseClasses;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Obtiene el SupportMapFragment y es notificado cuando el mapa esta listo para ser usado llamando al metodo OnMapReady
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

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
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        populatePins(googleMap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        return false;
    }

    public void populatePins(GoogleMap googleMap){
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mamiHouse = new LatLng(9.917467, -84.022291);
        LatLng myHouse = new LatLng(9.930363, -84.027100);
        mMap.addMarker(new MarkerOptions()
                .position(myHouse)
                .title("My house madafackas")
        );
        mMap.addMarker(new MarkerOptions()
                .position(mamiHouse)
                .title("Casa de mami")
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myHouse, 13));
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
