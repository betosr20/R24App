package Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.R;

import java.util.ArrayList;

import Models.Constants.DataConstants;

public class ImageChooserActivity extends AppCompatActivity {
    private ArrayList<Uri> imagesUris;
    private int imagesSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);
        openImagesContainer();
        addLoadImagesButtonListener();
        imagesUris = new ArrayList<>();
    }

    private void addLoadImagesButtonListener() {
        ImageButton loadImagesButton = findViewById(R.id.loadImagesButton);

        loadImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnDataToReportsActivity(imagesSelected);
            }
        });
    }

    private void openImagesContainer() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, ""), DataConstants.SELECT_MULTIPLE_PHOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DataConstants.SELECT_MULTIPLE_PHOTOS && resultCode == RESULT_OK) {
            LinearLayout imagesContainer = findViewById(R.id.imagesContainer);

            ClipData clipData = data != null ? data.getClipData() : null;
            imagesSelected = clipData != null ? clipData.getItemCount() : 0;
            ImageView currentImage;

            if (imagesSelected <= DataConstants.MAXNUMBERPHOTOS) {
                if (clipData != null) {
                    for (int i = 0; i < imagesSelected; i++) {
                        currentImage = new ImageView(this);
                        currentImage.setPadding(0, 0, 0, 50);

                        if (currentImage.getParent() != null) {
                            ((ViewGroup) currentImage.getParent()).removeView(currentImage);
                        }
                        currentImage.setImageURI(clipData.getItemAt(i).getUri());
                        imagesUris.add(clipData.getItemAt(i).getUri());
                        imagesContainer.addView(currentImage);
                    }
                } else {

                    imagesSelected = 1;
                    currentImage = new ImageView(this);
                    Uri imageUri = data.getData();

                    if (currentImage.getParent() != null) {
                        ((ViewGroup) currentImage.getParent()).removeView(currentImage);
                    }

                    currentImage.setImageURI(imageUri);
                    imagesUris.add(imageUri);
                    imagesContainer.addView(currentImage);
                }
            } else {
                Toast.makeText(ImageChooserActivity.this, "Se permite un maximo de " + DataConstants.MAXNUMBERPHOTOS + " imágenes", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(ImageChooserActivity.this, "Ninguna imagen fue seleccionada", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void returnDataToReportsActivity(int imagesSelected) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("imagesUris", imagesUris);
        returnIntent.putExtra("selectedImages", imagesSelected);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
