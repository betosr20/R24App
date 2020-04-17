package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;

import java.util.List;

import Activities.MyReportsActivity;
import Activities.ReportDetail.ReportDetailContainer;
import Models.POJOS.Report;
import Services.ReportService;

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

    @SuppressLint({"SetTextI18n", "RestrictedApi"})

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.reportType.setText("Incidente: " + userReports.get(position).getType());
            holder.reportDate.setText("Fecha: " + userReports.get(position).getStartDateString());

            holder.moreOptionsButton.setOnClickListener(view -> {
                if (view.getId() == R.id.moreOptionsButton) {

                    Context wrapper = new ContextThemeWrapper(context, R.style.AppTheme_CustomPopupStyle);
                    @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(context);
                    MenuInflater inflater = new MenuInflater(context);
                    inflater.inflate(R.menu.myreportsmenu, menuBuilder);
                    @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(wrapper, menuBuilder, view);
                    optionsMenu.setForceShowIcon(true);

                    // Set Item Click Listener
                    menuBuilder.setCallback(new MenuBuilder.Callback() {
                        @Override
                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.viewReportOption:
                                    Intent intent = new Intent(context, ReportDetailContainer.class);
                                    intent.putExtra("idReport", userReports.get(position).getId());
                                    context.startActivity(intent);
                                    return true;
                                case R.id.editReportOption:
                                    Toast.makeText(context, "Editar reporte " + userReports.get(position).getType(), Toast.LENGTH_LONG).show();
                                    return true;
                                case R.id.deleteReportOption:
                                    ReportService reportService = new ReportService();
                                    userReports.get(position).setActive(false);

                                    if (reportService.deleteReport(userReports.get(position))) {
                                        userReports.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Reporte eliminado exitosamente", Toast.LENGTH_LONG).show();

                                        if (userReports.size() == 0) {
                                            ((MyReportsActivity) context).checkExistingElements();
                                        }
                                    } else {
                                        Toast.makeText(context, "Error durante el proceso de eliminación", Toast.LENGTH_LONG).show();
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onMenuModeChange(MenuBuilder menu) {}
                    });
                    // Display the menu
                    optionsMenu.show();
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
        TextView reportType, reportDate;
        ImageView moreOptionsButton;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reportType = itemView.findViewById(R.id.reportType);
            reportDate = itemView.findViewById(R.id.reportDate);
            moreOptionsButton = itemView.findViewById(R.id.moreOptionsButton);
        }
    }
}
