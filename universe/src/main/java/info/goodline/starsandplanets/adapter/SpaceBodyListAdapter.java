package info.goodline.starsandplanets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by sergeyb on 10.06.15.
 */
public class SpaceBodyListAdapter extends ArrayAdapter<SpaceBody> implements AdapterDataChangeListener{
/**
 * List of news topics
 */
private ArrayList<SpaceBody> mSpaceBodyList;
private final Context mContext;
private LayoutInflater mInflater;

public SpaceBodyListAdapter(Context context) {
        super(context, R.layout.spacebody_list_item);
        mSpaceBodyList =new ArrayList<>();
        mContext=context;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    mSpaceBodyList.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_GALAXIES, mContext.getResources()));
    mSpaceBodyList.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
    mSpaceBodyList.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_PLANETS, mContext.getResources()));



}

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        SpaceBody spaceBody = mSpaceBodyList.get(position);
        ViewHolder holder;
        if(convertView == null) {
        convertView = mInflater.inflate(R.layout.spacebody_list_item, null, true);
        holder  = getViewHolder(convertView);
        } else{
        holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(spaceBody.getName());


        return convertView;
        }


/**
 * Get viewHolder by tag or create new if it doesn't exist
 * @param convertView view of news topic
 * @return instance of viewHolder
 */
private ViewHolder getViewHolder(View convertView) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.titleView = (TextView) convertView.findViewById(R.id.news_title);
        convertView.setTag(holder);
        convertView.setLongClickable(true);
        return holder;
        }

@Override
public int getCount() {
        return mSpaceBodyList.size();
        }

@Override
public SpaceBody getItem(int position) {
        return mSpaceBodyList.get(position);
        }

@Override
public long getItemId(int position) {
        return position;
        }
/**
 * Add list with news to current NewsList
 * @param parsedNewsList list to add
 */
public void addNewslist(ArrayList<SpaceBody> parsedNewsList) {
        parsedNewsList.removeAll(mSpaceBodyList);
        mSpaceBodyList.addAll(parsedNewsList);
        notifyDataSetChanged();
        }
public ArrayList<SpaceBody> getNewsList() {
        return mSpaceBodyList;
        }

/**
 * prepend list with news to current NewsList
 * used when news received from "pull to update"
 * @param newsList list to prepend
 */
public void prependNewsList(ArrayList<SpaceBody> newsList) {
        for(SpaceBody boardNews: newsList){
        mSpaceBodyList.add(0, boardNews);
        }
        notifyDataSetChanged();
        }

        @Override
        public void deleteItem(int position, int group) {
           mSpaceBodyList.remove(position);
           notifyDataSetChanged();
        }

        /**
 * implementation of ViewHolder pattern for SpaceBodyListAdapter
 */
static class ViewHolder {
    public ImageView imageView;
    public TextView titleView;
    public TextView dateView;
    public TextView descView;
    public Menu menuView;
}
}