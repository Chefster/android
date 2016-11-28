package com.codepath.chefster.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.chefster.R;
import com.codepath.chefster.fragments.ContainerFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private int[] tabImages = {R.drawable.ic_category, R.drawable.ic_favorite};

    private String tabTitles[] = new String[]{"CATEGORIES", "FAVORITES"};

    private Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void setFragment(Fragment fragment, int index) {
        mFragmentList.add(index, fragment);
    }

    public void replaceFragment(Fragment fragment, int index) {
        mFragmentList.remove(index);
        mFragmentList.add(index, fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = ContextCompat.getDrawable(context, tabImages[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * The reason this is overriden is a little Hacky. When we would click onto a dish and favorite it
     * We wanted to update the favorite view but it was already instantiated, so we had to call
     * notifyDataSetChanged(), but then it would return us to MainFragment on position 0, so that's
     * why we differentiate.
     * @param object The fragment returning from ViewPager
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        if (object instanceof ContainerFragment) {
            return POSITION_UNCHANGED;
        } else {
            return POSITION_NONE;
        }
    }
}
