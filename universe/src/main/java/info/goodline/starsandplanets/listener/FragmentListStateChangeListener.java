package info.goodline.starsandplanets.listener;

import android.support.annotation.Nullable;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by sergeyb on 11.06.15.
 */
public interface FragmentListStateChangeListener {
    void changeViewVisibility(int visibility);

    void setCurrentItem(int position, @Nullable SpaceBody spaceBody);

    void deleteItem(SpaceBody id);

    void changeStateFavoriteItem(SpaceBody favoriteSpaceBody, boolean isChecked);
}
