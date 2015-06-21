package com.vk.fesswod.articleView.activity;

/**
 * This interface must be implemented by {@link BaseActivity}
 * Entry of this interface is used by FragmentArticleList for activating filters of list
 */
public interface ChangeFilterClauseListener{
    void initFilter(boolean mIsOnlyMyFilter,boolean misUnpublishedFilter,CharSequence mKeyword);
}
