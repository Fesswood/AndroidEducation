package info.goodline.funnycounters.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
    protected volatile int mCounterValue = 0;
    private RegistratorListener mRegistratorListener;
    Handler mTimerHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimerHandler = new Handler();
        if(savedInstanceState != null){
            mCounterValue=savedInstanceState.getInt(TIMER_STATE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mRegistratorListener = ((RegistratorListener) activity);
        mRegistratorListener.registerListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIMER_STATE,mCounterValue);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimerHandler.removeCallbacks(null);
        mRegistratorListener.unregisterListener(this);
        mRegistratorListener = null;
    }

    public void setupTimer(final TextView counterTextView) {
        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (counterTextView != null) {
                    countTimer(counterTextView);
                }
                mTimerHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void clearCounter() {
        mCounterValue = 0;
    }

    @Override
    public int getCounter() {
        return mCounterValue;
    }

    @Override
    public void setCounter(int newValue) {
        mCounterValue = newValue;
    }

    @Override
    public void deleteCounter() {
        mTimerHandler.removeCallbacks(null);
    }

    public abstract void countTimer(TextView counterTextView);


    public interface RegistratorListener {
        void registerListener(BaseActivity.CounterListener counterListener);
        void unregisterListener(BaseActivity.CounterListener counterListener);
    }



}