package Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;
import com.github.barteksc.pdfviewer.PDFView;

public class UserManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);

        PDFView userManualView = findViewById((R.id.pdfViewer));
        userManualView.fromAsset("ManualUsuario.pdf").load();
    }
}
