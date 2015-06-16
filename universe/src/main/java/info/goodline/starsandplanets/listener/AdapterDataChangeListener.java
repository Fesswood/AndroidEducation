package info.goodline.starsandplanets.listener;

import info.goodline.starsandplanets.data.SpaceBody;


/**
 * A callback interface that all fragments have used in this BaseFragment must
 * implement. This mechanism allows BaseFragment notify adapters for start changing item state
 */
public interface AdapterDataChangeListener {
    void deleteItem(SpaceBody id);

    SpaceBody getItem(int group, int position);

    void setItemFavorite(SpaceBody selectedSpaceBody);
}
