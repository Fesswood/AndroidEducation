package com.vk.fesswod.articleView.activity;

import com.vk.fesswod.articleView.data.Article;

/**
 * Created by sergeyb on 16.06.15.
 */
public interface DataStateChangeListener {
    boolean delete(long id);
    boolean update(Article article);
    Article create(Article article);
}
