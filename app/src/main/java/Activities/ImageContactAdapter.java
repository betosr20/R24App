package Activities;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.r24app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageContactAdapter extends RecyclerView.ViewHolder {
    View view;
    public ImageContactAdapter(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void seeDetails( Context context, String pfullName, String pcellPhone, String pimage) {
        //Elementos de la vista
        TextView fullName = view.findViewById(R.id.fullName_userContact);
        TextView cellphone = view.findViewById(R.id.cellPhone_userContact);
        ImageView imageView = view.findViewById(R.id.user_contact_image);
        // Copulacion de los elemetos
        fullName.setText(pfullName);
        cellphone.setText(pcellPhone);
        Picasso.get().load(pimage).into(imageView);
    }
}
