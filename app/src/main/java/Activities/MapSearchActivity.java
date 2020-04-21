package Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Models.Constants.DataConstants;
import Models.POJOS.MapPlace;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private MapPlace place;
    private Boolean view, isGPSActivated, isNetworkActivated;
    private Location currentLocation;
    private AutocompleteSupportFragment autocompleteFragment;
    private Switch switchAutoLocalization;
    private int markersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        instantiateElements();
        setSearchViewInputListener();
        addCheckButtonListener();
        addSwitchListener();
        getLocation();
    }

    private void instantiateElements() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSearchViewFragment);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        view = true;
        isGPSActivated = false;
        isNetworkActivated = false;
        place = null;
        currentLocation = null;
    }

    private void addSwitchListener() {
        Switch switchButtonView = findViewById(R.id.switchButtonView);

        switchButtonView.setOnCheckedChangeListener((view2, isChecked) -> {
            if (isChecked) {
                view = true;
                changeView();
            } else {
                view = false;
                changeView();
            }
        });

        switchAutoLocalization = findViewById(R.id.autoLocalizationSwitch);

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

    public String getCurrentLocationName(double longitude, double latitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getAddressLine(0);
    }

    public void populatePins(LatLng selectedPlace) {
        this.googleMap.clear();

        this.googleMap.addMarker(new MarkerOptions()
                .position(selectedPlace)
        );

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlace, 16));
        markersCount = 1;
    }

    private void changeView() {
        if (this.view) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void addCheckButtonListener() {
        ImageButton checkButton = findViewById(R.id.checkButton);

        checkButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();

            if (place != null && markersCount == 1) {
                returnIntent.putExtra("selectedPlace", place.getPlaceName());
                returnIntent.putExtra("longitude", place.getLongitude());
                returnIntent.putExtra("latitude", place.getLatitude());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(this, "Debe indicar una ubicación en el mapa", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSearchViewInputListener() {
        assert autocompleteFragment != null;
        Objects.requireNonNull(autocompleteFragment.getView()).setBackgroundColor(Color.WHITE);
        autocompleteFragment.setHint("Lugar del incidente...");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("CR");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                MapSearchActivity.this.place = new MapPlace(place.getName(), String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
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
}
