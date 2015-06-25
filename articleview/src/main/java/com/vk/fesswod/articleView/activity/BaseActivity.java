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
import com.vk.fesswod.articleView.adapter.AdapterExpandableListArticle;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.AppSQLiteOpenHelper;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleCategory;
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

    private final static int ARTICLES_LOADER = 0;
    private final static int ARTICLE_SYNC = 1;
    private static final int GROUP_LOADER = 2;
    private static final String ID_SERVER_LIST = "SERVER_ID_LIST";

    private SimpleCursorAdapterListArticle adapter;
    private AdapterExpandableListArticle adapterExp;
    private Uri mArticleUri;
    boolean keepInsertedArticle=false;

    String[] childFrom = new String[] {COLUMN_ID,ARTICLES_COLUMN_TITLE,ARTICLES_COLUMN_IS_MYOWN};
    // Fields on the UI to which we map
    int[] childTo = new int[] { R.id.textViewTitle };

    String[] groupFrom = new String[] {COLUMN_ID,GROUPS_COLUMN_TITLE };
    // Fields on the UI to which we map
    int[] groupTo = new int[] { R.id.textViewTitle };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "onCreate");
        fillSimpleListData();
        fillExpListData();
    }

    private void fillSimpleListData() {
        Log.d(DEBUG_TAG, "fillSimpleListData");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        getLoaderManager().initLoader(ARTICLES_LOADER, null, this);
        setAdapter(new SimpleCursorAdapterListArticle(this, childFrom, childTo));

    }
    private void fillExpListData() {
        Log.d(DEBUG_TAG, "fillExpListData");
        getLoaderManager().initLoader(GROUP_LOADER, null, this);
        setAdapterExp(new AdapterExpandableListArticle(this,null, R.layout.view_list_group_tem,
                groupFrom,
                groupTo,
                R.layout.view_list_tem,
                childFrom,
                childTo
        ));
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
        fillSimpleListData();
        return delete>0;
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
        values.put(ARTICLES_COLUMN_GROUP_ID, article.getArticleGroupId());
        if( article.getPhotoContainer() != null){
            values.put(ARTICLES_COLUMN_IMAGE_URL,article.getPhotoContainer().getImageUrl());
        }

        values.put(ARTICLES_COLUMN_UPDATED_AT, article.getUpdateAtTime().getTime());
        values.put(ARTICLES_COLUMN_CREATED_AT, article.getCreateAtTime().getTime());

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
    public void insertAllGroups(ArticleCategory[] categories) {
        for(int i=0;i <categories.length;i++){
            ContentValues values = new ContentValues();
            values.put(GROUPS_COLUMN_TITLE, categories[i].getTitle());
            values.put(COLUMN_ID, categories[i].getId());

            getContentResolver()
                        .insert(AppContentProvider.CONTENT_URI_GROUPS, values);

        }
    }

    @Override
    public void synchronizeDB(ArrayList<Long> serverIdsList) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(ID_SERVER_LIST,serverIdsList);
        getLoaderManager().initLoader(ARTICLE_SYNC, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        switch (id){
            case ARTICLE_SYNC:
                cursorLoader = createSycnCursorLoader(args);
                break;
            case GROUP_LOADER:
                cursorLoader = createGroupCursorLoader(args);
                break;
            default:
                cursorLoader = createArticleCursorLoader(args);
                break;
        }


        return cursorLoader;
    }

    private CursorLoader createSycnCursorLoader(Bundle args) {
        return null;
    }

    private CursorLoader createArticleCursorLoader(Bundle args) {
        String[] projection = { COLUMN_ID,
                ARTICLES_COLUMN_TITLE,
                ARTICLES_COLUMN_IS_MYOWN,
                ARTICLES_COLUMN_CREATED_AT
        };
        String orderBy = AppSQLiteOpenHelper.ARTICLES_COLUMN_UPDATED_AT+
                " DESC ";
                //+" DESC";
        String selection = null;
        String[] selectionARGS = null;
        if(args != null){
           selection = args.getString(SELECTION);
            selectionARGS = args.getStringArray(SELECTION_ARGS);
        }

        return new CursorLoader(this,
                AppContentProvider.CONTENT_URI_ARTICLES, projection, selection, selectionARGS, orderBy);
    }

    private CursorLoader createGroupCursorLoader(Bundle args) {
        String[] projection = { COLUMN_ID,
                GROUPS_COLUMN_TITLE
        };


        return new CursorLoader(this,
                AppContentProvider.CONTENT_URI_PATH_NOT_EMPTY, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       switch (loader.getId()){
           case ARTICLES_LOADER:
               getAdapter().swapCursor(data);
               break;
           case GROUP_LOADER:
               ExpandableAdapterDataLoadFinished(data);
               break;
       }
    }

    private void ExpandableAdapterDataLoadFinished(Cursor data) {

        getAdapterExp().setGroupCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getAdapter().swapCursor(null);
        switch (loader.getId()){
            case ARTICLES_LOADER:
                getAdapter().swapCursor(null);
                break;
            case GROUP_LOADER:
                getAdapterExp().changeCursor(null);
                break;
        }
    }


    public AdapterExpandableListArticle getAdapterExp() {
        return adapterExp;
    }

    public void setAdapterExp(AdapterExpandableListArticle adapterExp) {
        this.adapterExp = adapterExp;
    }

    public SimpleCursorAdapterListArticle getAdapter() {
        return adapter;
    }

    public void setAdapter(SimpleCursorAdapterListArticle adapter) {
        this.adapter = adapter;
    }
}
