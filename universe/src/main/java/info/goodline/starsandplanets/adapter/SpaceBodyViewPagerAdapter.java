package info.goodline.starsandplanets.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.activity.ViewPagerCallback;
import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyDetail;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyExpandableList;

/**
 * Created by sergeyb on 10.06.15.
 */
public class SpaceBodyViewPagerAdapter extends FragmentStatePagerAdapter {
/**
 * List of news topics
 */
private ArrayList<SpaceBody> mNewslist;
private ArrayList<SpaceBody> mFavorite = new ArrayList<>();
private final Context mContext;
private LayoutInflater mInflater;


    public SpaceBodyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mNewslist=new ArrayList<>();
        mContext=context;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    mNewslist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_GALAXIES, mContext.getResources()));
    mNewslist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
    mNewslist.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_PLANETS, mContext.getResources()));

}


        @Override
        public int getCount() {
        return mNewslist.size();
        }


        @Override
        public Fragment getItem(int position) {
          return FragmentSpaceBodyDetail.newInstance(mNewslist.get(position));
        }
    @Override
    public int getItemPosition(Object object) {
        return  PagerAdapter.POSITION_NONE;
    }


    /**
 * Add list with news to current NewsList
 * @param parsedNewsList list to add
 */
public void addNewslist(ArrayList<SpaceBody> parsedNewsList) {
        parsedNewsList.removeAll(mNewslist);
        mNewslist.addAll(parsedNewsList);
        notifyDataSetChanged();
        }
public ArrayList<SpaceBody> getNewsList() {
        return mNewslist;
        }

/**
 * prepend list with news to current NewsList
 * used when news received from "pull to update"
 * @param newsList list to prepend
 */
public void prependNewsList(ArrayList<SpaceBody> newsList) {
        for(SpaceBody boardNews: newsList){
        mNewslist.add(0,boardNews);
        }
        notifyDataSetChanged();
        }

    public void deleteItem(SpaceBody itemForDeleted) {
        int i = mNewslist.indexOf(itemForDeleted);
        SpaceBody spaceBody = mNewslist.get(i);
        mNewslist.remove(i);
        notifyDataSetChanged();
    }

    public void setFavorite(SpaceBody favoriteSpaceBody) {
        int i = mNewslist.indexOf(favoriteSpaceBody);
        mNewslist.get(i).setFavorite(true);
        mFavorite.add(favoriteSpaceBody);
    }


    /**
 * implementation of ViewHolder pattern for SpaceBodyListAdapter
 */
static class ViewHolder {
    public WebView imageView;
}
}