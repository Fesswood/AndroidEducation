package com.vk.fesswod.articleView.fragment;

import com.vk.fesswod.articleView.adapter.AdapterExpandableListArticle;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.Article;

/**
 * Created by sergeyb on 17.06.15.
 */
public interface FragmentListDisplayListener {
   void updateListWithItem(long article);
   void setAdapter(SimpleCursorAdapterListArticle adapter, AdapterExpandableListArticle adapterExp);
}
