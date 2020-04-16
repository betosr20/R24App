package Activities.ReportDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.r24app.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Activities.MapActivity;
import Adapters.OptionPageAdapter;
import Models.Constants.FirebaseClasses;
import Models.POJOS.ReportPicture;

public class ReportDetailContainer extends AppCompatActivity {
    private List<ReportPicture> imagesReference = new ArrayList<>();
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail_container);
        ViewPager2 viewPager2 = findViewById(R.id.viewPaper);
        Intent intent = getIntent();
        String idReport = intent.getStringExtra("idReport");
        viewPager2.setAdapter(new OptionPageAdapter(this, idReport));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.ReportPicture);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    imagesReference.add(snapshot.getValue(ReportPicture.class));
                }
                for (ReportPicture reportPicture : imagesReference) {
                    if (reportPicture.getReportId().equals(idReport)) {
                        counter += 1;
                    }
                }
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                        tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Detalle");
                                tab.setIcon(R.drawable.ic_info);
                                break;
                            case 1:
                                tab.setText("Imágenes");
                                tab.setIcon(R.drawable.ic_images);
                                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                                badgeDrawable.setBackgroundColor(
                                        ContextCompat.getColor(getApplication(), R.color.blackColor)
                                );
                                badgeDrawable.setVisible(true);
                                badgeDrawable.setNumber(counter);
                                break;
                            case 2:
                                tab.setText("Ubicación");
                                tab.setIcon(R.drawable.ic_location_on_black_24dp);
                                break;
                        }
                    }
                }
                );
                tabLayoutMediator.attach();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void windowBack(View v) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}
