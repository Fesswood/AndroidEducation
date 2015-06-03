package info.goodline.funnycounters.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import info.goodline.funnycounters.R;


public class BlinkingFragment extends BaseFragment {

    private TextView mCounterTextView;
    private FrameLayout mFrame_layout;

    public static BlinkingFragment newInstance() {
        BlinkingFragment fragment = new BlinkingFragment();

        return fragment;
    }

    public BlinkingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void countTimer(TextView counterTextView) {
        counterTextView.setText(String.valueOf(mCounterValue));
        mFrame_layout.setBackgroundColor(Color.rgb(mCounterValue * 10 % 255, mCounterValue * 10 % 255, mCounterValue * 10 % 255));
        mCounterValue++;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_blinking, container, false);
        ImageView catView = (ImageView) inflateView.findViewById(R.id.blinking_image_view);
        mFrame_layout = (FrameLayout) inflateView.findViewById(R.id.frame_layout);
        mCounterTextView = (TextView) inflateView.findViewById(R.id.counter_text_view);
        setupAnimation(catView);
        setupTimer(mCounterTextView);
        return inflateView;
    }

    private void setupAnimation(ImageView catView) {
        Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.tween);
        catView.setAnimation(an);

    }


}
