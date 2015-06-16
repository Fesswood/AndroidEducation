package info.goodline.starsandplanets.listener;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item
 * selections.
 */
public interface ActivityItemStateListener {
    /**
     * Callback for when an item has been selected.
     */
    void onItemSelected(int pos);

    /**
     * Callback for when an item has been set as favorite.
     */
    void changeStateFavoriteItem(SpaceBody favoriteSpaceBody, boolean isChecked);

    /**
     * Callback for deleting items from other list
     *
     * @param id
     */
    void deleteFromOtherList(SpaceBody id);
}