package Activities.ReportDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Models.POJOS.Report;

public class OptionPageAdapter extends FragmentStateAdapter {
    private Report report;

    public OptionPageAdapter(@NonNull FragmentActivity fragmentActivity , Report report) {
        super(fragmentActivity);
        this.report = report;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new GeneralInformation(this.report);
            case 1:
                return new Images();
            default:
                return new location();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
