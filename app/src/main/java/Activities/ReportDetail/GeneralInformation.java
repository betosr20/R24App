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

public class GeneralInformation extends Fragment {
    private String idReport;
    private Report report;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    TextView type, place, affectedPeople, affectedAnimals, details, startDateTextView, pathDisabled;
    View viewGeneralInformation;
    TextInputEditText reportDetail;

    public GeneralInformation(String idReport) {
        this.idReport = idReport;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(FirebaseClasses.Report).child(idReport);
        this.report = new Report();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGeneralInformation = inflater.inflate(R.layout.fragment_general_infomation, container, false);
        type = viewGeneralInformation.findViewById(R.id.reportType);
        place = viewGeneralInformation.findViewById(R.id.reportPlace);
        affectedPeople = viewGeneralInformation.findViewById(R.id.reportAffectedPeople);
        affectedAnimals = viewGeneralInformation.findViewById(R.id.reportAffectedAnimals);
        details = viewGeneralInformation.findViewById(R.id.DetailReport);
        startDateTextView = viewGeneralInformation.findViewById(R.id.reportStartDate);
        pathDisabled = viewGeneralInformation.findViewById(R.id.reportPathDisabled);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    report = dataSnapshot.getValue(Report.class);
                    setNaturalDisasterType(type,report.getType());
                    setIconPlace(place,report.getPlace());
                    setAffectedPeopleIcon(affectedPeople,report.getAffectedPeople());
                    setAffectedAnimalsIcon(affectedAnimals,report.getAffectedAnimals());
                    setEventDetailIcon(details,report.getDetail());
                    setStarDateIcon(startDateTextView, report.getStartDateString());
                    setPathDisabledIcon(pathDisabled, report.isPathDisabled());
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
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_location, 0, 0, 0);
        textView.setText("   "+place);
    }
    public void setAffectedPeopleIcon (TextView textView, int affectedPeople) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.peopleaffected, 0, 0, 0);
        textView.setText("   "+affectedPeople);
    }
    public void setAffectedAnimalsIcon (TextView textView, int affectedAnimals) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.animalsaffeted, 0, 0, 0);
        textView.setText("   "+affectedAnimals);
    }
    public void setStarDateIcon(TextView textView, String startDate) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.event, 0, 0, 0);
        textView.setText("   "+startDate);
    }
     public void setPathDisabledIcon(TextView textView, boolean PathDisable) {
         textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.road, 0, 0, 0);
        if(PathDisable) {
            textView.setText(" "+ "Bloqueado");
        } else {
            textView.setText(" "+ "Despejado");
        }
     }
    public void setEventDetailIcon(TextView textView, String details) {
        textView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.article, 0, 0, 0);
        textView.setText("   "+details);
    }

}
