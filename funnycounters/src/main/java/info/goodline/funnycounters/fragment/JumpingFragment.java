package info.goodline.funnycounters.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import info.goodline.funnycounters.R;

public class JumpingFragment extends BaseFragment {
    private TextView mCounterTextView;
    private ProgressBar mProgressBar;

    public static JumpingFragment newInstance() {
        JumpingFragment fragment = new JumpingFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public JumpingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void countTimer(TextView counterTextView) {
        counterTextView.setText(String.valueOf(mCounterValue));
        mProgressBar.setProgress(mCounterValue%100);
        mCounterValue++;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_jumping, container, false);
        ImageView catView = (ImageView) inflateView.findViewById(R.id.jumping_image_view);
        mCounterTextView = (TextView) inflateView.findViewById(R.id.counter_text_view);
        mProgressBar = (ProgressBar) inflateView.findViewById(R.id.progressBar);
        setupAnimation(catView);
        setupTimer(mCounterTextView);
        return inflateView;
    }

    private void setupAnimation(ImageView catView) {
        TranslateAnimation an = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT,0f,
                Animation.RELATIVE_TO_PARENT,-1f,
                Animation.RELATIVE_TO_PARENT,0.6f);
        an.setRepeatCount(-1);
        an.setInterpolator(new BounceInterpolator());
        an.setDuration(3500);
        catView.setAnimation(an);
    }

}
