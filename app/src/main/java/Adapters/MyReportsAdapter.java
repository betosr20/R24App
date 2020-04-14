package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;

import java.util.List;

import Models.POJOS.Report;

public class MyReportsAdapter extends RecyclerView.Adapter<MyReportsAdapter.MyViewHolder> {

    private Context context;
    private List<Report> userReports;

    public MyReportsAdapter(List<Report> userReports, Context context) {
        this.userReports = userReports;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_reports_options, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.reportType.setText(userReports.get(position).getType());
        holder.reportDate.setText(userReports.get(position).getStartDateString());
    }

    @Override
    public int getItemCount() {
        return userReports.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView viewReport;
        ImageView editReport;
        ImageView deleteReport;
        TextView reportType;
        TextView reportDate;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewReport = itemView.findViewById(R.id.viewReport);
            editReport = itemView.findViewById(R.id.editReport);
            deleteReport = itemView.findViewById(R.id.deleteReport);
            reportType = itemView.findViewById(R.id.reportType);
            reportDate = itemView.findViewById(R.id.reportDate);
        }
    }
}
