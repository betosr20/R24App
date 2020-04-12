package Activities.ReportDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
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

    TextView type, place, affectedPeople, affectedAnimals, details, startDateTextView, endDateTextView;
    View viewGeneralInformation;
    TextInputEditText reportDetail;

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
        type = (TextView) viewGeneralInformation.findViewById(R.id.reportType);
        place = (TextView) viewGeneralInformation.findViewById(R.id.reportPlace);
        affectedPeople = (TextView) viewGeneralInformation.findViewById(R.id.reportAffectedPeople);
        affectedAnimals = (TextView) viewGeneralInformation.findViewById(R.id.reportAffectedAnimals);
        reportDetail = (TextInputEditText) viewGeneralInformation.findViewById(R.id.etDetailReport);
        startDateTextView = (TextView) viewGeneralInformation.findViewById(R.id.reportStartDate);
        endDateTextView = (TextView) viewGeneralInformation.findViewById(R.id.reportEndDate);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Valida si existe el arreglo, osea si hay datos
                if (dataSnapshot.exists()) {
                    report = dataSnapshot.getValue(Report.class);
                    setNaturalDisasterType(type,report.getType());
                    setIconPlace(place,report.getPlace());
                    setAffectedPeopleIcon(affectedPeople,report.getAffectedPeople());
                    setAffectedAnimalsIcon(affectedAnimals,report.getAffectedAnimals());
                    setEventDetailIcon(reportDetail,report.getDetail());
                    setStarDateIcon(startDateTextView, report.getStartDateString());
                    setEndDateIcon(endDateTextView, report.getEndDateString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return viewGeneralInformation;
    }
    public void setNaturalDisasterType( TextView textView, String type) {
        switch (type) {
            case "Asteroide":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.impact, 0, 0, 0);
                break;
            case "Avalancha":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.avalanche, 0, 0, 0);
                break;
            case "Corrimiento de tierra":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.earthquake, 0, 0, 0);
                break;
            case "Derramamiento del petróleo":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.oil, 0, 0, 0);
                break;
            case "Erupción límnica":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.limica, 0, 0, 0);
                break;
            case "Erupción volcánica":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.eruption, 0, 0, 0);
                break;
            case "Fuga de materiales radiactivos":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.toxic, 0, 0, 0);
                break;
            case "Granizo":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.hailstorm, 0, 0, 0);
                break;
            case "Hundimiento":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.hundimiento, 0, 0, 0);
                break;
            case "Huracán":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.typhoon, 0, 0, 0);
                break;
            case "Incendio forestal":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.fire, 0, 0, 0);
                break;
            case "Inundación":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.flood, 0, 0, 0);
                break;
            case "Sequía":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.drought, 0, 0, 0);
                break;
            case "Terremoto":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.terremoto, 0, 0, 0);
                break;
            case "Tormenta de arena":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.sandstorm, 0, 0, 0);
                break;
            case "Tormenta eléctrica":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.thunder, 0, 0, 0);
                break;
            case "Tornado":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.hurricane, 0, 0, 0);
                break;
            case "Tsunami":
                textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.tsunami, 0, 0, 0);
                break;
        }
        textView.setText("   "+type);
    }
    public void setIconPlace(TextView textView, String place) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.place, 0, 0, 0);
        textView.setText("   "+place);
    }
    public void setAffectedPeopleIcon (TextView textView, int affectedPeople) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.peopleaffected, 0, 0, 0);
        textView.setText("   "+affectedPeople);
    }
    public void setAffectedAnimalsIcon (TextView textView, int affectedAnimals) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.death, 0, 0, 0);
        textView.setText("   "+affectedAnimals);
    }
    public void setStarDateIcon(TextView textView, String startDate) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.event, 0, 0, 0);
        textView.setText("   "+startDate);
    }
    public void setEndDateIcon(TextView textView, String endDate) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.no, 0, 0, 0);
        textView.setText("   "+endDate);
    }
    public void setEventDetailIcon(TextInputEditText textView, String details) {
        textView.setText("   "+details);
    }

}
