package provider.androidbuffer.com.cameracontact;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by incred-dev
 * on 29/8/18.
 */

class MainAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 2;

    MainAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return CameraFragment.newInstance();

            case 1:
                return ContactFragment.newInstance();

            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Camera";

            case 1:
                return "Contact";

            default:
                return "";
        }
    }

}