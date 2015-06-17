package com.vk.fesswod.articleView.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.Article;
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
        mListDisplayLister = (FragmentListDisplayListener) getSupportFragmentManager()
                .findFragmentById(R.id.list_article_fragment);
         mArticleDisplayListener = (FragmentArticleDisplayListener) getSupportFragmentManager()
                .findFragmentById(R.id.article_fragment);
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
    public void onFragmentInteraction(Article article, int fragmentId) {
        switch (fragmentId){
            case R.id.list_article_fragment:
                mArticleDisplayListener.displayArticle(article);
                break;
            case R.id.article_fragment:
                mListDisplayLister.updateListWithItem(article);
                break;
        }
    }

}
