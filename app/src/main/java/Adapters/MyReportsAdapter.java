package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.reportType.setText("Incidente: " + userReports.get(position).getType());
            holder.reportDate.setText("Fecha: " + userReports.get(position).getStartDateString());

            holder.moreOptionsButton.setOnClickListener(view -> {
                if (view.getId() == R.id.moreOptionsButton) {
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.myreportsmenu, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.viewReportOption:
                                Toast.makeText(context, "Ver reporte " + userReports.get(position).getType(), Toast.LENGTH_LONG).show();
                                break;
                            case R.id.editReportOption:
                                Toast.makeText(context, "Editar reporte " + userReports.get(position).getType(), Toast.LENGTH_LONG).show();
                                break;
                            case R.id.deleteReportOption:
                                Toast.makeText(context, "Eliminar reporte " + userReports.get(position).getType(), Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }

                        return true;
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userReports.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reportType;
        TextView reportDate;
        ImageView moreOptionsButton;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reportType = itemView.findViewById(R.id.reportType);
            reportDate = itemView.findViewById(R.id.reportDate);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);
        }
    }
}
