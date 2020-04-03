package Activities.ReportDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.r24app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class location extends Fragment implements OnMapReadyCallback {
    View viewLocation;//Fragmento
    GoogleMap gMap;
    MapView mapView;
    String idReport;
    Report report;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public location() {
        // Required empty public constructor
    }

    public location(String idReport) {
        this.idReport = idReport;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(FirebaseClasses.Report).child(idReport);
        this.report = new Report();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewLocation = inflater.inflate(R.layout.fragment_location, container, false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Valida si existe el arreglo, osea si hay datos
                if (dataSnapshot.exists()) {
                    report = dataSnapshot.getValue(Report.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return viewLocation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = viewLocation.findViewById(R.id.mapEventDetail);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        gMap = googleMap;
        this.populatePins(googleMap);
    }

    //metodo que se encarga de pintar el ping del lugar del evento
    public void populatePins(GoogleMap googleMap) {
        gMap = googleMap;
        double latitude, longitude;
        latitude = Double.parseDouble(report.getLatitude());
        longitude = Double.parseDouble(report.getLongitude());
        LatLng selectedPlace = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(selectedPlace));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
    }
}
