package info.goodline.funnycounters.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import info.goodline.funnycounters.R;
import info.goodline.funnycounters.fragment.BlinkingFragment;
import info.goodline.funnycounters.fragment.EmptyFragment;
import info.goodline.funnycounters.fragment.JumpingFragment;
import info.goodline.funnycounters.fragment.RotatingFragment;
import info.goodline.funnycounters.fragment.RunningFragment;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int FRAGMENT_TYPE_REQUEST = 0;
    public static final String FRAGMENT_ID_TAG = "MainActivity.FRAGMENT_ID_TAG";
    Button mClearFragmentsButton;
    Button mClearCountersButton;
    ArrayList<FrameLayout> mFrameLayoutsList;
    /**
     *  Array to mapping FrameView id to Fragment id.
     *  It uses for save state app.
     */
    SparseArray<Integer>  mFrameToFragmentArray;
    int mSelectedFrame=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClearCountersButton  = (Button) findViewById(R.id.clear_fragments_button);
        mClearFragmentsButton = (Button) findViewById(R.id.clear_counters_button);
        LinearLayout column1 = (LinearLayout) findViewById(R.id.column_1);
        LinearLayout column2 = (LinearLayout) findViewById(R.id.column_2);
        mFrameLayoutsList = new ArrayList();
        mFrameToFragmentArray = new SparseArray<>();
        findAllFrames(column1);
        findAllFrames(column2);
        addClickListener();
    }
    private void findAllFrames(LinearLayout column) {
        int count = column.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = column.getChildAt(i);
            mFrameLayoutsList.add((FrameLayout) view);
        }
    }
    private void addClickListener() {
        mClearCountersButton.setOnClickListener(this);
        mClearFragmentsButton.setOnClickListener(this);
       for(FrameLayout frameLayout:mFrameLayoutsList){
           frameLayout.setOnClickListener(this);
       }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_counters_button:
                clearAllCounters();
                break;
            case R.id.clear_fragments_button:
                removeAllFragments();
            default:
                startActivityForResult(
                    new Intent(this,ChooseFragmentActivity.class),
                    FRAGMENT_TYPE_REQUEST);
                mSelectedFrame=v.getId();
                break;
        }
    }

    private void removeAllFragments() {
        for(CounterListener listener: mListeners){
            listener.deleteCounter();
            getFragmentManager().beginTransaction().remove((Fragment) listener).commit();
        }
        mListeners.clear();
        mFrameToFragmentArray.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FRAGMENT_TYPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                int receivedData=data.getIntExtra(FRAGMENT_ID_TAG,0);
               if(receivedData != 0){
                   attachCurrentFragment(receivedData);
                   mFrameToFragmentArray.append(mSelectedFrame,receivedData);
               }
            }
        }

    }

    private void attachCurrentFragment(int receivedData) {
        Fragment selectedFragment = getSelectedFragment(receivedData);
        getFragmentManager().beginTransaction()
                .add(mSelectedFrame, selectedFragment)
                .commit();
        this.mListeners.add((CounterListener) selectedFragment);
    }

    private Fragment getSelectedFragment(int receivedData) {
        switch (receivedData){
            case 1:
                return RunningFragment.newInstance();

            case 2:
                return RotatingFragment.newInstance();

            case 3:
                return JumpingFragment.newInstance();

            case 4:
                return BlinkingFragment.newInstance();

            default:
                return EmptyFragment.newInstance("1", "2");

        }
    }

}
