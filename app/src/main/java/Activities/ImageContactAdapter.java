package Activities;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;

import java.util.ArrayList;

public class ImageContactAdapter extends RecyclerView.Adapter<ImageContactAdapter.MyViewHolder> {
    ArrayList<Uri> uriArrayList= new ArrayList<>();
    Context context;
    public ImageContactAdapter( ArrayList<Uri> puriArrayList,Context pcontext) {
        this.uriArrayList =  puriArrayList;
        this.context = pcontext;
    }

    @NonNull
    @Override
    public ImageContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.user_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageContactAdapter.MyViewHolder holder, int position) {

        holder.imageView.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_contact_image);
        }
    }
}
