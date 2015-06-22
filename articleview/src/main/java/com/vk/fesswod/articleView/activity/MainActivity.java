package com.vk.fesswod.articleView.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.fragment.FragmentArticleDisplayListener;
import com.vk.fesswod.articleView.fragment.FragmentArticleList;
import com.vk.fesswod.articleView.fragment.FragmentListDisplayListener;


public class MainActivity extends BaseActivity implements FragmentArticleList.FragmentInteractionListener {

    private FragmentArticleDisplayListener mArticleDisplayListener;
    private FragmentListDisplayListener mListDisplayLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            mListDisplayLister = (FragmentListDisplayListener) getSupportFragmentManager()
                    .findFragmentById(R.id.list_article_fragment);
            mListDisplayLister.setAdapter(getAdapter(), getAdapterExp());
            mArticleDisplayListener = (FragmentArticleDisplayListener) getSupportFragmentManager()
                    .findFragmentById(R.id.article_fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(long id, int fragmentId) {
        switch (fragmentId){
            case R.id.list_article_fragment:
                mArticleDisplayListener.showArticle(id);
                break;
            case R.id.article_fragment:
                mListDisplayLister.updateListWithItem(id);
                break;
        }
    }

    @Override
    public void showSnackBar(int stringResource, int scnakbarActionString, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.linear_layout_root), stringResource, Snackbar.LENGTH_LONG);
        if(listener != null){
            snackbar.setAction(scnakbarActionString, listener);
        }
        snackbar.show(); // Don’t forget to show!
    }

}
