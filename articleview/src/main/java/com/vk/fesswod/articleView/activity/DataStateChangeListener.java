package com.vk.fesswod.articleView.activity;

import android.net.Uri;

import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleCategory;

import java.util.ArrayList;

/**
 * Created by sergeyb on 16.06.15.
 */
public interface DataStateChangeListener {
    boolean delete(long id);
    boolean update(Article article);
    Uri insert(Article article);
    void insertAllGroups(ArticleCategory[] categories);
    void synchronizeDB(ArrayList<Long> articleContainer);
}
