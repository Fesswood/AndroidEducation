package com.vk.fesswod.articleView.api.response;

import com.vk.fesswod.articleView.data.ArticleCategory;

/**
 * Created by sergeyb on 23.06.15.
 */
public class ResponseCategoriesWrapper {
    public ArticleCategory[] categories;
    public ResponseCategoriesWrapper(ArticleCategory[] categories) {
        this.categories = categories;
    }
}