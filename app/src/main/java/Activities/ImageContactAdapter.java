package Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Models.POJOS.User;

public class ImageContactAdapter extends RecyclerView.ViewHolder {
    TextView fullName;
    TextView cellPhone;
    ImageView imageView;
    ProgressBar progressBar;
    TextView needHelp;

    public ImageContactAdapter(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.fullName_userContact);
        cellPhone = itemView.findViewById(R.id.cellPhone_userContact);
        imageView = itemView.findViewById(R.id.user_contact_image);
        progressBar = itemView.findViewById(R.id.loadContacImage);
        needHelp = itemView.findViewById(R.id.userHelpDistress);

    }
}