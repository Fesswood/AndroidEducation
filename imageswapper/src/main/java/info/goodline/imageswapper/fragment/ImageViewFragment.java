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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewFragment extends Fragment implements BaseBehavior, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IMAGE_ID = "param1";
    private static final String FRAGMENT_NUMBER = "param2";

    // TODO: Rename and change types of parameters
    private int imageID;
    private int fragmentNumber;
    private OnFragmentInteractionListener mListener;
    private ImageView mImageView;
    private LinearLayout mlinearLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageId  id of image resource.
     * @return A new instance of fragment ImageViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageViewFragment newInstance(int imageId ,int fragmentNumber) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_ID, imageId);
        args.putInt(FRAGMENT_NUMBER, fragmentNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewFragment() {
        // Required empty public constructor
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
        mlinearLayout = new LinearLayout(getActivity());
        mlinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mlinearLayout.setPadding(10, 10, 10, 10);
        mImageView = new ImageView(getActivity());
        mImageView.setImageResource(getIdentFromInt(imageID));
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mImageView.setOnClickListener(this);
        mlinearLayout.setOnClickListener(this);
        mlinearLayout.addView(mImageView);
        return mlinearLayout;
    }

    private int getIdentFromInt(int imageID) {
        return getResources().getIdentifier("img_"+imageID,"drawable", getActivity().getPackageName());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFragmentSelected(int fragmentNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragmentNumber);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
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
        mImageView.setImageResource(getIdentFromInt(imageId));
    }

    @Override
    public void clearSelection() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.white));
        gd.setAlpha(0);
        mlinearLayout.setBackgroundDrawable(gd);
    }

    @Override
    public void onClick(View v) {
        GradientDrawable gd = new GradientDrawable();

        gd.setStroke(50, 0xFF00FF00);
        mlinearLayout.setBackgroundDrawable(gd);
        onFragmentSelected(fragmentNumber);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int numberOfFragment);
    }

}
