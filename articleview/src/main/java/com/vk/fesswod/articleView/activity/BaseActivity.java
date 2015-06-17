package com.vk.fesswod.articleView.activity;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SimpleCursorAdapter;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.Article;
import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class BaseActivity extends FragmentActivity implements DataStateChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = 0;
    private SimpleCursorAdapter adapter;
    private Uri mArticleUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillData();
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { ARTICLES_COLUMN_TITLE };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.textViewTitle };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.view_list_tem, null, from,
                to, 0);
    }

    @Override
    public boolean delete(long id) {
        Uri uri = Uri.parse(AppContentProvider.CONTENT_URI_ARTICLE + "/"
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
        values.put(ARTICLES_COLUMN_TITLE, article.getTitle());
        values.put(ARTICLES_COLUMN_DESC, article.getDesc());
        values.put(ARTICLES_COLUMN_IS_PUBLISHED, article.isPublished());
        values.put(ARTICLES_COLUMN_IS_MYOWN, article.isMyOwn());
        values.put(ARTICLES_COLUMN_IMAGE_URL, article.getImageUri());;
        values.put(ARTICLES_COLUMN_UPDATED_AT, article.getUpdateAtTimeStamp());
        values.put(ARTICLES_COLUMN_CREATED_AT, article.getCreateAtTimeStamp());

        if (mArticleUri == null) {
            mArticleUri =getContentResolver()
                    .insert(AppContentProvider.CONTENT_URI_ARTICLE, values);
        } else {

            getContentResolver()
                    .update(mArticleUri, values, null, null);
        }
        return mArticleUri;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { COLUMN_ID,
                ARTICLES_COLUMN_TITLE
        };
        CursorLoader cursorLoader = new CursorLoader(this,
                AppContentProvider.CONTENT_URI_ARTICLE, projection, null, null, null);
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
