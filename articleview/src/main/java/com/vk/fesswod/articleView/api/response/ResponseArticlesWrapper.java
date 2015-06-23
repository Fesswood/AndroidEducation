package com.vk.fesswod.articleView.api.response;

import com.vk.fesswod.articleView.data.Article;

/**
 *  Wrapper for article received from server
 */
public class ResponseArticlesWrapper {
    private Article[] articles;

    public ResponseArticlesWrapper(Article[] articles) {
        this.setArticles(articles);
    }

    public ResponseArticlesWrapper() {

    }

    public Article[] getArticles() {
        return articles;
    }

    public void setArticles(Article[] articles) {
        this.articles = articles;
    }
}