package com.vk.fesswod.articleView.activity;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.AppSQLiteOpenHelper;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;
import com.vk.fesswod.articleView.fragment.FragmentArticleList;

import java.util.ArrayList;
import java.util.List;

import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class BaseActivity extends FragmentActivity implements ChangeFilterClauseListener,DataStateChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DEBUG_TAG = FragmentArticleList.class.getSimpleName();
    private static final String SELECTION = "SELECTION";
    private static final String SELECTION_ARGS = "SELECTION_ARGS";
    protected SimpleCursorAdapterListArticle adapter;
    private Uri mArticleUri;
    boolean keepInsertedArticle=false;
    private int ARTICLES_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG,"onCreate");
        fillData();
    }

    private void fillData() {
        Log.d(DEBUG_TAG,"fillData");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] {COLUMN_ID,ARTICLES_COLUMN_TITLE };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.textViewTitle };
        getLoaderManager().initLoader(ARTICLES_LOADER, null, this);
        adapter = new SimpleCursorAdapterListArticle(this, from, to);
    }


    /**
     * setup filter and init query from DB
     */
    @Override
    public void initFilter(boolean mIsOnlyMyFilter,boolean misUnpublishedFilter,CharSequence mKeyword){

        List<String> selectionArgs		= new ArrayList<>();
        StringBuilder filterSelection	= new StringBuilder();
        if(mIsOnlyMyFilter) {
            filterSelection.append(AppSQLiteOpenHelper.ARTICLES_COLUMN_IS_MYOWN);
            filterSelection.append("= ? ");
            selectionArgs.add("1");
        }
        if(misUnpublishedFilter) {
            if(filterSelection.length() > 0){
                filterSelection.append( " AND " );
            }
            filterSelection.append(AppSQLiteOpenHelper.ARTICLES_COLUMN_IS_PUBLISHED);
            filterSelection.append("= ?");
            selectionArgs.add("1");
        }
        if(!TextUtils.isEmpty(mKeyword)) {
            if(filterSelection.length() > 0){
                filterSelection.append( " AND " );
            }
            filterSelection.append( AppSQLiteOpenHelper.ARTICLES_COLUMN_TITLE);
            filterSelection.append( " LIKE ?");
            selectionArgs.add( mKeyword + "%");

        }

        Bundle args = null;
        if(filterSelection.length() > 0) {
            args	= new Bundle();
            args.putString(SELECTION, filterSelection.toString());
            args.putStringArray(SELECTION_ARGS
                    , selectionArgs.toArray(new String[selectionArgs.size()]));
        }

        getLoaderManager().restartLoader(ARTICLES_LOADER, args, this);
    }






    @Override
    public boolean delete(long id) {
        Uri uri = Uri.parse(AppContentProvider.CONTENT_URI_ARTICLES + "/"
                + id);
        int  delete= getContentResolver().delete(uri, null, null);
        fillData();
        return delete>0?true:false;
    }

    @Override
    public boolean update(Article article) {
        return false;
    }

    @Override
    public Uri insert(Article article) {
        ContentValues values = new ContentValues();
        values.put(SQL_INSERT_OR_REPLACE, true);
        if(article.getId()!=-1){
            values.put(COLUMN_ID, article.getId());
        }
        values.put(ARTICLES_COLUMN_TITLE, article.getTitle());
        values.put(ARTICLES_COLUMN_DESC, article.getDesc());
        values.put(ARTICLES_COLUMN_IS_PUBLISHED, article.isPublished());
        values.put(ARTICLES_COLUMN_IS_MYOWN, article.isMyOwn());
        values.put(ARTICLES_COLUMN_IMAGE_URL, article.getImageUri());;
        values.put(ARTICLES_COLUMN_UPDATED_AT, article.getUpdateAtTimeStamp());
        values.put(ARTICLES_COLUMN_CREATED_AT, article.getCreateAtTimeStamp());

        if (mArticleUri == null || !keepInsertedArticle) {
            mArticleUri =getContentResolver()
                    .insert(AppContentProvider.CONTENT_URI_ARTICLES, values);
        } else {

            getContentResolver()
                    .update(mArticleUri, values, null, null);
        }
        return mArticleUri;
    }

    @Override
    public void insertAllGroups(ArticleGroup[] categories) {
        for(int i=0;i <categories.length;i++){
            ContentValues values = new ContentValues();
            values.put(GROUPS_COLUMN_TITLE, categories[i].getTitle());
            values.put(COLUMN_ID, categories[i].getId());

            getContentResolver()
                        .insert(AppContentProvider.CONTENT_URI_GROUPS, values);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { COLUMN_ID,
                ARTICLES_COLUMN_TITLE,
                ARTICLES_COLUMN_IS_MYOWN
        };
        String orderBy =COLUMN_ID+","+ARTICLES_COLUMN_TITLE;
        String selection = null;
        String[] selectionARGS = null;
        if(args != null){
           selection = args.getString(SELECTION);
            selectionARGS = args.getStringArray(SELECTION_ARGS);
        }

        CursorLoader cursorLoader = new CursorLoader(this,
                AppContentProvider.CONTENT_URI_ARTICLES, projection, selection, selectionARGS, orderBy);


        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


}
