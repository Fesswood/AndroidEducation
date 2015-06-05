package info.goodline.imageswapper.fragment;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import info.goodline.imageswapper.R;
import info.goodline.imageswapper.activity.BaseBehavior;


public class ImageViewFragment extends Fragment implements BaseBehavior, View.OnClickListener {

    private static final String IMAGE_ID = "param1";
    private static final String FRAGMENT_NUMBER = "param2";

    private int imageID;
    private int fragmentNumber;
    private OnFragmentInteractionListener mListener;
    private ImageView mImageView;
    private LinearLayout mLinearLayout;

    public static ImageViewFragment newInstance(int imageId ,int fragmentNumber) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_ID, imageId);
        args.putInt(FRAGMENT_NUMBER, fragmentNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             imageID = getArguments().getInt(IMAGE_ID);
             fragmentNumber = getArguments().getInt(FRAGMENT_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLinearLayout = new LinearLayout(getActivity());
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLinearLayout.setPadding(10, 10, 10, 10);
        mImageView = new ImageView(getActivity());
        mImageView.setImageResource(getIdFromInt(imageID));
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mImageView.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
        mLinearLayout.addView(mImageView);
        return mLinearLayout;
    }

    private int getIdFromInt(int imageID) {
        switch (imageID){
            case 1: return R.drawable.img_1;
            case 2: return R.drawable.img_2;
            case 3: return R.drawable.img_3;
            case 4: return R.drawable.img_4;
            case 5: return R.drawable.img_5;
            default: return R.drawable.img_6;
        }
    }


    public void onFragmentSelected(int fragmentNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragmentNumber);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if( activity instanceof  OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) activity;
        }else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void changeScaleType(ImageView.ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    @Override
    public int getImageId() {
        return imageID;
    }

    @Override
    public int getNumber() {
        return fragmentNumber;
    }

    @Override
    public void setImageId(int imageId) {
        imageID=imageId;
        mImageView.setImageResource(getIdFromInt(imageId));
    }

    @Override
    public void clearSelection() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.white));
        gd.setAlpha(0);
        mLinearLayout.setBackgroundDrawable(gd);
    }

    @Override
    public void onClick(View v) {
        GradientDrawable gd = new GradientDrawable();

        gd.setStroke(50, 0xFF00FF00);
        mLinearLayout.setBackgroundDrawable(gd);
        onFragmentSelected(fragmentNumber);
    }


    public interface OnFragmentInteractionListener {
         void onFragmentInteraction(int numberOfFragment);
    }

}
