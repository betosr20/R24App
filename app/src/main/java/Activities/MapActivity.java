package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.r24app.MainActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Report> reportList = new ArrayList<>();
    private FirebaseAuth mAuth;

    //To check which to delete if marker or heat
    Boolean activeMarker = true;
    Boolean activeHeatMap = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        //Setear las actividades del boton te toggle de pines

        Switch switchButtonPins;
        Switch switchButtonHeatMap;

        switchButtonPins = findViewById(R.id.switchButtonPins);
        switchButtonHeatMap = findViewById(R.id.switchButtonHeat);

        switchButtonPins.setChecked(true);
        switchButtonHeatMap.setChecked(true);

        // Set a checked change listener for switch button
        switchButtonPins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activeMarker = true;
                    populatePins(mMap);
                } else {
                    activeMarker = false;
                    clearPins(mMap, false, activeHeatMap);
                }
            }
        });

        switchButtonHeatMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    activeHeatMap = true;
                    addHeatMap(mMap);
                } else {
                    activeHeatMap = false;
                    clearHeatMap(mMap);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(FirebaseClasses.Report);

        //Agrega el Event Listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Valida si existe el arreglo, osea si hay datos
                if (dataSnapshot.exists()) {
                    //Itera el contenido del arreglo
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reportList.add(snapshot.getValue(Report.class));
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

    public void populatePins(GoogleMap googleMap) {
        mMap = googleMap;
        List<Marker> markerList = new ArrayList<>();
        double latitude, longitude;
        Marker marker;
        LatLng latLng;
        latLng = new LatLng(9.932231, -84.091373);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));

        for (Report report : reportList) {
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

        for (Marker m : markerList) {
            latLng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getBaseContext(), "Se ha seleccionado el marcador " + marker.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clearPins(GoogleMap googleMap, Boolean activeMarker, Boolean activeHeatMap) {
        if (!activeMarker) {
            googleMap.clear();
            this.activeMarker = false;
            if (this.activeHeatMap) {
                addHeatMap(googleMap);
            }
        }
    }

    private void addHeatMap(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> list = new ArrayList<>();
        HeatmapTileProvider mProvider;
        TileOverlay mOverlay;
        LatLng latLng;
        double latitude, longitude;

        for (Report report : reportList) {
            latitude = Double.parseDouble(report.getLatitude());
            longitude = Double.parseDouble(report.getLongitude());
            latLng = new LatLng(latitude, longitude);
            list.add(latLng);
        }

        if (!list.isEmpty()) {
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    public void clearHeatMap(GoogleMap googleMap) {
        if (!activeHeatMap) {
            googleMap.clear();
            if (this.activeMarker) {
                populatePins(googleMap);
            }
        } else {
            addHeatMap(googleMap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                signOut();
                return true;
            case R.id.map2:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;
            case R.id.report:
                Intent reportActivity = new Intent(this, ReportIncidentActivity.class);
                startActivity(reportActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
        Intent signOut = new Intent(this, MainActivity.class);
        signOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signOut);
        finish();
    }
}
