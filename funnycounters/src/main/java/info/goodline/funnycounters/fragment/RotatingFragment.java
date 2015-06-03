package info.goodline.funnycounters.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

import info.goodline.funnycounters.R;
import info.goodline.funnycounters.activity.BaseActivity;


public class RotatingFragment extends BaseFragment{

    private TextView mCounterTextView;
    private ImageView mCatView;
    private GridLayout mGridLayout;

    public static RotatingFragment newInstance() {
        RotatingFragment fragment = new RotatingFragment();

        return fragment;
    }

    public RotatingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_rotating, container, false);

        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = 60;
        param.width = 60;
        param.rightMargin = 5;
        param.topMargin = 5;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(0);
        param.rowSpec = GridLayout.spec(0);


        mCatView = (ImageView) inflateView.findViewById(R.id.rotate_image_view);
        mCatView.setLayoutParams(param);
        mCounterTextView = (TextView) inflateView.findViewById(R.id.counter_text_view);
        mGridLayout = (GridLayout) inflateView.findViewById(R.id.grid_layout);
        setupAnimation(mCatView);
        setupTimer(mCounterTextView);
        return inflateView;
    }

    private void setupAnimation(ImageView catView) {
        Animation an = new RotateAnimation(0.0f, 360.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        an.setRepeatCount(-1);
        an.setDuration(1000);
        catView.setAnimation(an);
    }


    @Override
    public void countTimer(TextView counterTextView) {
        counterTextView.setText(String.valueOf(mCounterValue));
        changeGridCell();
        mCounterValue++;
    }

    private void changeGridCell() {
        mGridLayout.removeViewInLayout(mCatView);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = 60;
        param.width = 60;
        param.rightMargin = 5;
        param.topMargin = 5;
        param.setGravity(Gravity.CENTER);
        toGridСell(param);
        mGridLayout.addView(mCatView,param);
    }

    public void toGridСell(GridLayout.LayoutParams param){
        int index = mCounterValue%4;
        switch (index){
            case 0:
                param.columnSpec = GridLayout.spec(0);
                param.rowSpec = GridLayout.spec(0);
                break;
            case 1:
                param.columnSpec = GridLayout.spec(1);
                param.rowSpec = GridLayout.spec(0);
                break;
            case 2:
                param.columnSpec = GridLayout.spec(1);
                param.rowSpec = GridLayout.spec(1);
                break;
            case 3:
                param.columnSpec = GridLayout.spec(0);
                param.rowSpec = GridLayout.spec(1);
                break;
        }

    }

}
