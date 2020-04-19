package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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

import java.util.Arrays;
import java.util.Objects;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;
import Services.DistressSignalService;
import Services.UserService;


public class DistressSignalActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Place place;
    private Boolean view = true;
    private UserService userService = new UserService();
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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
                //addCheckButtonListener();
                addReportButtonListener();
                addSwitchListener();
                place = null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
    }

    private void changeView() {
        if (this.view) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    //private void addCheckButtonListener() {
      //  ImageButton checkButton = findViewById(R.id.checkButton);

        //checkButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
         //       if (place != null) {
         //           Intent returnIntent = new Intent();
          //          returnIntent.putExtra("selectedPlace", String.valueOf(place.getName()));
          //          returnIntent.putExtra("longitude", String.valueOf(place.getLatLng().longitude));
           //         returnIntent.putExtra("latitude", String.valueOf(place.getLatLng().latitude));
             //       setResult(Activity.RESULT_OK, returnIntent);
              //  }

          //      finish();
          //  }
       // });
   // }

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
           if (place != null) {
               submitButton.setEnabled(false);
               Toast.makeText(DistressSignalActivity.this, user.getId(), Toast.LENGTH_LONG).show();
            boolean isRegistered;
           Models.POJOS.DistressSignal distressCall = new Models.POJOS.DistressSignal(user.getId(), user.getName(), user.getLastName(), String.valueOf(place.getName()),
                   String.valueOf(place.getLatLng().latitude),   String.valueOf(place.getLatLng().longitude));

               DistressSignalService distressService = new DistressSignalService();
              isRegistered = distressService.createDistressReport(distressCall);
              if(isRegistered){
                  Toast.makeText(DistressSignalActivity.this, "La señal de alerta se ha creado correctamente", Toast.LENGTH_LONG).show();
                  finish();
              }else{
                  Toast.makeText(DistressSignalActivity.this, "Ha ocurrido un problema al registrar la señal de alerta", Toast.LENGTH_LONG).show();
              }

      }else{
               Toast.makeText(DistressSignalActivity.this, "Debe indicar una ubicación en el mapa", Toast.LENGTH_LONG).show();
           }
      }
     });
     }

}
