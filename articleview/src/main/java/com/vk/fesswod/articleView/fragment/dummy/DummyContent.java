package com.vk.fesswod.articleView.fragment.dummy;

import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Article> ITEMS = new ArrayList<Article>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Article> ITEM_MAP = new HashMap<String, Article>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static HashSet<String> GROUP_SET = new  HashSet<String>();


    static {
        // Add 3 or little bit more sample items.
        addItem(new Article("Test","Desc",new ArticleGroup("Group1"), 13l));
        addItem(new Article("Test1","Desc",new ArticleGroup("Group2"), 12l));
        addItem(new Article("Test2","Desc",new ArticleGroup("Group3"), 11l));
        addItem(new Article("Test3","Desc",new ArticleGroup("Group4"), 10l));
        addItem(new Article("Test4","Desc",new ArticleGroup("Group5"), 9l));
        addItem(new Article("Test5","Desc",new ArticleGroup("Group6"), 8l));
        addItem(new Article("Test6","Desc",new ArticleGroup("Group1"), 7l));
        addItem(new Article("Test7","Desc",new ArticleGroup("Group2"), 6l));
        addItem(new Article("Test8","Desc",new ArticleGroup("Group3"), 5l));
        addItem(new Article("Test9","Desc",new ArticleGroup("Group3"), 4l));
        addItem(new Article("Test0","Desc",new ArticleGroup("Group3"), 3l));
        addItem(new Article("Test-","Desc",new ArticleGroup("Group4"), 2l));
        addItem(new Article("Test=","Desc",new ArticleGroup("Group4"), 1l));
    }

    private static void addItem(Article item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getTitle(), item);
        GROUP_SET.add(item.getArticleGroup().getTitle());

    }


}
