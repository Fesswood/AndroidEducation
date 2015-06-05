package info.goodline.funnycounters.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import info.goodline.funnycounters.fragment.BaseFragment;

public class BaseActivity extends Activity {

    protected ArrayList<CounterListener> mListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListeners=new ArrayList<>();
    }
    protected void clearAllCounters(){
        for(CounterListener counterListener: mListeners){
            counterListener.clearCounter();
        }
    }
    public void registerCounterListener(CounterListener counterListener){
        mListeners.add(counterListener);
    }
    public void unRegisterCounterListener(CounterListener counterListener){
        mListeners.remove(counterListener);
    }

    public interface CounterListener {
       void clearCounter();
       void deleteCounter();
       int getCounter();
       void setCounter(int newValue);
   }

}
