package Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Objects;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Place place;
    private ImageButton checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSearchViewFragment);
        setSearchViewInputListener();
        addCheckButtonListener();
        place = null;
    }

    private void addCheckButtonListener() {
        checkButton = findViewById(R.id.checkButton);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("selectedPlace", String.valueOf(place.getName()));
                    returnIntent.putExtra("longitude", String.valueOf(place.getLatLng().latitude));
                    returnIntent.putExtra("latitude", String.valueOf(place.getLatLng().longitude));
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                finish();
            }
        });
    }

    private void setSearchViewInputListener() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        Objects.requireNonNull(autocompleteFragment.getView()).setBackgroundColor(Color.WHITE);
        autocompleteFragment.setHint("Lugar del incidente...");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("CR");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                populatePins(place);
            }

            @Override
            public void onError(Status status) {

            }
        });

        String apiKey = getString(R.string.google_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);
        mapFragment.getMapAsync(this);
    }

    public void populatePins(Place place) {
        this.place = place;
        LatLng selectedPlace = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

        this.googleMap.addMarker(new MarkerOptions()
                .position(selectedPlace)
        );

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlace, 16));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLngBounds CR = new LatLngBounds(new LatLng(9.9368345, -84.1077296), new LatLng(9.9368345, -84.1077296));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.9368345, -84.1077296), 16));
    }
}
