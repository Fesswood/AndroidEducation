package com.vk.fesswod.articleView.api.request;

import com.vk.fesswod.articleView.data.Article;

/**
 *  Wrapper for article received from server
 */
public class ArticleArrayContainer {
    public Article[] articles;

    public ArticleArrayContainer(Article[] articles) {
        this.articles = articles;
    }

    public ArticleArrayContainer() {

    }
}