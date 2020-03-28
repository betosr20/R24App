package Activities.ReportDetail;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;

import java.util.List;

import Models.POJOS.Image;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {


    Context context;
    List<Image> images;
    public  ImageAdapter (List<Image> pimages, Context pcontext) {

        this.images =  pimages;
        this.context = pcontext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_report,parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.imageView.setImageResource(images.get(position).getFotoUrl());

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageReport);
        }
    }
}
