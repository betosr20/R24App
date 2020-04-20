package Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Activities.ReportDetail.GeneralInformation;
import Activities.ReportDetail.Images;
import Activities.ReportDetail.location;

public class OptionPageAdapter extends FragmentStateAdapter {
    private String idReport;
    private String naturalDisasterName;

    public OptionPageAdapter(@NonNull FragmentActivity fragmentActivity, String idReport, String naturalDisasterName) {
        super(fragmentActivity);
        this.idReport = idReport;
        this.naturalDisasterName = naturalDisasterName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new GeneralInformation(this.idReport);
            case 1:
                return new Images(this.idReport);
            case 3:
                return new SecondaryEffect(this.naturalDisasterName);
            default:
                return new location(this.idReport);

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
