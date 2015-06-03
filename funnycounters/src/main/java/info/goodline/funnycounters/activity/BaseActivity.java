package info.goodline.funnycounters.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

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

   public interface CounterListener {
       void clearCounter();
       void deleteCounter();
       int getCounter();
       void setCounter(int newValue);
   }

}
