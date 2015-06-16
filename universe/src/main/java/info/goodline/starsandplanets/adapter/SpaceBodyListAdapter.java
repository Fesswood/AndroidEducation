package info.goodline.starsandplanets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.listener.AdapterDataChangeListener;
import info.goodline.starsandplanets.listener.FragmentListStateChangeListener;

/**
 * Created by sergeyb on 10.06.15.
 */
public class SpaceBodyListAdapter extends ArrayAdapter<SpaceBody> implements AdapterDataChangeListener, CompoundButton.OnCheckedChangeListener,Filterable {
    private final Context mContext;
    FragmentListStateChangeListener mListStateChangeListener;

    private ArrayList<SpaceBody> allModelItemsArray;
    private ArrayList<SpaceBody> filteredModelItemsArray;
    private TextFilter filter;
    private LayoutInflater mInflater;
    private ArrayList<SpaceBody> mFavorite = new ArrayList<>();

    public SpaceBodyListAdapter(Context context, FragmentListStateChangeListener fragmentSpaceBodyList) {
        super(context, R.layout.spacebody_list_item);
        filteredModelItemsArray = new ArrayList<>();
        allModelItemsArray=new ArrayList<>();
        mContext = context;
        mListStateChangeListener = fragmentSpaceBodyList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        filteredModelItemsArray.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_GALAXIES, mContext.getResources()));
        filteredModelItemsArray.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
        filteredModelItemsArray.addAll(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_PLANETS, mContext.getResources()));

        allModelItemsArray.addAll(allModelItemsArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SpaceBody spaceBody = filteredModelItemsArray.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spacebody_list_item, null, false);
            holder = getViewHolder(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mCheckBox.setTag(spaceBody);
        holder.titleView.setText(spaceBody.getName());
        holder.mCheckBox.setChecked(spaceBody.isFavorite());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new TextFilter(filteredModelItemsArray,this);
        }
        return filter;
    }

    /**
     * Get viewHolder by tag or create new if it doesn't exist
     *
     * @param convertView view of news topic
     * @return instance of viewHolder
     */
    private ViewHolder getViewHolder(View convertView) {
        ViewHolder holder;
        holder = new ViewHolder();
        convertView.setFocusable(false);
        convertView.setClickable(false);
        holder.titleView = (TextView) convertView.findViewById(R.id.news_title);
        holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.favorite_check);
        holder.mCheckBox.setFocusable(false);
        holder.mCheckBox.setOnCheckedChangeListener(this);
        holder.titleView.setFocusable(false);
        holder.titleView.setClickable(false);
        convertView.setTag(holder);
        //convertView.setLongClickable(true);
        return holder;
    }

    @Override
    public int getCount() {
        return filteredModelItemsArray.size();
    }

    @Override
    public SpaceBody getItem(int position) {
        return filteredModelItemsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Add list with news to current NewsList
     *
     * @param parsedNewsList list to add
     */
    public void addNewslist(ArrayList<SpaceBody> parsedNewsList) {
        parsedNewsList.removeAll(filteredModelItemsArray);
        filteredModelItemsArray.addAll(parsedNewsList);
        notifyDataSetChanged();
    }

    public ArrayList<SpaceBody> getNewsList() {
        return filteredModelItemsArray;
    }

    /**
     * prepend list with news to current NewsList
     * used when news received from "pull to update"
     *
     * @param newsList list to prepend
     */
    public void prependNewsList(ArrayList<SpaceBody> newsList) {
        for (SpaceBody boardNews : newsList) {
            filteredModelItemsArray.add(0, boardNews);
        }
        notifyDataSetChanged();
    }


    @Override
    public void deleteItem(SpaceBody itemForDelete) {
        filteredModelItemsArray.remove(itemForDelete);
        notifyDataSetChanged();
    }

    @Override
    public SpaceBody getItem(int group, int position) {
        return filteredModelItemsArray.get(position);
    }

    @Override
    public void setItemFavorite(SpaceBody selectedSpaceBody) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if (tag != null) {
            mFavorite.add((SpaceBody) tag);
            mListStateChangeListener.changeStateFavoriteItem((SpaceBody) tag, isChecked);
        }
    }

    /**
     * implementation of ViewHolder pattern for SpaceBodyListAdapter
     */
    static class ViewHolder {
        public TextView titleView;
        public CheckBox mCheckBox;
    }
}