package Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ReportIncidentActivity extends AppCompatActivity {

    private Spinner disasterTypeSpinner;
    private Switch activateMapLocation;
    private String latitude, longitude, disasterType;
    private TextInputEditText reportLocation, description;
    private TextInputLayout mapLocationLayout, reportDetailLayout;
    private NaturalDisasterService naturalDisasterService;
    private UserService userService;
    private TextView imagesSelectedText;
    private ReportService reportService;
    private ArrayList<Uri> imagesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        naturalDisasterService = new NaturalDisasterService();
        userService = new UserService();
        reportService = new ReportService();
        addItemsToSpinner();
        addActivateLocationListener();
        addSubmitReportListener();
        addImagesButtonListener();
        addSpinnerListener();
        reportLocation = findViewById(R.id.mapLocationInput);
        mapLocationLayout = findViewById(R.id.mapLocationLayout);
        reportDetailLayout = findViewById(R.id.reportDetailLayout);
        description = findViewById(R.id.reportDetailInput);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case DataConstants.LAUNCH_MAPSEARCH_ACTIVITY:
                    activateMapLocation.setChecked(false);
                    String selectedPlace = data.getStringExtra("selectedPlace");

                    if (!TextUtils.isEmpty(selectedPlace)) {
                        latitude = data.getStringExtra("latitude");
                        longitude = data.getStringExtra("longitude");
                        reportLocation.setText(selectedPlace);
                    } else {
                        reportLocation.setText("");
                    }

                    break;

                case DataConstants.SELECT_MULTIPLE_PHOTOS:
                    imagesUri = (ArrayList<Uri>) data.getSerializableExtra("imagesUris");
                    int imagesSelected = data.getIntExtra("selectedImages", 0);

                    if (imagesSelected > 0) {
                        imagesSelectedText.setText(imagesSelected + " imagen(es) para mostrar");
                    }

                    break;
            }
        } else {
            activateMapLocation.setChecked(false);
            imagesSelectedText.setText(getResources().getText(R.string.noImagesSelected));
        }
    }

    private void addImagesButtonListener() {
        imagesSelectedText = findViewById(R.id.imagesSelectedText);
        imagesSelectedText.setText(getResources().getText(R.string.noImagesSelected));
        Button addImagesButton = findViewById(R.id.addImagesButton);

        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(DataConstants.PERMISSION_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case DataConstants.PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent imagesIntent = new Intent(ReportIncidentActivity.this, ImageChooserActivity.class);
                    startActivityForResult(imagesIntent, DataConstants.SELECT_MULTIPLE_PHOTOS);

                } else {
                    Toast.makeText(ReportIncidentActivity.this, "Para acceder a las imágenes es necesario brindar permisos", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;

        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions) {
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
        } else {
            Intent imagesIntent = new Intent(ReportIncidentActivity.this, ImageChooserActivity.class);
            startActivityForResult(imagesIntent, DataConstants.SELECT_MULTIPLE_PHOTOS);
        }
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
        boolean validFields = true;

        if (TextUtils.isEmpty(reportLocation.getText().toString())) {
            mapLocationLayout.setError(getResources().getText(R.string.requiredField));
            mapLocationLayout.requestFocus();
            validFields = false;
        } else {
            mapLocationLayout.setError(null);
        }

        TextView spinnerErrorText = (TextView) disasterTypeSpinner.getSelectedView();

        if (TextUtils.isEmpty(disasterType)) {
            spinnerErrorText.setError(null);
            spinnerErrorText.setTextColor(getResources().getColor(R.color.errorColor, null));
            spinnerErrorText.setText(R.string.requiredField);
            validFields = false;
        } else {
            spinnerErrorText.setError(null);
        }

        if (TextUtils.isEmpty(description.getText().toString())) {
            reportDetailLayout.setError(getResources().getText(R.string.requiredField));
            reportDetailLayout.requestFocus();
            validFields = false;
        } else {
            reportDetailLayout.setError(null);
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
        endDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.YEAR, 1900);

        Calendar startDate = new GregorianCalendar();
        startDate.add(Calendar.MONTH, 1);
        startDate.add(Calendar.YEAR, 1900);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String reportId = userService.getCurrentFirebaseUserId() + simpleDateFormat.format(new Date());

        Report report = new Report(reportId, disasterType, description.getText().toString(), latitude, longitude, reportLocation.getText().toString(), isPathDisabled,
                true, startDate.getTime(), endDate.getTime(), affectedAnimals, affectedPeople, userService.getCurrentFirebaseUserId(),
                getStringDate(true), getStringDate(false));
        Button submitReportButton = findViewById(R.id.btnSubmitReport);
        submitReportButton.setEnabled(false);
        if (reportService.addNewReport(report) && reportService.saveReportImages(imagesUri, reportId)) {
            Toast.makeText(ReportIncidentActivity.this, "Reporte registrado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ReportIncidentActivity.this, "Hubo un problema al registrar el reporte", Toast.LENGTH_LONG).show();
            submitReportButton.setEnabled(true);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMapActivity();
            }
        }, 3500);
    }

    public String getStringDate(boolean isSartDate) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringDate = simpleDateFormat.format(new Date());

        if (!isSartDate) {
            String[] dateSplitted = stringDate.split("-");
            dateSplitted[0] = Integer.parseInt(dateSplitted[0]) + 1 + "";

            if (Integer.parseInt(dateSplitted[0]) <= 9) {
                stringDate = "0" + dateSplitted[0] + "-" + dateSplitted[1] + "-" + dateSplitted[2];
            } else {
                stringDate = dateSplitted[0] + "-" + dateSplitted[1] + "-" + dateSplitted[2];
            }
        }

        return stringDate;
    }

    private void startMapActivity() {
        Intent mapIntent = new Intent(ReportIncidentActivity.this, MapActivity.class);
        startActivityForResult(mapIntent, DataConstants.LAUNCH_MAPSEARCH_ACTIVITY);
    }

    private void addItemsToSpinner() {
        ArrayList<String> disasterTypeList = new ArrayList<>();
        disasterTypeList.add(getResources().getString(R.string.disasterTypeLabel));
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
                    startActivityForResult(intent, DataConstants.LAUNCH_MAPSEARCH_ACTIVITY);
                }
            }
        });
    }

    public void windowBack(View v) {
        onBackPressed();
    }
}
