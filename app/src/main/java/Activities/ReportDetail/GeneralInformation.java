package Activities.ReportDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.r24app.R;
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
public class GeneralInformation extends Fragment {

//    private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
    private String idReport;
    private Report report;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    TextView type, place, affectedPeople, affectedAnimals, details, startDateTextView, pathDisabled;
    View viewGeneralInformation;

    public GeneralInformation(String idReport) {
        // Required empty public constructor
        this.idReport = idReport;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(FirebaseClasses.Report).child(idReport);
        this.report = new Report();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGeneralInformation = inflater.inflate(R.layout.fragment_general_infomation, container, false);
        type = viewGeneralInformation.findViewById(R.id.reportType);
        place = viewGeneralInformation.findViewById(R.id.reportPlace);
        affectedPeople = viewGeneralInformation.findViewById(R.id.reportAffectedPeople);
        affectedAnimals = viewGeneralInformation.findViewById(R.id.reportAffectedAnimals);
        details = viewGeneralInformation.findViewById(R.id.reportDetail);
        startDateTextView = viewGeneralInformation.findViewById(R.id.reportStartDate);
        pathDisabled = viewGeneralInformation.findViewById(R.id.reportPathDisabled);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Valida si existe el arreglo, osea si hay datos
                if (dataSnapshot.exists()) {
                    report = dataSnapshot.getValue(Report.class);
                    type.setText("Tipo de evento: " + report.getType());
                    place.setText("Lugar del  evento: " + report.getPlace());
                    affectedPeople.setText("Cantidad de personas afectadas: " + report.getAffectedPeople());
                    affectedAnimals.setText("Cantidad de animales afectados: " + report.getAffectedAnimals());
                    details.setText("Detalle de vento: " + report.getDetail());
                    startDateTextView.setText("Fecha de inicio: " + report.getStartDateString());
                    if (report.isPathDisabled()) {
                        pathDisabled.setText("El camino se encuentra bloqueado");
                    } else {
                        pathDisabled.setText("El camino se encuentra despejado");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return viewGeneralInformation;
    }

}
