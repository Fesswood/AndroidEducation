package info.goodline.funnycounters.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import info.goodline.funnycounters.R;


public class RunningFragment extends BaseFragment {

    private TextView  mCounterTextView;
    private ImageView mCatView;

    public static RunningFragment newInstance() {
        RunningFragment fragment = new RunningFragment();
        return fragment;
    }

    public RunningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_running, container, false);
        mCatView = (ImageView) inflateView.findViewById(R.id.running_image_view);
        mCounterTextView = (TextView) inflateView.findViewById(R.id.counter_text_view);

        setupTimer(mCounterTextView);
        return inflateView;
    }
    @Override
    public void countTimer(TextView counterTextView) {
        counterTextView.setText(String.valueOf(mCounterValue));
        int subvalue = (mCounterValue+1) * 10 ;
        setupAnimation(mCatView, toSmallRange(mCounterValue*10), toSmallRange(subvalue));
        mCounterValue++;

    }

    public float toSmallRange(int value){
        return ((float)value%100)/100f;
    }

    private void setupAnimation(ImageView catView,float fromXPlot, float toXPlot) {

        Log.d("tag",""+catView.getRight());

        Log.d("tag",""+catView.getLeft());
        TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (-0.5f+fromXPlot),
                Animation.RELATIVE_TO_PARENT,(-0.5f+toXPlot),
                Animation.RELATIVE_TO_PARENT,0f,
                Animation.RELATIVE_TO_PARENT,0f);
        an.setRepeatCount(1);
        an.setDuration(1000);
        an.setFillAfter(true);
        catView.setAnimation(an);
    }

}
