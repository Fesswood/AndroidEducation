package com.vk.fesswod.articleView.api.response;

import com.vk.fesswod.articleView.data.Article;

/**
 *  Wrapper for article received from server
 */
public class ArticleContainer {
    public Article[] articles;

    public ArticleContainer(Article[] articles) {
        this.articles = articles;
    }

    public ArticleContainer() {

    }
}