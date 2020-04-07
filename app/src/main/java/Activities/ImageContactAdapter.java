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

public class ImageContactAdapter extends RecyclerView.Adapter<ImageContactAdapter.MyViewHolder> {
    List<User> userList =  new ArrayList<>();
    Context context;

    public ImageContactAdapter(List<User> puserList, Context pcontext) {
        this.userList =puserList;
        this.context = pcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(context);
         View view = inflater.inflate(R.layout.user_contact,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user =  userList.get(position);
        holder.fullName.setText(user.getName() + "" + user.getLastName());
        holder.cellPhone.setText(user.getCellPhone());

        if (user.getProfileImage() != null) {
            Picasso.get().load(user.getProfileImage()).into(holder.imageView);
            holder.progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_person));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fullName;
        TextView cellPhone;
        ImageView imageView;
        ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName_userContact);
            cellPhone = itemView.findViewById(R.id.cellPhone_userContact);
            imageView = itemView.findViewById(R.id.user_contact_image);
            progressBar = itemView.findViewById(R.id.loadContacImage);
        }
    }
}
