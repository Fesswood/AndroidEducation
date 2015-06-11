package info.goodline.starsandplanets.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.activity.VisibilityChangeCallback;
import info.goodline.starsandplanets.adapter.SpaceBodyExpandableListAdapter;
import info.goodline.starsandplanets.adapter.SpaceBodyListAdapter;
import info.goodline.starsandplanets.data.DummyContent;

/**
 * A list fragment representing a list of spaceBodies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FragmentSpaceBodyDetail}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FragmentSpaceBodyExpandableList extends Fragment implements VisibilityChangeCallback {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on landscape orientation.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on landscape orientation.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private SpaceBodyExpandableListAdapter mAdapter;
    private ExpandableListView mExpandableView;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };

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
        View v = inflater.inflate(android.R.layout.expandable_list_content, null);
        mExpandableView = (ExpandableListView) v.findViewById(android.R.id.list);
        mExpandableView.setAdapter(mAdapter);
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
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
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
}
