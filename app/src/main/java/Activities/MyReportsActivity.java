package Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyReportsAdapter;
import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Services.UserService;

public class MyReportsActivity extends AppCompatActivity {
    private List<Report> userReports;
    private RecyclerView userReportsList;
    private ProgressBar progressBar;
    private TextView noElementsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mis Reportes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        noElementsText = findViewById(R.id.noElementsText);
        setProgressBar();
        userReportsList = findViewById(R.id.userReportsList);
        userReportsList.setHasFixedSize(true);
        userReports = null;
        getUserReports();
    }

    public void setProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void getUserReports() {
        userReportsList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query userReportsQuery = database.getReference(FirebaseClasses.Report).orderByChild(FirebaseClasses.ReportOwnerAttribute).equalTo(new UserService().getCurrentFirebaseUserId());

        userReportsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userReports = new ArrayList<>();
                    Report report;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        report = snapshot.getValue(Report.class);
                        if (report.isActive()) {
                            userReports.add(report);
                        }
                    }

                    MyReportsAdapter myReportsAdapter = new MyReportsAdapter(userReports, MyReportsActivity.this);
                    userReportsList.setAdapter(myReportsAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    checkExistingElements();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkExistingElements() {
        if (userReports.size() > 0) {
            noElementsText.setVisibility(View.INVISIBLE);
        } else {
            noElementsText.setVisibility(View.VISIBLE);
        }
    }
}
