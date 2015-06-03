package info.goodline.funnycounters.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.goodline.funnycounters.R;


public class EmptyFragment extends BaseFragment {


    private LinearLayout mInvisLayout;
    private TextView mCounterTextView;
    private Button mButton;

    public static EmptyFragment newInstance() {
        EmptyFragment fragment = new EmptyFragment();
        return fragment;
    }

    public EmptyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void countTimer(TextView counterTextView) {
        counterTextView.setText(String.valueOf(mCounterValue));
        setTextSize();
        mCounterValue++;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_empty, container, false);

        mInvisLayout = (LinearLayout) inflateView.findViewById(R.id.invisible_frame_layout);
        mButton = (Button) inflateView.findViewById(R.id.press_textView);
        addTouchListener();
        mCounterTextView = (TextView) inflateView.findViewById(R.id.counter_text_view);

        setupTimer(mCounterTextView);
        return inflateView;
    }

    private void addTouchListener() {
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.tween_once);
                    an.setFillAfter(true);
                    mInvisLayout.setAnimation(an);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    an.setFillAfter(true);
                    mInvisLayout.setAnimation(an);
                    return true;
                }
                return false;
            }
        });
    }
    public void setTextSize(){
        mButton.setTextSize(mCounterValue%15);
    }

}
