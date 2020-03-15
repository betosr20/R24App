package Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

import java.util.ArrayList;
import java.util.List;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        addItemsToSpinner();
    }

    private void addItemsToSpinner() {
        spinner = findViewById(R.id.disasterTypeSpinner);
        List<String> disasterTypeList = new ArrayList<>();
        disasterTypeList.add("Seleccione el tipo de desastre");
        disasterTypeList.add("Costa Rica");
        disasterTypeList.add("Nueva Zelanda");
        disasterTypeList.add("Holanda");
        disasterTypeList.add("Dubai");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, disasterTypeList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void addSpinnerListener() {

    }
}
