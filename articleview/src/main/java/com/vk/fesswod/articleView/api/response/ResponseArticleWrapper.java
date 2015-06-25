package com.vk.fesswod.articleView.api.response;

import com.vk.fesswod.articleView.data.Article;

/**
 * Created by fesswood on 23.06.15.
 */
public class ResponseArticleWrapper {
    private  Article article;

    public ResponseArticleWrapper() {
    }

    public ResponseArticleWrapper(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}
