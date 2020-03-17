package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinner;
    private Switch activateMapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        addItemsToSpinner();
        addActivateLocationListener();
    }

    private void addItemsToSpinner() {
        spinner = findViewById(R.id.disasterTypeSpinner);
        /*String[] disasterTypeList = new String[5];
        disasterTypeList[0] = "Seleccione el tipo de desastre";
        disasterTypeList[1] = "Costa Rica";
        disasterTypeList[2] = "Nueva Zelanda";
        disasterTypeList[3] = "Holanda";
        disasterTypeList[4] = "Dubai";*/

        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_layout);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(dataAdapter);
    }

    private void addSpinnerListener() {

    }

    private void addActivateLocationListener() {
        activateMapLocation = findViewById(R.id.mapActivated);
        activateMapLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(ReportIncidentActivity.this, MapSearchActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
