package com.vk.fesswod.articleView.activity;


import android.support.v4.app.FragmentActivity;

import com.vk.fesswod.articleView.data.Article;

/**
 * Created by sergeyb on 16.06.15.
 */
public class BaseActivity extends FragmentActivity implements DataStateChangeListener {
    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Article article) {
        return false;
    }

    @Override
    public Article create(Article article) {
        return null;
    }
}
