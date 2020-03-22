package Activities.ReportDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OpcionPageAdapter extends FragmentStateAdapter {

    public OpcionPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new GeneralInformation();
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
