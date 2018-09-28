package coolgroup.com.aline.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import coolgroup.com.aline.fragments.chat.Chats;
import coolgroup.com.aline.fragments.chat.Friends;
import coolgroup.com.aline.fragments.chat.Requests;

public class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Requests requests = new Requests();
                return requests;

            case 1:
                Chats chats = new Chats();
                return chats;

            case 2:
                Friends friends = new Friends();
                return friends;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
