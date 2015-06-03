package info.goodline.funnycounters.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.goodline.funnycounters.R;
import info.goodline.funnycounters.fragment.BaseFragment;
import info.goodline.funnycounters.fragment.BlinkingFragment;
import info.goodline.funnycounters.fragment.EmptyFragment;
import info.goodline.funnycounters.fragment.JumpingFragment;
import info.goodline.funnycounters.fragment.RotatingFragment;
import info.goodline.funnycounters.fragment.RunningFragment;
import info.goodline.funnycounters.util.SerializableSparseArray;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int FRAGMENT_TYPE_REQUEST = 0;
    public static final String FRAGMENT_ID_TAG = "MainActivity.FRAGMENT_ID_TAG";
    private static final String FRAME_TO_FRAGMENT_ARRAY_KEY_STATE = "MainActivity.FRAME_TO_FRAGMENT_ARRAY_KEY_STATE";
    private static final String COUNTERS_VALUES = "MainActivity.COUNTERS_VALUES";
    Button mClearFragmentsButton;
    Button mClearCountersButton;
    ArrayList<FrameLayout> mFrameLayoutsList;
    /**
     *  Array to mapping FrameView id to Fragment id.
     *  It uses for save state app.
     */
    HashMap<Integer,Integer> mFrameToFragmentMap;
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
        mFrameToFragmentMap = new HashMap<>();
        findAllFrames(column1);
        findAllFrames(column2);

        restoreFragmentsState(savedInstanceState);
        addClickListener();
    }

    private void restoreFragmentsState(Bundle savedInstanceState) {
        if(savedInstanceState!= null){
            mFrameToFragmentMap =(HashMap)  savedInstanceState.getSerializable(FRAME_TO_FRAGMENT_ARRAY_KEY_STATE);
            int[] countersValues = savedInstanceState.getIntArray(COUNTERS_VALUES);
            int  fragmentNumber;
            for(Map.Entry<Integer,Integer> entry:mFrameToFragmentMap.entrySet()){
                mSelectedFrame = entry.getKey();
                // get the object by the key.
                fragmentNumber = entry.getValue();
                attachCurrentFragment(fragmentNumber);
            }
            for(int i = 0; i < mListeners.size(); i++) {
                 mListeners.get(i).setCounter(countersValues[i]);
            }
        }
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
        mFrameToFragmentMap.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FRAGMENT_TYPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                int receivedData=data.getIntExtra(FRAGMENT_ID_TAG,0);
               if(receivedData != 0){
                   attachCurrentFragment(receivedData);
                   mFrameToFragmentMap.put(mSelectedFrame, receivedData);

               }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FRAME_TO_FRAGMENT_ARRAY_KEY_STATE, mFrameToFragmentMap);
        int[] counter = new int[mListeners.size()];
        for(int i=0;i<mListeners.size();i++){
            counter[i]=mListeners.get(i).getCounter();
        }
        outState.putIntArray(COUNTERS_VALUES, counter);
    }

    private void attachCurrentFragment(int selectedFragmentNumber) {
        Fragment selectedFragment = getSelectedFragment(selectedFragmentNumber);
        getFragmentManager().beginTransaction()
                .replace(mSelectedFrame, selectedFragment)
                .commit();
        this.mListeners.add((CounterListener) selectedFragment);
    }

    private Fragment getSelectedFragment(int receivedData) {
        BaseFragment fragment;
        switch (receivedData){
            case 1:
                fragment = RunningFragment.newInstance();
                break;

            case 2:
                fragment = RotatingFragment.newInstance();
                break;

            case 3:
                fragment = JumpingFragment.newInstance();
                break;

            case 4:
                fragment = BlinkingFragment.newInstance();
                break;

            default:
                fragment = EmptyFragment.newInstance();
                break;
        }
        return fragment;
    }

}
