package com.vk.fesswod.articleView.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.Article;
import android.widget.SimpleCursorAdapter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by sergeyb on 16.06.15.
 */
public class SimpleCursorAdapterListArticle extends SimpleCursorAdapter implements View.OnClickListener {

    private final Context mContext;
    private LinkedList<Article> mFilteredModelItemsArray;
    private LayoutInflater mInflater;

    public SimpleCursorAdapterListArticle(Context context, List<Article> objects, String[] from, int[] to) {
      super(context, R.layout.view_list_tem, null, from,
              to, 0);
        mContext=context;
        mFilteredModelItemsArray = new LinkedList<>(objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Article currentArticle = mFilteredModelItemsArray.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_list_tem, null, false);
            holder = getViewHolder(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleView.setText(currentArticle.getTitle());
        holder.mDeleleButton.setTag(currentArticle);
        return convertView;
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
        holder.titleView = (TextView) convertView.findViewById(R.id.textViewTitle);
        holder.mDeleleButton = (ImageButton) convertView.findViewById(R.id.imageButtonDeleteItem);
        holder.mDeleleButton.setFocusable(false);
        holder.mDeleleButton.setOnClickListener(this);
        convertView.setTag(holder);
        return holder;
    }

    @Override
    public int getCount() {
        return mFilteredModelItemsArray.size();
    }

    @Override
    public Article getItem(int position) {
        return mFilteredModelItemsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onClick(View v) {
        Article article =(Article) v.getTag();
        mFilteredModelItemsArray.remove(article);
        notifyDataSetChanged();
    }

    /**
     * implementation of ViewHolder pattern for SpaceBodyListAdapter
     */
    static class ViewHolder {
        public TextView titleView;
        public ImageButton mDeleleButton;
    }
}
