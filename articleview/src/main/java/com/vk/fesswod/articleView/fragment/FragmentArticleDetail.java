package com.vk.fesswod.articleView.fragment;

import android.app.Activity;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.activity.BaseActivity;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.vk.fesswod.articleView.fragment.FragmentArticleDetail.FragmentArticleStateListener} interface
 * to handle interaction events.
 * Use the {@link FragmentArticleDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArticleDetail extends BaseFragment implements FragmentArticleDisplayListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentArticleStateListener mListener;
    private EditText mNameEditText;
    private EditText mDescEditText;
    private Spinner mGroupSpinner;
    private Switch mPublishSwitch;
    private ImageView mImageView;
    private FloatingActionButton mAddImageFloatingActionButton;
    private Button mSaveButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentArticleDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentArticleDetail newInstance(String param1, String param2) {
        FragmentArticleDetail fragment = new FragmentArticleDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentArticleDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_detail, null);
        mNameEditText = (EditText) v.findViewById(R.id.editTextArticleName);
        mDescEditText = (EditText) v.findViewById(R.id.editTextArticleContent);
        mGroupSpinner = (Spinner) v.findViewById(R.id.spinnerArticleGroup);
        mPublishSwitch = (Switch) v.findViewById(R.id.switchPublished);
        mImageView = (ImageView) v.findViewById(R.id.imageViewThumb);
        mAddImageFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingButtonAddImage);
        mSaveButton = (Button) v.findViewById(R.id.buttonSaveArticle);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onArticleChangesListener(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentArticleStateListener) activity;
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
    public void displayArticle(Article article) {
        mNameEditText.setText(article.getTitle());
        mDescEditText.setText(article.getDesc());
        ArrayAdapter<String> adapter;
        HashSet<String> list = DummyContent.GROUP_SET;
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new ArrayList<String>(list));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupSpinner.setAdapter(adapter);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface FragmentArticleStateListener {
        // TODO: Update argument type and name
        public void onArticleChangesListener(Uri uri);
    }

}