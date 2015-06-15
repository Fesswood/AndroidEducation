package info.goodline.starsandplanets.adapter;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by fesswood on 14.06.15.
 */
public interface AdapterDataChangeListener {
   void deleteItem(SpaceBody id);
   SpaceBody getItem(int group,int position);
   void setItemFavorite(SpaceBody selectedSpaceBody);
}
