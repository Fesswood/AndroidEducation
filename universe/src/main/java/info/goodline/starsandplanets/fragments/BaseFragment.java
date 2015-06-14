package info.goodline.starsandplanets.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.adapter.AdapterDataChangeListener;
import info.goodline.starsandplanets.data.SpaceBody;

/**
 * Created by fesswood on 14.06.15.
 */
public class BaseFragment extends Fragment  implements Callbacks{

    protected ListView listViewWithContextMenu;
    private AdapterDataChangeListener mAdapter;
    private DeleteListItemCallbackListener mDeleteListItemCallbackListener;
    private int mCurrentPosition;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(listViewWithContextMenu!=null){
            registerForContextMenu(listViewWithContextMenu);
        }else{
            throw new IllegalArgumentException("listViewWithContextMenu is null!");
        }
        if(getActivity() instanceof DeleteListItemCallbackListener){
            mDeleteListItemCallbackListener=(DeleteListItemCallbackListener)getActivity();
        }else{
            throw new IllegalArgumentException("Activity must implements  DeleteListItemCallbackListener!");
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            SpaceBody obj = (SpaceBody) lv.getItemAtPosition(acmi.position);

            if(lv.getAdapter() instanceof  AdapterDataChangeListener){
                mAdapter = (AdapterDataChangeListener) lv.getAdapter();
            }else{
                throw new IllegalArgumentException("Adapter must implements AdapterDataChangeListener!");
            }
            String title = obj.getName();
            menu.setHeaderTitle(title);

            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.list_item_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                adb.setTitle(getActivity().getResources().getString(R.string.delete_question));
                adb.setMessage("Are you sure you want to delete " + mCurrentPosition);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.deleteItem(mCurrentPosition,0);
                        mDeleteListItemCallbackListener.deleteFromOtherList(mCurrentPosition);
                    }});
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(int pos) {
        mCurrentPosition=pos;
    }
}
