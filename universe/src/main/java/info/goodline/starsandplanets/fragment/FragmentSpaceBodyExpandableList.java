package info.goodline.starsandplanets.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.adapter.SpaceBodyExpandableListAdapter;
import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.listener.ActivityItemStateListener;
import info.goodline.starsandplanets.listener.FragmentListStateChangeListener;

/**
 * A list fragment representing a list of spaceBodies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FragmentSpaceBodyDetail}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ActivityItemStateListener}
 * interface.
 */
public class FragmentSpaceBodyExpandableList extends BaseFragment implements FragmentListStateChangeListener, ExpandableListView.OnChildClickListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on landscape orientation.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * A implementation of the {@link ActivityItemStateListener} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static ActivityItemStateListener sActivityItemStateListener = new ActivityItemStateListener() {
        @Override
        public void onItemSelected(int id) {
        }

        @Override
        public void changeStateFavoriteItem(SpaceBody favoriteSpaceBody, boolean isChecked) {

        }

        @Override
        public void deleteFromOtherList(SpaceBody id) {

        }
    };
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private ActivityItemStateListener mActivityItemStateListener = sActivityItemStateListener;
    /**
     * The current activated item position. Only used on landscape orientation.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private SpaceBodyExpandableListAdapter mAdapter;
    private ExpandableListView mExpandableView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentSpaceBodyExpandableList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SpaceBodyExpandableListAdapter(getActivity());

        // TODO: replace with a real list adapter.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spacebody_expandable_list, null);
        mExpandableView = (ExpandableListView) v.findViewById(R.id.spacebody_expandable_list_view);
        mExpandableView.setAdapter(mAdapter);
        mExpandableView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mExpandableView.setOnChildClickListener(this);
        super.mChildListView = mExpandableView;
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ActivityItemStateListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mActivityItemStateListener = (ActivityItemStateListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mActivityItemStateListener = sActivityItemStateListener;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mExpandableView.setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mExpandableView.setItemChecked(mActivatedPosition, false);
        } else {
            mExpandableView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    /**
     * Switch visibility of listview when activity's spinner change item
     */
    @Override
    public void changeViewVisibility(int visibility) {
        getView().setVisibility(visibility);
    }

    @Override
    public void setCurrentItem(int position, SpaceBody item) {
        int[] itemPosition = mAdapter.getItemPosition(item);
        if (itemPosition != null) {
            mExpandableView.setSelectedChild(itemPosition[1],itemPosition[0],true);
        } else {
            Toast.makeText(getActivity(), R.string.bad_index, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteItem(SpaceBody id) {
        mAdapter.deleteItem(id);
    }

    @Override
    public void changeStateFavoriteItem(SpaceBody favoriteSpaceBody, boolean isChecked) {
        mActivityItemStateListener.changeStateFavoriteItem(favoriteSpaceBody, isChecked);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mActivityItemStateListener.onItemSelected(mAdapter.getOffset()[groupPosition] + childPosition);
        return false;
    }
}
