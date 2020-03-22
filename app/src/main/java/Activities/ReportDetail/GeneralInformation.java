package Activities.ReportDetail;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.r24app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import Models.POJOS.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInformation extends Fragment {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date startDate = new Date();
    Date endDate = new Date();
    Report fakeReport = new Report("lt9oLjW930crR4a4u39TPAPAKnx12020-03-21",
            "Avalancha",
            "Avalancha en Tres rios y el edificio Terramall colapso por completo",
            "9.902066899999998",
            "-83.9962491",
            "Terramall",
            true,
            true,
            startDate,
            endDate,
            5,
            5,
            startDate);
    TextView type, place,affectedPeople,affectedAnimals, details, startDateTextView, endDateTextView;
    View viewGeneralInformation;
    public GeneralInformation() {
        // Required empty public constructor
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
         type.setText("Tipo de Evento: " + fakeReport.getType());
         place.setText("Lugar del  Evento: " + fakeReport.getPlace());
         affectedPeople.setText("Cantidad de personas afectadas: " + fakeReport.getAffectedPeople());
         affectedAnimals.setText("Cantidad de Animales afectadas: " + fakeReport.getAffectedAnimals());
         details.setText("Detalle de Evento: " + fakeReport.getDetail());
         startDateTextView.setText("Fecha de Inicio: " + dateFormat.format(fakeReport.getStartDate()));
         endDateTextView.setText("Fecha de Fin: " + dateFormat.format(fakeReport.getEndDate()));


        return viewGeneralInformation;
    }

}
