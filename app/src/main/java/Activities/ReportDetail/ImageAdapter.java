package Activities.ReportDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Activities.modules.GlideApp;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private Context context;
    private List<StorageReference> referencesList = new ArrayList<>();
    public  ImageAdapter (List<StorageReference> referencesList, Context context) {
        this.context = context;
        this.referencesList = referencesList;
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
        GlideApp.with(context)
                .load(referencesList.get(position))
                .into(holder.imageView);
        if (referencesList.get(position).toString().equals("gs://r24app-e1e7d.appspot.com/myImages/image-placeholder.jpg")) {
            holder.noImageText.setText("No hay imágenes en este reporte");
        }
    }

    @Override
    public int getItemCount() {
        return referencesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView noImageText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageReport);
            noImageText = itemView.findViewById(R.id.tv_noImages);
        }
    }
}
