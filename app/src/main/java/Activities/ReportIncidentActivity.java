package Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Models.POJOS.Report;
import Services.ReportService;
import Services.UserService;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner disasterTypeSpinner;
    private Switch activateMapLocation;
    private String latitude, longitude, disasterType;
    private TextInputEditText reportLocation, place, description;
    private TextInputLayout mapLocationLayout, reportDetailLayout;
    private final int LAUNCH_SECOND_ACTIVITY = 1;
    private ReportService reportService;
    private UserService userService;
    private TextView spinnerErrorText;
    private FirebaseAuth mAuth;
    private boolean validFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        reportService = new ReportService();
        userService = new UserService();
        addItemsToSpinner();
        addActivateLocationListener();
        addReturnButtonListener();
        addSubmitReportListener();
        addImagesButtonListener();
        addSpinnerListener();
        reportLocation = findViewById(R.id.mapLocationInput);
        place = findViewById(R.id.mapLocationInput);
        mapLocationLayout = findViewById(R.id.mapLocationLayout);
        reportDetailLayout = findViewById(R.id.reportDetailLayout);
        description = findViewById(R.id.reportDetailInput);
        mAuth = FirebaseAuth.getInstance();
        validFields = true;
        // saveReportTypeInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activateMapLocation.setChecked(false);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String selectedPlace = data.getStringExtra("selectedPlace");
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                reportLocation.setText(selectedPlace);
            }
        }
    }

    /*private void saveReportTypeInfo() {
        database = FirebaseDatabase.getInstance();
        NaturalDisaster naturalDisaster = new NaturalDisaster("1", "Asteroide", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster.getName());
        databaseReference.setValue(naturalDisaster);

        NaturalDisaster naturalDisaster2 = new NaturalDisaster("2", "Avalancha", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster2.getName());
        databaseReference.setValue(naturalDisaster2);

        NaturalDisaster naturalDisaster3 = new NaturalDisaster("3", "Corrimiento de tierra", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster3.getName());
        databaseReference.setValue(naturalDisaster3);

        NaturalDisaster naturalDisaster4 = new NaturalDisaster("4", "Derramamiento del petróleo", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster4.getName());
        databaseReference.setValue(naturalDisaster4);

        NaturalDisaster naturalDisaster5 = new NaturalDisaster("5", "Erupción límnica", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster5.getName());
        databaseReference.setValue(naturalDisaster5);

        NaturalDisaster naturalDisaster6 = new NaturalDisaster("6", "Erupción volcánica", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster6.getName());
        databaseReference.setValue(naturalDisaster6);

        NaturalDisaster naturalDisaster7 = new NaturalDisaster("7", "Fuga de materiales radiactivos", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster7.getName());
        databaseReference.setValue(naturalDisaster7);

        NaturalDisaster naturalDisaster8 = new NaturalDisaster("8", "Granizo", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster8.getName());
        databaseReference.setValue(naturalDisaster8);

        NaturalDisaster naturalDisaster9 = new NaturalDisaster("9", "Hundimiento", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster9.getName());
        databaseReference.setValue(naturalDisaster9);

        NaturalDisaster naturalDisaster10 = new NaturalDisaster("10", "Huracán", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster10.getName());
        databaseReference.setValue(naturalDisaster10);

        NaturalDisaster naturalDisaster11 = new NaturalDisaster("11", "Incendio forestal", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster11.getName());
        databaseReference.setValue(naturalDisaster11);

        NaturalDisaster naturalDisaster12 = new NaturalDisaster("12", "Inundación", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster12.getName());
        databaseReference.setValue(naturalDisaster12);

        NaturalDisaster naturalDisaster13 = new NaturalDisaster("13", "Sequía", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster13.getName());
        databaseReference.setValue(naturalDisaster13);

        NaturalDisaster naturalDisaster14 = new NaturalDisaster("14", "Terremoto", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster14.getName());
        databaseReference.setValue(naturalDisaster14);

        NaturalDisaster naturalDisaster15 = new NaturalDisaster("15", "Tormenta", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster15.getName());
        databaseReference.setValue(naturalDisaster15);

        NaturalDisaster naturalDisaster16 = new NaturalDisaster("16", "Tormenta de arena", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster16.getName());
        databaseReference.setValue(naturalDisaster16);

        NaturalDisaster naturalDisaster17 = new NaturalDisaster("17", "Tormenta eléctrica", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster17.getName());
        databaseReference.setValue(naturalDisaster17);

        NaturalDisaster naturalDisaster18 = new NaturalDisaster("18", "Tornado", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster18.getName());
        databaseReference.setValue(naturalDisaster18);

        NaturalDisaster naturalDisaster19 = new NaturalDisaster("19", "Tsunami", "1");
        databaseReference =  database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisaster19.getName());
        databaseReference.setValue(naturalDisaster19);
    }*/

    private void addImagesButtonListener() {
        Button addImagesButton = findViewById(R.id.addImagesButton);

        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportIncidentActivity.this, MapSearchActivity.class);
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
        if (TextUtils.isEmpty(place.getText().toString())) {
            mapLocationLayout.setError("Espacio requerido *");
            mapLocationLayout.requestFocus();
            validFields = false;
        } else {
            mapLocationLayout.setError(null);
            validFields = true;
        }

        if (TextUtils.isEmpty(description.getText().toString())) {
            reportDetailLayout.setError("Espacio requerido *");
            reportDetailLayout.requestFocus();
            validFields = false;
        } else {
            reportDetailLayout.setError(null);
            validFields = true;
        }

        if (TextUtils.isEmpty(disasterType)) {
            spinnerErrorText = (TextView) disasterTypeSpinner.getSelectedView();
            spinnerErrorText.setError(null);
            spinnerErrorText.setTextColor(getResources().getColor(R.color.errorColor, null));
            spinnerErrorText.setText("Espacio requerido *");
            validFields = false;
        } else {
            spinnerErrorText = null;
            validFields = true;
        }

        return validFields;
    }

    private void saveReportInfo() {
        String reportOwnerId = userService.getCurrentFirebaseUserId();
        boolean isPathDisabled = ((Switch) findViewById(R.id.isPathDisabled)).isChecked();
        TextInputEditText affectedPeopleInput = findViewById(R.id.affectedPeopleInput);
        TextInputEditText affectedAnimalsInput = findViewById(R.id.affectedAnimalsInput);
        int affectedPeople = !affectedPeopleInput.getText().toString().equals("") ? Integer.parseInt(affectedPeopleInput.getText().toString()) : 0;
        int affectedAnimals = !affectedAnimalsInput.getText().toString().equals("") ? Integer.parseInt(affectedAnimalsInput.getText().toString()) : 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        endDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        startDate.add(Calendar.MONTH, 1);

        Report report = new Report("2", disasterType, description.getText().toString(), latitude, longitude, place.getText().toString(), isPathDisabled,
                true, startDate.getTime(), endDate.getTime(), affectedAnimals, affectedPeople, "JaSQwI4ncPhCQHEoyey09PX3RL12");
        reportService.addNewReport(report);
        Toast.makeText(ReportIncidentActivity.this, "Reporte registrado exitosamente", Toast.LENGTH_LONG).show();
        validFields = false;
    }

    private void addReturnButtonListener() {
        ImageButton returnButton = findViewById(R.id.newReportReturnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dkfhgkdfjghdfkjghdjghdkjgfhdkf");
            }
        });
    }

    private void addItemsToSpinner() {
        ArrayList<String> disasterTypeList = new ArrayList<>();
        disasterTypeList.add("Seleccione el tipo de desastre");
        reportService.getDisasterTypes(disasterTypeList);
        disasterTypeSpinner = findViewById(R.id.disasterTypeSpinner);
        //ArrayList<String> disasterTypeList = new ArrayList<>();
        /*String[] disasterTypeList = new String[5];
        disasterTypeList[0] = "Seleccione el tipo de desastre";
        disasterTypeList[1] = "Costa Rica";
        disasterTypeList[2] = "Nueva Zelanda";
        disasterTypeList[3] = "Holanda";
        disasterTypeList[4] = "Dubai";*/

        /*ArrayList<String> disasterTypeList = new ArrayList<>();
        disasterTypeList.add("Seleccione el tipo de desastre");
        disasterTypeList.add("Costa Rica");
        disasterTypeList.add("Nueva Zelanda");
        disasterTypeList.add("Holanda");
        disasterTypeList.add("Dubai");*/

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
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                }
            }
        });
    }
}
