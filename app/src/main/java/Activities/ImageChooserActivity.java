package Activities;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

import Models.Constants.DataConstants;

public class ImageChooserActivity extends AppCompatActivity {
    private Uri uri;
    private ImageView imageSelected;
    private ImageView imageSelected2;
    private ImageView imageSelected3;
    private ImageView imageSelected4;
    private ImageView imageSelected5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);

        imageSelected = findViewById(R.id.image1);
        imageSelected2 = findViewById(R.id.image2);
        imageSelected3 = findViewById(R.id.image3);
        imageSelected4 = findViewById(R.id.image4);
        imageSelected5 = findViewById(R.id.image5);
        setImagesListeners();
    }

    private void setImagesListeners() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un máximo de 5 imagenes"), DataConstants.SELECT_MULTIPLE_PHOTOS);

        /*imageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), "Selecciona una imagen"), DataConstants.SELECT_PHOTO);
            }
        });

        imageSelected2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageSelected3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageSelected4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageSelected5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DataConstants.SELECT_MULTIPLE_PHOTOS && resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                imageSelected.setImageURI(clipData.getItemAt(0).getUri());
                imageSelected2.setImageURI(clipData.getItemAt(1).getUri());
                /*imageSelected3.setImageURI(clipData.getItemAt(2).getUri());
                imageSelected4.setImageURI(clipData.getItemAt(3).getUri());
                imageSelected5.setImageURI(clipData.getItemAt(4).getUri());*/
            } else {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imageSelected.setImageURI(imageUri);
                }
            }

            /*for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
            }*/
        }
    }
}
