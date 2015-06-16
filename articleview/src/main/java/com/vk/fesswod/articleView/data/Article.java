package com.vk.fesswod.articleView.data;

/**
 * Created by sergeyb on 16.06.15.
 */
public class Article {
    long id;
    String title;
    String desc;
    boolean isPublished;
    ArticleGroup mArticleGroup;
    long createAtTimeStamp;
    long updateAtTimeStamp;

    public Article(String title, String desc, ArticleGroup articleGroup, long createAtTimeStamp) {
        this.title = title;
        this.desc = desc;
        this.mArticleGroup = articleGroup;
        this.createAtTimeStamp = createAtTimeStamp;
    }

    public Article(long id, String title, String desc, boolean isPublished, ArticleGroup articleGroup, long createAtTimeStamp, long updateAtTimeStamp) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.isPublished = isPublished;
        this.mArticleGroup = articleGroup;
        this.createAtTimeStamp = createAtTimeStamp;
        this.updateAtTimeStamp = updateAtTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || (this.title == ((Article)o).getTitle() &&
                                   this.desc == ((Article)o).getDesc()   &&
                                   this.createAtTimeStamp == ((Article)o).getCreateAtTimeStamp() );
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public ArticleGroup getArticleGroup() {
        return mArticleGroup;
    }

    public void setArticleGroup(ArticleGroup articleGroup) {
        this.mArticleGroup = articleGroup;
    }

    public long getCreateAtTimeStamp() {
        return createAtTimeStamp;
    }

    public void setCreateAtTimeStamp(long createAtTimeStamp) {
        this.createAtTimeStamp = createAtTimeStamp;
    }

    public long getUpdateAtTimeStamp() {
        return updateAtTimeStamp;
    }

    public void setUpdateAtTimeStamp(long updateAtTimeStamp) {
        this.updateAtTimeStamp = updateAtTimeStamp;
    }
}
