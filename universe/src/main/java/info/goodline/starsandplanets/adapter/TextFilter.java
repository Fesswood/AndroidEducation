package info.goodline.starsandplanets.adapter;

import android.widget.BaseAdapter;
import android.widget.Filter;

import java.util.ArrayList;

import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by sergeyb on 16.06.15.
 */
public class TextFilter extends Filter {
    private ArrayList<SpaceBody> mAllSpaceBodyItemsArray = new ArrayList<>();
    private ArrayList<SpaceBody> filteredSpaceBodyItemsArray;
    private ArrayList<SpaceBody> mAdapterListItems;
    private BaseAdapter mBaseAdapter;

    public TextFilter(ArrayList<SpaceBody> adapterListItems,BaseAdapter baseAdapter) {
        mAllSpaceBodyItemsArray.addAll(adapterListItems);
        mAdapterListItems=adapterListItems;
        mBaseAdapter = baseAdapter;
    }

    @Override
        protected FilterResults performFiltering(CharSequence constraint) {

        constraint = constraint.toString().toLowerCase();
        FilterResults result = new FilterResults();
        if(constraint != null && constraint.toString().length() > 0)
        {
            ArrayList<SpaceBody> filteredItems = new ArrayList<SpaceBody>();

            for(int i = 0, l = mAllSpaceBodyItemsArray.size(); i < l; i++)
            {
                SpaceBody m = mAllSpaceBodyItemsArray.get(i);
                if(m.getName().toLowerCase().contains(constraint))
                    filteredItems.add(m);
            }
            result.count = filteredItems.size();
            result.values = filteredItems;
        }
        else
        {
            synchronized(this)
            {
                result.values = mAllSpaceBodyItemsArray;
                result.count = mAllSpaceBodyItemsArray.size();
            }
        }
        return result;
    }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        filteredSpaceBodyItemsArray = (ArrayList<SpaceBody>)results.values;
            mBaseAdapter.notifyDataSetChanged();
            mAdapterListItems.clear();
        for(int i = 0, l = filteredSpaceBodyItemsArray.size(); i < l; i++)
            mAdapterListItems.add(filteredSpaceBodyItemsArray.get(i));
            mBaseAdapter.notifyDataSetInvalidated();
    }

}