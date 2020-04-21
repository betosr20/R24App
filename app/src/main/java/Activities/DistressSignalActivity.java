package Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.r24app.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Models.Constants.DataConstants;
import Models.Constants.FirebaseClasses;
import Models.POJOS.MapPlace;
import Models.POJOS.User;
import Services.DistressSignalService;
import Services.UserService;


public class DistressSignalActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private MapPlace place;
    private Boolean view = true;
    private Boolean isGPSActivated, isNetworkActivated;
    private UserService userService = new UserService();
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Switch switchAutoLocalization;
    private Location currentLocation;
    private int markersCount;
    private AutocompleteSupportFragment autocompleteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distress_signal);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSearchViewFragmentDistress);
        //Inicializa el nuevo user
        String id = userService.getCurrentFirebaseUserId();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference = database.getReference(FirebaseClasses.User).child(id);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                setSearchViewInputListener();
                addReportButtonListener();
                addSwitchListener();
                place = null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        isGPSActivated = false;
        isNetworkActivated = false;
        getLocation();
    }

    private void addSwitchListener() {
        Switch switchButtonView = findViewById(R.id.switchButtonView);

        switchButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view2, boolean isChecked) {
                if (isChecked) {
                    view = true;
                    changeView();
                } else {
                    view = false;
                    changeView();
                }
            }
        });

        switchAutoLocalization = findViewById(R.id.autoLocalizationSwitch2);

        switchAutoLocalization.setOnCheckedChangeListener((view2, isChecked) -> {
            if (isChecked) {
                getLocation();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                googleMap.clear();
                LatLng defaultPosition = new LatLng(9.932231, -84.091373);
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 7));
                markersCount = 0;
            }
        });
    }

    private void changeView() {
        if (this.view) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void setSearchViewInputListener() {
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        Objects.requireNonNull(autocompleteFragment.getView()).setBackgroundColor(Color.WHITE);
        autocompleteFragment.setHint("Lugar del incidente...");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("CR");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                DistressSignalActivity.this.place = new MapPlace(place.getName(), String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
                switchAutoLocalization.setChecked(false);
                populatePins(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
            }

            @Override
            public void onError(Status status) {

            }
        });

        View clearButton = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button);
        clearButton.setOnClickListener(view -> {
            autocompleteFragment.setText("");
            markersCount = 0;
            this.googleMap.clear();
        });

        String apiKey = getString(R.string.google_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);
        mapFragment.getMapAsync(this);
    }

    public void populatePins(LatLng selectedPlace) {
        this.googleMap.clear();

        this.googleMap.addMarker(new MarkerOptions()
                .position(selectedPlace)
        );

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlace, 16));
        markersCount = 1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // LatLngBounds CR = new LatLngBounds(new LatLng(9.9368345, -84.1077296), new LatLng(9.9368345, -84.1077296));
        // this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.9368345, -84.1077296), 16));

        LatLng defaultPosition = new LatLng(9.932231, -84.091373);
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 7));
    }

    public void windowBack(View v) {
        onBackPressed();
    }

    private void addReportButtonListener() {
        Button submitButton = findViewById(R.id.btnSubmitDistress);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place != null && markersCount == 1) {
                    submitButton.setEnabled(false);
                    boolean isRegistered;
                    Models.POJOS.DistressSignal distressCall = new Models.POJOS.DistressSignal(user.getId(), user.getName(), user.getLastName(), place.getPlaceName(),
                            place.getLatitude(), place.getLongitude());

                    DistressSignalService distressService = new DistressSignalService();
                    isRegistered = distressService.createDistressReport(distressCall);
                    if (isRegistered) {
                        Toast.makeText(DistressSignalActivity.this, "La señal de alerta se ha creado correctamente", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startMapActivity();
                            }
                        }, 3500);

                    } else {
                        Toast.makeText(DistressSignalActivity.this, "Ha ocurrido un problema al registrar la señal de alerta", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (markersCount > 0) {
                        Toast.makeText(DistressSignalActivity.this, "Solo puede indicar una ubicación", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DistressSignalActivity.this, "Debe indicar una ubicación en el mapa", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }

    /* Reference link https://www.journaldev.com/13325/android-location-api-tracking-gps*/
    private void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            isGPSActivated = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // get GPS status
            isNetworkActivated = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // get network provider status

            if (isGPSActivated) { // if GPS Enabled get lat/long using GPS Services
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DataConstants.MIN_TIME_BW_UPDATES, DataConstants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentLocationName(double longitude, double latitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getAddressLine(0);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSActivated && isNetworkActivated && currentLocation != null) {
                    try {
                        String placeName = getCurrentLocationName(currentLocation.getLongitude(), currentLocation.getLatitude());
                        place = new MapPlace(placeName, String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));
                        autocompleteFragment.setText("");
                        populatePins(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    switchAutoLocalization.setChecked(false);
                    Toast.makeText(this, "Asegurate de tener conexión a internet y tu ubicación activada", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Acceso a ubicación negado, por favor habilite el acceso en configuraciones del dispositivo", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void startMapActivity() {
        Intent mapIntent = new Intent(DistressSignalActivity.this, MapActivity.class);
        startActivity(mapIntent);
        finish();
    }

}
