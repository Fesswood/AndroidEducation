package info.goodline.starsandplanets.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.data.SpaceBody;
import info.goodline.starsandplanets.listener.ActivityItemStateListener;
import info.goodline.starsandplanets.listener.AdapterDataChangeListener;

/**
 * Created by fesswood on 14.06.15.
 */
public class BaseFragment extends Fragment implements AdapterView.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {

    protected ListView mChildListView;
    private AdapterDataChangeListener mAdapter;
    private ActivityItemStateListener mDeleteListItemCallbackListener;
    private int mCurrentPosition;
    private int mCurrentGroupPosition;
    private long mItemId;
    private SpaceBody mSelectedSpaceBody;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mChildListView != null && mChildListView instanceof ExpandableListView) {
            ExpandableListAdapter adapter = ((ExpandableListView) mChildListView).getExpandableListAdapter();

            mAdapter = (AdapterDataChangeListener) adapter;
        } else if (mChildListView != null) {
            mAdapter = (AdapterDataChangeListener) mChildListView.getAdapter();
        }
        attachClickListeners();
        attachDeleteListener();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter = null;
        mDeleteListItemCallbackListener = null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showPopupMenu(view);
        mItemId = id;
        if (mChildListView instanceof ExpandableListView) {
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                mCurrentGroupPosition = ExpandableListView.getPackedPositionGroup(id);
                mCurrentPosition = ExpandableListView.getPackedPositionChild(id);
                return true;
            }
        } else {
            mCurrentPosition = position;
            mCurrentGroupPosition = 0;
        }

        mSelectedSpaceBody = mAdapter.getItem(mCurrentGroupPosition, mCurrentPosition);
        return false;
    }

    private void attachDeleteListener() {
        if (getActivity() instanceof ActivityItemStateListener) {
            mDeleteListItemCallbackListener = (ActivityItemStateListener) getActivity();
        } else {
            throw new IllegalArgumentException("Activity must implements  DeleteListItemCallbackListener!");
        }
    }

    private void attachClickListeners() {
        if (mChildListView != null) {
            mChildListView.setOnItemLongClickListener(this);
        } else {
            throw new IllegalArgumentException("mChildListView is null!");
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        MenuInflater inflate = popupMenu.getMenuInflater();
        inflate.inflate(R.menu.list_item_menu, popupMenu.getMenu());
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.favorite);
        menuItem.setCheckable(true);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle(getActivity().getResources().getString(R.string.delete_question));
                adb.setMessage("Are you sure you want to delete " + mCurrentPosition);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.deleteItem(mSelectedSpaceBody);
                        mDeleteListItemCallbackListener.deleteFromOtherList(mSelectedSpaceBody);
                    }
                });
                adb.show();
                return true;
            case R.id.favorite:
                menuItem.setChecked(true);
                mAdapter.setItemFavorite(mSelectedSpaceBody);
                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }
}
