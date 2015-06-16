package info.goodline.starsandplanets.fragment;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.adapter.SpaceBodyListAdapter;
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
public class FragmentSpaceBodyList extends BaseFragment implements FragmentListStateChangeListener, AdapterView.OnItemClickListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on landscape orientation.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * A dummy implementation of the {@link ActivityItemStateListener} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static ActivityItemStateListener sDummyActivityItemStateListener = new ActivityItemStateListener() {
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
    private ActivityItemStateListener mActivityItemStateListener = sDummyActivityItemStateListener;
    /**
     * The current activated item position. Only used on landscape orientation.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private SpaceBodyListAdapter mAdapter;
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
        mAdapter = new SpaceBodyListAdapter(getActivity(), this);
        // TODO: replace with a real list adapter.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spacebody_list, null);
        mListView = (ListView) v.findViewById(R.id.spacebody_list_view);
        mListView.setAdapter(mAdapter);
        super.mChildListView = mListView;
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
        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        mListView.setLayoutTransition(new LayoutTransition());
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
        // Reset the active callbacks interface
        mActivityItemStateListener = null;
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
        //mListView.setChoiceMode(activateOnItemClick
        //        ? ListView.CHOICE_MODE_SINGLE
       //         : ListView.CHOICE_MODE_NONE);
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
    public void setCurrentItem(int position, @Nullable SpaceBody spaceBody) {
        mListView.setItemChecked(position, true);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        setActivatedPosition(position);
        mActivityItemStateListener.onItemSelected(position);
    }
}
