package Activities;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.POJOS.Report;
import Services.ReportService;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageButton returnButton;
    private Button submitReportButton, addImagesButton;
    private Switch activateMapLocation, isPathDisabled;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        addItemsToSpinner();
        addActivateLocationListener();
        addReturnButtonListener();
        addSubmitReportListener();
        addImagesButtonListener();
        addSpinnerListener();
        // saveReportTypeInfo();
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

    private void saveReportInfo() {
        boolean isPathDisabled = ((Switch) findViewById(R.id.isPathDisabled)).isChecked();
        TextInputEditText affectedPeople = findViewById(R.id.affectedPeopleInput);
        TextInputEditText affectedAnimals = findViewById(R.id.affectedAnimalsInput);
        TextInputEditText place = findViewById(R.id.mapLocationInput);
        TextInputEditText description = findViewById(R.id.reportDetailInput);

        Report report = new Report();
        new ReportService().addNewReport(report);
    }

    private void addReturnButtonListener() {
        returnButton = findViewById(R.id.newReportReturnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportIncidentActivity.this.addSpinnerListener();
            }
        });
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
        spinner = findViewById(R.id.disasterTypeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    System.out.println(parent.getItemAtPosition(position).toString());
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
                    startActivity(intent);
                }
            }
        });
    }
}
