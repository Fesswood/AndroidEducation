package com.vk.fesswod.articleView.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.AppSQLiteOpenHelper;
import com.vk.fesswod.articleView.fragment.ListItemDeleteListener;

import java.util.zip.Inflater;

/**
 * Created by sergeyb on 16.06.15.
 */
public class AdapterExpandableListArticle  extends SimpleCursorTreeAdapter implements View.OnClickListener {

    private Context mContext;
    private String[] mChildFrom;
    private int[] mChildTo;
    private ListItemDeleteListener mOnDeleteListener;
    private LayoutInflater mInflater;

    public AdapterExpandableListArticle(Context context, Cursor cursor, int groupLayout,
                     String[] groupFrom, int[] groupTo, int childLayout,
                     String[] childFrom, int[] childTo) {
        super(context, cursor, groupLayout, groupFrom, groupTo,
                childLayout, childFrom, childTo);
        mContext = context;
        mChildFrom = childFrom;
        mChildTo = childTo;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        //super.bindGroupView(view, context, cursor, isExpanded);
        TextView groupTitle =(TextView) view.findViewById(R.id.textViewTitle);
        groupTitle.setText(cursor.getString(cursor.getColumnIndex(AppSQLiteOpenHelper.GROUPS_COLUMN_TITLE)));
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
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

    @Override
    public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.view_list_tem, parent, false);
        ViewHolder holder  = getViewHolder(rowView);
        rowView.setTag(holder);
        return rowView;
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
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        int idColumn = groupCursor.getColumnIndex(AppSQLiteOpenHelper.COLUMN_ID);

        long categoryId= groupCursor.getLong(idColumn);
        return mContext.getContentResolver().query(AppContentProvider.CONTENT_URI_ARTICLES,
                                                   mChildFrom,
                                                   AppSQLiteOpenHelper.ARTICLES_COLUMN_GROUP_ID+" =?",
                                                   new String[]{""+categoryId},
                                                   null);
    }

    @Override
    public void onClick(View v) {
        mOnDeleteListener.deleteArticle(v);
    }

    public void setOnDeleteListener(ListItemDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

}
