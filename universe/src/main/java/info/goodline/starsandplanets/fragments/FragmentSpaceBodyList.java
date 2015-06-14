package info.goodline.starsandplanets.fragments;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.activity.ListStateChangeListener;
import info.goodline.starsandplanets.adapter.SpaceBodyListAdapter;

/**
 * A list fragment representing a list of spaceBodies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FragmentSpaceBodyDetail}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FragmentSpaceBodyList extends BaseFragment implements ListStateChangeListener, AdapterView.OnItemClickListener {

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
    private SpaceBodyListAdapter mAdapter;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };
    private ListView mListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentSpaceBodyList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SpaceBodyListAdapter(getActivity());
        // TODO: replace with a real list adapter.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spacebody_list, null);
        mListView = (ListView) v.findViewById(R.id.spacebody_list_view);
        mListView.setAdapter(mAdapter);
        super.listViewWithContextMenu=mListView;
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableTransitionAnimation();
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(this);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void enableTransitionAnimation() {
        LayoutTransition lt=new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
       mListView.setLayoutTransition(new LayoutTransition());
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
       mListView.setChoiceMode(activateOnItemClick
               ? ListView.CHOICE_MODE_SINGLE
               : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
           mListView.setItemChecked(mActivatedPosition, false);
        } else {
           mListView.setItemChecked(position, true);
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
    public void setCurrentItem(int position) {
       mListView.setItemChecked(position,true);
    }

    @Override
    public void deleteItem(int position) {
        mAdapter.deleteItem(position,0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mCallbacks.onItemSelected(i);
    }
}
