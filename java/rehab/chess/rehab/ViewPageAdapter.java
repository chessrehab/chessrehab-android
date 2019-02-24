package rehab.chess.rehab;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> listFragment= new ArrayList<>();
    private boolean doNotifyDataSetChangedOnce = false;

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragmentGameActive();
            case 1:
                return new FragmentGameFinished();

            case 2:
                return new FragmentChallenges();
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;

    }



}
