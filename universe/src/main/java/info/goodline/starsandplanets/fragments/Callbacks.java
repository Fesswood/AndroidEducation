package info.goodline.starsandplanets.fragments;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item
 * selections.
 */
public interface Callbacks {
    /**
     * Callback for when an item has been selected.
     */
    public void onItemSelected(int pos);

    void sendFavoriteSpaceBody(SpaceBody favoriteSpaceBody);
}