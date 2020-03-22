package Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Models.Constants.DataConstants;
import Models.POJOS.Report;
import Services.NaturalDisasterService;
import Services.ReportService;
import Services.UserService;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner disasterTypeSpinner;
    private Switch activateMapLocation;
    private String latitude, longitude, disasterType;
    private TextInputEditText reportLocation, description;
    private TextInputLayout mapLocationLayout, reportDetailLayout;
    private NaturalDisasterService naturalDisasterService;
    private UserService userService;
    private ReportService reportService;
    private boolean validFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        naturalDisasterService = new NaturalDisasterService();
        userService = new UserService();
        reportService = new ReportService();
        addItemsToSpinner();
        addActivateLocationListener();
        addReturnButtonListener();
        addSubmitReportListener();
        addImagesButtonListener();
        addSpinnerListener();
        reportLocation = findViewById(R.id.mapLocationInput);
        mapLocationLayout = findViewById(R.id.mapLocationLayout);
        reportDetailLayout = findViewById(R.id.reportDetailLayout);
        description = findViewById(R.id.reportDetailInput);
        validFields = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DataConstants.LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                activateMapLocation.setChecked(false);
                String selectedPlace = data.getStringExtra("selectedPlace");
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                reportLocation.setText(selectedPlace);
            }
        }

        if (requestCode == DataConstants.SELECT_MULTIPLE_PHOTOS) {
            if (resultCode == Activity.RESULT_OK) {
                activateMapLocation.setChecked(false);
                String selectedPlace = data.getStringExtra("selectedPlace");
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                reportLocation.setText(selectedPlace);
            }
        }
    }

    private void addImagesButtonListener() {
        Button addImagesButton = findViewById(R.id.addImagesButton);

        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportIncidentActivity.this, ImageChooserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addSubmitReportListener() {
        Button submitReportButton = findViewById(R.id.btnSubmitReport);

        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    saveReportInfo();
                }
            }
        });
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(reportLocation.getText().toString())) {
            mapLocationLayout.setError(getResources().getText(R.string.requiredField));
            mapLocationLayout.requestFocus();
            validFields = false;
        } else {
            mapLocationLayout.setError(null);
            validFields = true;
        }

        if (TextUtils.isEmpty(description.getText().toString())) {
            reportDetailLayout.setError(getResources().getText(R.string.requiredField));
            reportDetailLayout.requestFocus();
            validFields = false;
        } else {
            reportDetailLayout.setError(null);
            validFields = true;
        }

        TextView spinnerErrorText = (TextView) disasterTypeSpinner.getSelectedView();

        if (TextUtils.isEmpty(disasterType)) {
            spinnerErrorText.setError(null);
            spinnerErrorText.setTextColor(getResources().getColor(R.color.errorColor, null));
            spinnerErrorText.setText(R.string.requiredField);
            validFields = false;
        } else {
            validFields = true;
        }

        return validFields;
    }

    private void saveReportInfo() {
        boolean isPathDisabled = ((Switch) findViewById(R.id.isPathDisabled)).isChecked();
        TextInputEditText affectedPeopleInput = findViewById(R.id.affectedPeopleInput);
        TextInputEditText affectedAnimalsInput = findViewById(R.id.affectedAnimalsInput);
        int affectedPeople = !affectedPeopleInput.getText().toString().equals("") ? Integer.parseInt(affectedPeopleInput.getText().toString()) : 0;
        int affectedAnimals = !affectedAnimalsInput.getText().toString().equals("") ? Integer.parseInt(affectedAnimalsInput.getText().toString()) : 0;
        Calendar endDate = new GregorianCalendar();
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.YEAR, 1900);

        Calendar startDate = new GregorianCalendar();
        startDate.add(Calendar.DATE, 1);
        startDate.add(Calendar.YEAR, 1900);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String id = userService.getCurrentFirebaseUserId() + simpleDateFormat.format(new Date());

        Report report = new Report(id, disasterType, description.getText().toString(), latitude, longitude, reportLocation.getText().toString(), isPathDisabled,
                true, startDate.getTime(), endDate.getTime(), affectedAnimals, affectedPeople, userService.getCurrentFirebaseUserId());
        reportService.addNewReport(report);
        Toast.makeText(ReportIncidentActivity.this, "Reporte registrado exitosamente", Toast.LENGTH_LONG).show();
        validFields = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMapActivity();
            }
        }, 3500);
    }

    private void addReturnButtonListener() {
        ImageButton returnButton = findViewById(R.id.newReportReturnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapActivity();
            }
        });
    }

    private void startMapActivity() {
        Intent mapIntent = new Intent(ReportIncidentActivity.this, MapActivity.class);
        startActivity(mapIntent);
        finish();
    }

    private void addItemsToSpinner() {
        ArrayList<String> disasterTypeList = new ArrayList<>();
        disasterTypeList.add("Seleccione el tipo de desastre");
        naturalDisasterService.getDisasterTypes(disasterTypeList);
        disasterTypeSpinner = findViewById(R.id.disasterTypeSpinner);

        // Estas lineas permiten hacer un spinner donde los elementos sean de color
        // Assets en strings.xml y en layout spinner_dropdown y spinner_layout
        /*ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.disasterTypes_array, R.layout.spinner_layout);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);*/

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, disasterTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        disasterTypeSpinner.setAdapter(dataAdapter);
    }

    private void addSpinnerListener() {
        disasterTypeSpinner = findViewById(R.id.disasterTypeSpinner);

        disasterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    disasterType = parent.getItemAtPosition(position).toString();
                } else {
                    disasterType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addActivateLocationListener() {
        activateMapLocation = findViewById(R.id.mapActivated);
        activateMapLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(ReportIncidentActivity.this, MapSearchActivity.class);
                    startActivityForResult(intent, DataConstants.LAUNCH_SECOND_ACTIVITY);
                }
            }
        });
    }
}
