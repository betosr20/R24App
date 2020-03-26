package Activities.ReportDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.r24app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;

/**
 * A simple {@link Fragment} subclass.
 */
public class location extends Fragment implements OnMapReadyCallback{
    View viewLocation;//Fragmento
    GoogleMap gMap;
    MapView mapView;
    public location() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewLocation = inflater.inflate(R.layout.fragment_location, container, false);
        //Aqui se deberia de extraer la longitud y la latitud del objeto Report para usuarlos en el mapa

        return viewLocation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = viewLocation.findViewById(R.id.mapEventDetail);
        if(mapView != null) {
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
        LatLng selectedPlace = new LatLng(9.953779385201116, -84.08405337512151);
        gMap.addMarker(new MarkerOptions().position(selectedPlace));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.953779385201116, -84.08405337512151), 16));
    }
}
