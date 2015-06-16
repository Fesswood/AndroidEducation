package com.vk.fesswod.articleView.data;

/**
 * Created by sergeyb on 16.06.15.
 */
public class ArticleGroup {
    long id;
    String title;

    public ArticleGroup(String title) {
        this.title = title;
    }

    public ArticleGroup(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
