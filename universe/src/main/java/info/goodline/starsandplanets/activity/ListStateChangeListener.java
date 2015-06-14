package info.goodline.starsandplanets.activity;

/**
 * Created by sergeyb on 11.06.15.
 */
public interface ListStateChangeListener {
    void changeViewVisibility(int visibility);
    void setCurrentItem(int position);
    void deleteItem(int position);
}
