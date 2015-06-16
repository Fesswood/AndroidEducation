package info.goodline.starsandplanets.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.webkit.WebView;

import java.util.ArrayList;

import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.fragment.FragmentSpaceBodyDetail;

/**
 * Created by sergeyb on 10.06.15.
 */
public class SpaceBodyViewPagerAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    /**
     * List of news topics
     */
    private ArrayList<SpaceBody> mSpaceBodylist;
    private ArrayList<SpaceBody> mFavorite = new ArrayList<>();
    private LayoutInflater mInflater;


    public SpaceBodyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mSpaceBodylist = new ArrayList<>();
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mSpaceBodylist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_GALAXIES, mContext.getResources()));
        mSpaceBodylist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
        mSpaceBodylist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_PLANETS, mContext.getResources()));

    }


    @Override
    public int getCount() {
        return mSpaceBodylist.size();
    }


    @Override
    public Fragment getItem(int position) {
        return FragmentSpaceBodyDetail.newInstance(mSpaceBodylist.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public ArrayList<SpaceBody> getDataList() {
        return mSpaceBodylist;
    }


    public void deleteItem(SpaceBody itemForDeleted) {
        int i = mSpaceBodylist.indexOf(itemForDeleted);
        SpaceBody spaceBody = mSpaceBodylist.get(i);
        mSpaceBodylist.remove(i);
        notifyDataSetChanged();
    }

    public void setFavorite(SpaceBody favoriteSpaceBody, boolean isChecked) {
        int i = mSpaceBodylist.indexOf(favoriteSpaceBody);
        mSpaceBodylist.get(i).setFavorite(isChecked);
        if (isChecked) {
            mFavorite.add(favoriteSpaceBody);
        } else {
            mFavorite.remove(favoriteSpaceBody);
        }
    }


    /**
     * implementation of ViewHolder pattern for SpaceBodyListAdapter
     */
    static class ViewHolder {
        public WebView imageView;
    }
}