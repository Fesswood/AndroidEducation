package com.vk.fesswod.articleView.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.AppSQLiteOpenHelper;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.fragment.FragmentArticleList;

import android.widget.SimpleCursorAdapter;

import java.util.List;

/**
 * Created by sergeyb on 16.06.15.
 */
public class SimpleCursorAdapterListArticle extends SimpleCursorAdapter implements View.OnClickListener {

    private LayoutInflater mInflater;
    private ListItemDeleteListener mOnDeleteListener;

    public SimpleCursorAdapterListArticle(Context context, String[] from, int[] to) {
        super(context, R.layout.view_list_tem, null, from,
                to, 0);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.view_list_tem, parent, false);
        ViewHolder holder  = getViewHolder(rowView);
        rowView.setTag(holder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(AppSQLiteOpenHelper.ARTICLES_COLUMN_TITLE));
        int isMyOwn = cursor.getInt(cursor.getColumnIndex(AppSQLiteOpenHelper.ARTICLES_COLUMN_IS_MYOWN));
        if(isMyOwn == 0){
            holder.mDeleteButton.setVisibility(View.GONE);
        }else {
            Object obj = cursor.getString(cursor.getColumnIndex(AppSQLiteOpenHelper.COLUMN_ID));
            holder.mDeleteButton.setVisibility(View.VISIBLE);
            holder.mDeleteButton.setTag(obj);
        }
        holder.titleView.setText(name);

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
        holder.mDeleteButton = (ImageButton) convertView.findViewById(R.id.imageButtonDeleteItem);
        holder.mDeleteButton.setFocusable(false);
        holder.mDeleteButton.setOnClickListener(this);
        convertView.setTag(holder);
        return holder;
    }

    @Override
    public void onClick(View v) {
        mOnDeleteListener.deleteArticle(v);
    }

    public void setOnDeleteListener(ListItemDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    /**
     * implementation of ViewHolder pattern for SpaceBodyListAdapter
     */
    static class ViewHolder {
        public TextView titleView;
        public ImageButton mDeleteButton;
    }

    /**
     * Created by sergeyb on 19.06.15.
     */
    public interface ListItemDeleteListener {
        void deleteArticle(View v);
    }
}
