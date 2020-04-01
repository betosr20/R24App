package Activities.ReportDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.r24app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInformation extends Fragment {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private String idReport;
    private Report report;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    TextView type, place,affectedPeople,affectedAnimals, details, startDateTextView, endDateTextView;
    View viewGeneralInformation;

    public GeneralInformation() {

    }

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
         viewGeneralInformation = inflater.inflate(R.layout.fragment_general_infomation,container,false);
         type = (TextView) viewGeneralInformation.findViewById(R.id.reportType);
         place = (TextView) viewGeneralInformation.findViewById(R.id.reportPlace);
         affectedPeople = (TextView) viewGeneralInformation.findViewById(R.id.reportAffectedPeople);
         affectedAnimals = (TextView) viewGeneralInformation.findViewById(R.id.reportAffectedAnimals);
         details = (TextView) viewGeneralInformation.findViewById(R.id.reportDetail);
         startDateTextView = (TextView) viewGeneralInformation.findViewById(R.id.reportStartDate);
         endDateTextView = (TextView) viewGeneralInformation.findViewById(R.id.reportEndDate);

         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Valida si existe el arreglo, osea si hay datos
                if(dataSnapshot.exists()) {
                    report = dataSnapshot.getValue(Report.class);
                    type.setText("Tipo de Evento: " + report.getType());
                    place.setText("Lugar del  Evento: " + report.getPlace());
                    affectedPeople.setText("Cantidad de personas afectadas: " + report.getAffectedPeople());
                    affectedAnimals.setText("Cantidad de Animales afectadas: " + report.getAffectedAnimals());
                    details.setText("Detalle de Evento: " + report.getDetail());
                    startDateTextView.setText("Fecha de Inicio: " + dateFormat.format(report.getStartDate()));
                    endDateTextView.setText("Fecha de Fin: " + dateFormat.format(report.getEndDate()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return viewGeneralInformation;
    }

}
