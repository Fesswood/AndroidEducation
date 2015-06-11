package info.goodline.starsandplanets.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.fragments.Callbacks;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyDetail;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyExpandableList;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyList;


/**
 * An activity representing a list of spaceBodies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActivitySpaceBodyDetail} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link FragmentSpaceBodyList} and the item details
 * (if present) is a {@link FragmentSpaceBodyDetail}.
 * <p>
 * This activity also implements the required
 * {@link Callbacks} interface
 * to listen for item selections.
 */
public class ActivitySpaceBodyList extends FragmentActivity
        implements Callbacks, AdapterView.OnItemSelectedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device or landscape orientation.
     */
    private boolean mTwoPane;
    private Spinner mSpinner;
    private boolean mIsExpandableList=false;
    private VisibilityChangeCallback mListVisibilityCallback;
    private VisibilityChangeCallback mExpListVisibilityCallback;
    private ViewPagerCallback mViewPagerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacebody_list);


        mSpinner = (Spinner) findViewById(R.id.spinnerList);
        if (mSpinner != null) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_values, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setAdapter(adapter);
        }

        if (findViewById(R.id.spacebody_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            mViewPagerCallback = (ViewPagerCallback) getSupportFragmentManager().findFragmentById(R.id.spacebody_detail_container);
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            FragmentSpaceBodyList listfragment = (FragmentSpaceBodyList) getSupportFragmentManager()
                    .findFragmentById(R.id.spacebody_list);
            listfragment.setActivateOnItemClick(true);
            FragmentSpaceBodyExpandableList expListFragment = ((FragmentSpaceBodyExpandableList) getSupportFragmentManager()
                    .findFragmentById(R.id.spacebody_expandable_list));
            expListFragment.setActivateOnItemClick(true);
            mListVisibilityCallback = (VisibilityChangeCallback)listfragment;
            mExpListVisibilityCallback = (VisibilityChangeCallback)expListFragment;
            expListFragment.getView().setVisibility(View.GONE);
        }


        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(FragmentSpaceBodyDetail.ARG_ITEM_ID, id);
            FragmentSpaceBodyDetail fragment = new FragmentSpaceBodyDetail();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.spacebody_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ActivitySpaceBodyDetail.class);
            detailIntent.putExtra(FragmentSpaceBodyDetail.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      switch (position){
          case 0:
              if(mIsExpandableList){
                  mListVisibilityCallback.changeViewVisibility(View.VISIBLE);
                  mExpListVisibilityCallback.changeViewVisibility(View.GONE);
                  mIsExpandableList=false;
              }
              break;
          case 1:
              if(!mIsExpandableList){
                  mExpListVisibilityCallback.changeViewVisibility(View.VISIBLE);
                  mListVisibilityCallback.changeViewVisibility(View.GONE);
                  mIsExpandableList=true;
              }
              break;
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
