package Activities.ReportDetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.r24app.R;

import java.text.SimpleDateFormat;

import Models.POJOS.Report;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInformation extends Fragment {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Report report;

    TextView type, place,affectedPeople,affectedAnimals, details, startDateTextView, endDateTextView;
    View viewGeneralInformation;
    public GeneralInformation(Report report) {
        // Required empty public constructor
        this.report = report;
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

         type.setText("Tipo de Evento: " + report.getType());
         place.setText("Lugar del  Evento: " + report.getPlace());
         affectedPeople.setText("Cantidad de personas afectadas: " + report.getAffectedPeople());
         affectedAnimals.setText("Cantidad de Animales afectadas: " + report.getAffectedAnimals());
         details.setText("Detalle de Evento: " + report.getDetail());
         startDateTextView.setText("Fecha de Inicio: " + dateFormat.format(report.getStartDate()));
         endDateTextView.setText("Fecha de Fin: " + dateFormat.format(report.getEndDate()));

        return viewGeneralInformation;
    }

}
