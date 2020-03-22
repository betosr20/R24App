package Activities;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

import java.util.List;

import Models.Constants.DataConstants;

public class ImageChooserActivity extends AppCompatActivity {
    private List<Uri> imagesUris;
    private int imagesSelected;
    /*private ImageView imageSelected;
    private ImageView imageSelected2;
    private ImageView imageSelected3;
    private ImageView imageSelected4;
    private ImageView imageSelected5;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);

        /*imageSelected = findViewById(R.id.image1);
        imageSelected2 = findViewById(R.id.image2);
        imageSelected3 = findViewById(R.id.image3);
        imageSelected4 = findViewById(R.id.image4);
        imageSelected5 = findViewById(R.id.image5);*/
        openImagesContainer();
        addLoadImagesButtonListener();
    }

    private void addLoadImagesButtonListener() {
        ImageButton loadImagesButton = findViewById(R.id.loadImagesButton);

        loadImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openImagesContainer() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), DataConstants.SELECT_MULTIPLE_PHOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DataConstants.SELECT_MULTIPLE_PHOTOS && resultCode == RESULT_OK) {
            LinearLayout imagesContainer = findViewById(R.id.imagesContainer);

            ClipData clipData = data != null ? data.getClipData() : null;
            imagesSelected = clipData != null ? clipData.getItemCount() : 0;
            ImageView currentImage = new ImageView(this);

            if (imagesSelected <= DataConstants.MAXNUMBERPHOTOS) {
                if (clipData != null) {
                    for (int i = 0; i < imagesSelected; i++) {
                        currentImage.setImageURI(clipData.getItemAt(0).getUri());
                        imagesContainer.addView(currentImage);
                        setContentView(imagesContainer);
                    }

                /*imageSelected.setImageURI(clipData.getItemAt(0).getUri());
                imageSelected2.setImageURI(clipData.getItemAt(1).getUri());
                imageSelected3.setImageURI(clipData.getItemAt(2).getUri());
                imageSelected4.setImageURI(clipData.getItemAt(3).getUri());
                imageSelected5.setImageURI(clipData.getItemAt(4).getUri());*/
                } else {
                    Uri imageUri = data.getData();
                    currentImage.setImageURI(imageUri);
                    imagesContainer.addView(currentImage);
                    setContentView(imagesContainer);

                /*if (imageUri != null) {
                    imageSelected.setImageURI(imageUri);
                }*/

                 /*for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
            }*/
                }
            } else {
                Toast.makeText(ImageChooserActivity.this, "Se permite un maximo de" + DataConstants.MAXNUMBERPHOTOS + "imágenes", Toast.LENGTH_LONG).show();
            }
        }
    }
}
