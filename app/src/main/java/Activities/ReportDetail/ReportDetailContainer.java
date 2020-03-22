package Activities.ReportDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.r24app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReportDetailContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail_container);
        ViewPager2 viewPager2 = findViewById(R.id.viewPaper);
        viewPager2.setAdapter(new OpcionPageAdapter(this));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Detalles");
                        tab.setIcon(R.drawable.ic_info);
                        break;
                    case 1:
                        tab.setText("Imagenes");
                        tab.setIcon(R.drawable.ic_images);
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
}
