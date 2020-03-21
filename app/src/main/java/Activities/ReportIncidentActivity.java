package Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Models.Constants.FirebaseClasses;
import Models.POJOS.NaturalDisaster;
import Models.POJOS.Report;
import Services.ReportService;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageButton returnButton;
    private Button submitReportButton, addImagesButton;
    private Switch activateMapLocation;
    private String selectedPlace, latitude, longitude, disasterType;
    private TextInputEditText reportLocation;
    private final int LAUNCH_SECOND_ACTIVITY = 1;
    private ReportService reportService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        reportService = new ReportService();
        addItemsToSpinner();
        addActivateLocationListener();
        addReturnButtonListener();
        addSubmitReportListener();
        addImagesButtonListener();
        addSpinnerListener();
        // saveReportTypeInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activateMapLocation.setChecked(false);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                selectedPlace = data.getStringExtra("selectedPlace");
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                reportLocation = findViewById(R.id.mapLocationInput);
                reportLocation.setText(selectedPlace);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

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
        addImagesButton = findViewById(R.id.addImagesButton);

        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportIncidentActivity.this, MapSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addSubmitReportListener() {
        submitReportButton = findViewById(R.id.btnSubmitReport);
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReportInfo();
            }
        });
    }

    private void validateFields() {
        boolean validFields = true;
    }

    private void saveReportInfo() {
        boolean isPathDisabled = ((Switch) findViewById(R.id.isPathDisabled)).isChecked();
        TextInputEditText affectedPeopleInput = findViewById(R.id.affectedPeopleInput);
        TextInputEditText affectedAnimalsInput = findViewById(R.id.affectedAnimalsInput);
        TextInputEditText place = findViewById(R.id.mapLocationInput);
        TextInputEditText description = findViewById(R.id.reportDetailInput);
        int affectedPeople = !affectedPeopleInput.getText().toString().equals("") ? Integer.parseInt(affectedPeopleInput.getText().toString()) : 0;
        int affectedAnimals = !affectedAnimalsInput.getText().toString().equals("") ? Integer.parseInt(affectedAnimalsInput.getText().toString()) : 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        endDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        startDate.add(Calendar.MONTH, 1);

        Report report = new Report("1", disasterType, description.getText().toString(), latitude, longitude, place.getText().toString(), isPathDisabled,
                true, startDate.getTime(), endDate.getTime(), affectedAnimals, affectedPeople);
        new ReportService().addNewReport(report);
    }

    private void addReturnButtonListener() {
        returnButton = findViewById(R.id.newReportReturnButton);

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
        spinner = findViewById(R.id.disasterTypeSpinner);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, disasterTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        spinner.setAdapter(dataAdapter);
    }

    private void addSpinnerListener() {
        spinner = findViewById(R.id.disasterTypeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
