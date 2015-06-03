package info.goodline.funnycounters.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import info.goodline.funnycounters.activity.BaseActivity;

/**
 * Created by sergeyb on 02.06.15.
 */
public abstract class BaseFragment extends Fragment implements BaseActivity.CounterListener {

    private static final String TIMER_STATE = "BaseFragment.TIMER_STATE";
    private Timer mFragmentTimer;
    protected volatile int mCounterValue=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentTimer=new Timer();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentTimer.cancel();
        mFragmentTimer.purge();
        mFragmentTimer=null;
    }

    public void setupTimer(final TextView counterTextView){
        mFragmentTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(counterTextView!= null){
                            countTimer(counterTextView);
                        }
                    }
                });
            }
        }, 1000, 1000);
    }
    @Override
    public void clearCounter() {
        mCounterValue=0;
    }

    @Override
    public int getCounter() {
        return mCounterValue;
    }
    @Override
    public void setCounter(int newValue) {
        mCounterValue=newValue;
    }

    @Override
    public void deleteCounter() {
        mFragmentTimer.cancel();
        mFragmentTimer.purge();
        mFragmentTimer=null;
    }
    public abstract void countTimer(TextView counterTextView);
}
