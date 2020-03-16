package Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

import java.lang.reflect.Array;
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
        String[] disasterTypeList = new String[5];
        disasterTypeList[0] = "Seleccione el tipo de desastre";
        disasterTypeList[1] = "Costa Rica";
        disasterTypeList[2] = "Nueva Zelanda";
        disasterTypeList[3] = "Holanda";
        disasterTypeList[4] = "Dubai";

        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_layout);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(dataAdapter);
    }

    private void addSpinnerListener() {

    }
}
