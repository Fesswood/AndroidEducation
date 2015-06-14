package info.goodline.starsandplanets.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.fragments.FragmentSpaceBodyDetail;


/**
 * An activity representing a single spaceBody detail screen. This
 * activity is only used on handset devices. On landscape orientation,
 * item details are presented side-by-side with a list of items
 * in a {@link ActivitySpaceBodyList}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link FragmentSpaceBodyDetail}.
 */
public class ActivitySpaceBodyDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacebody_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(FragmentSpaceBodyDetail.ARG_ITEM_ID,
                    getIntent().getStringExtra(FragmentSpaceBodyDetail.ARG_ITEM_ID));
            FragmentSpaceBodyDetail fragment = new FragmentSpaceBodyDetail();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.spacebody_detail_container, fragment)
                    .commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ActivitySpaceBodyList.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
