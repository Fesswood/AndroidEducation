package info.goodline.starsandplanets.activity;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by sergeyb on 11.06.15.
 */
public interface ListStateChangeListener {
    void changeViewVisibility(int visibility);
    void setCurrentItem(int position);
    void deleteItem(SpaceBody id);
    void sendFavoriteState(SpaceBody favoriteSpaceBody);
}
