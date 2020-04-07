package Activities.ReportDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OptionPageAdapter extends FragmentStateAdapter {
    private String idReport;

    public OptionPageAdapter(@NonNull FragmentActivity fragmentActivity, String idReport) {
        super(fragmentActivity);
        this.idReport = idReport;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new GeneralInformation(this.idReport);
            case 1:
                return new Images(this.idReport);
            default:
                return new location(this.idReport);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
