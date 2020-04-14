package Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

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

import Activities.ReportDetail.ReportDetailContainer;
import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Models.POJOS.User;
import Services.UserService;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private List<Report> reportList = new ArrayList<>();
    private UserService userService = new UserService();
    private User user;
    private Switch switchButtonPins, switchButtonHeatMap, switchButtonView;
    //To check which to delete if marker or heat
    Boolean activeMarker;
    Boolean activeHeatMap;
    Boolean view;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Setear el onclick del boton del PopUp
        Button buttonPopUpMenu;

        mAuth = FirebaseAuth.getInstance();
        switchButtonPins = findViewById(R.id.switchButtonPins);
        switchButtonHeatMap = findViewById(R.id.switchButtonHeat);
        switchButtonView = findViewById(R.id.switchButtonView);
        String id = userService.getCurrentFirebaseUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.User).child(id);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                activeMarker = user.isPicker();
                activeHeatMap = user.isHeatMap();
                view = user.isViewType();

                // Obtiene el SupportMapFragment y es notificado cuando el mapa esta listo para ser usado llamando al metodo OnMapReady
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map2);
                mapFragment.getMapAsync(MapActivity.this::onMapReady);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //Setear las actividades del boton te toggle de pines

        // Set a checked change listener for switch button
        switchButtonPins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activeMarker = true;
                    userService.updatePinSetting(true);
                    populatePins(mMap);
                } else {
                    activeMarker = false;
                    userService.updatePinSetting(false);
                    clearPins(mMap, false, activeHeatMap);
                }
            }
        });

        switchButtonHeatMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    activeHeatMap = true;
                    userService.updateheatMapSetting(true);
                    addHeatMap(mMap);
                } else {
                    clearPins(mMap, activeMarker, false);
                }
            }
        });

        switchButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view2, boolean isChecked) {
                if (isChecked) {
                    view = true;
                    userService.updateViewTypeSetting(true);
                    changeView();
                } else {
                    view = false;
                    userService.updateViewTypeSetting(false);
                    changeView();
                }
            }
        });
    }

    @Override

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        switchButtonPins.setChecked(activeMarker);
        switchButtonHeatMap.setChecked(activeHeatMap);
        switchButtonView.setChecked(view);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.Report);
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
                    if (activeMarker) {
                        populatePins(googleMap);
                    }

                    if (activeHeatMap) {
                        addHeatMap(googleMap);
                    }

                    if (!view) {
                        changeView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        LatLng latLng;
        latLng = new LatLng(9.932231, -84.091373);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        return false;
    }

    public void populatePins(GoogleMap googleMap) {
        mMap = googleMap;
        List<Marker> markerList = new ArrayList<>();
        double latitude, longitude;
        Marker marker;
        LatLng latLng;

        for (Report report : reportList) {
            latitude = Double.parseDouble(report.getLatitude());
            longitude = Double.parseDouble(report.getLongitude());
            latLng = new LatLng(latitude, longitude);
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(report.getType())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            marker.setTag(report);
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
                Report report = (Report) marker.getTag();
                OnDetailSelected(report);
            }
        });
    }

    public void OnDetailSelected(Report report) {
        Intent intent = new Intent(this, ReportDetailContainer.class);
        String id = report.getId();
        intent.putExtra("idReport", id);
        startActivity(intent);
    }

    public void clearPins(GoogleMap googleMap, Boolean activeMarker, Boolean activeHeatMap) {
        if (!activeMarker) {
            googleMap.clear();
            this.activeMarker = false;
            userService.updatePinSetting(false);
            if (this.activeHeatMap) {
                addHeatMap(googleMap);
            }
        }
        if (!activeHeatMap) {
            googleMap.clear();
            this.activeHeatMap = false;
            userService.updateheatMapSetting(false);
            if (this.activeMarker) {
                populatePins(googleMap);
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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
            m.setGroupDividerEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

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

    private void changeView() {
        if (this.view) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v) {
        MenuBuilder menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        Context context2 = this;
        // Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        signOut();
                        return true;
                    case R.id.map2:
                        Intent intent = new Intent(context2, MapActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.report:
                        Intent reportActivity = new Intent(context2, ReportIncidentActivity.class);
                        startActivity(reportActivity);
                        return true;
                    case R.id.myProfile:
                        Intent profileActivity = new Intent(context2, MyProfileActivity.class);
                        startActivity(profileActivity);
                        return true;
                    case R.id.action_search_user:
                        Intent searchUserIntent = new Intent(context2, SearchUser.class);
                        startActivity(searchUserIntent);
                        return true;
                    case R.id.myReports:
                        Intent myReportsIntent = new Intent(context2, MyReportsActivity.class);
                        startActivity(myReportsIntent);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        optionsMenu.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.exitAlertTittle)
                .setMessage(R.string.exitAlert)
                .setPositiveButton("Salir", (dialogInterface, i) -> signOut())
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.show();
    }
}