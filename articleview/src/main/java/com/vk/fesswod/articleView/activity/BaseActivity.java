package com.vk.fesswod.articleView.activity;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;
import com.vk.fesswod.articleView.fragment.FragmentArticleList;
import com.vk.fesswod.articleView.fragment.dummy.DummyContent;

import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class BaseActivity extends FragmentActivity implements DataStateChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = 0;
    private static final String DEBUG_TAG = FragmentArticleList.class.getSimpleName();
    protected SimpleCursorAdapterListArticle adapter;
    private Uri mArticleUri;
    boolean keepInsertedArticle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG,"onCreate");
        fillData();
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] {COLUMN_ID,ARTICLES_COLUMN_TITLE };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.textViewTitle };

        Log.d(DEBUG_TAG,"fillData");
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapterListArticle(this, from, to);


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
        CursorLoader cursorLoader = new CursorLoader(this,
                AppContentProvider.CONTENT_URI_ARTICLES, projection, null, null, orderBy);


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
