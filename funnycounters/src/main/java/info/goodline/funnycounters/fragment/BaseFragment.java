package info.goodline.funnycounters.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import info.goodline.funnycounters.activity.BaseActivity;

/**
 * Created by sergeyb on 02.06.15.
 */
public class BaseFragment extends Fragment implements BaseActivity.CounterListener {

    private Timer mFragmentTimer;
    private volatile int mCounterValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentTimer=new Timer();
        mCounterValue=0;
    }

    public void setupTimer(final TextView counterTextView){
        mFragmentTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counterTextView.setText("" + mCounterValue);
                        mCounterValue++;
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
    public void deleteCounter() {
        mFragmentTimer.cancel();
        mFragmentTimer.purge();
        mFragmentTimer=null;
    }
}
