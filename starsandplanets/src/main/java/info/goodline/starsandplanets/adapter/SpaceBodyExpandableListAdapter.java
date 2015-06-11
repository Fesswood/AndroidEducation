package info.goodline.starsandplanets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.data.DummyContent;
import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.fragments.Callbacks;

/**
 * Created by sergeyb on 10.06.15.
 */
public class SpaceBodyExpandableListAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater mInflater;
    private final Callbacks mCallbacks;
    private int[] mOffset;
    private ArrayList<ArrayList<SpaceBody>> mGroups;
    private ArrayList<String> mGroupsNames;
    private Context mContext;

    public SpaceBodyExpandableListAdapter(Context context) {
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
        mContext = context;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroups = new ArrayList<>();
        mGroupsNames= new ArrayList<String>(SpaceBody.getSpaceBodyGroup(mContext.getResources()));
        mGroups.add(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_GALAXIES, mContext.getResources()));
        mGroups.add(new ArrayList<SpaceBody>());
        mGroups.add(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
        mGroups.add(SpaceBody.getSpaceBodyFromResource(SpaceBody.FLAG_GET_STARS, mContext.getResources()));
        mOffset=new int[mGroups.size()];
        int off=0;
        for (int i=0;i<mGroups.size();i++) {
            mOffset[i]=off;
            int vr=mGroups.get(i).size();
            off+=vr;
        }
    }

    public SpaceBodyExpandableListAdapter(Context context, ArrayList<ArrayList<SpaceBody>> groups){
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
        mContext = context;
        mGroups = groups;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spacebody_group_item, null);
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(mGroupsNames.get(groupPosition));

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        SpaceBody spaceBody = mGroups.get(groupPosition).get(childPosition);
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(mOffset[groupPosition]+childPosition);
        return true;
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
        return holder;
    }

    /**
     * implementation of ViewHolder pattern for SpaceBodyListAdapter
     */
    static class ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView dateView;
        public TextView descView;
    }
}
