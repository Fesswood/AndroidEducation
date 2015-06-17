package com.vk.fesswod.articleView.activity;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.fragment.FragmentArticleDetail;
import com.vk.fesswod.articleView.fragment.FragmentArticleList;


public class MainActivity extends BaseActivity implements FragmentArticleList.OnFragmentInteractionListener,FragmentArticleDetail.FragmentArticleStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentArticleList listfragment = (FragmentArticleList) getSupportFragmentManager()
                .findFragmentById(R.id.list_article_fragment);
        FragmentArticleDetail expListFragment = ((FragmentArticleDetail) getSupportFragmentManager()
                .findFragmentById(R.id.article_fragment));
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
    public void onFragmentInteraction(Article article) {

    }

    @Override
    public void onArticleChangesListener(Uri uri) {

    }
}
