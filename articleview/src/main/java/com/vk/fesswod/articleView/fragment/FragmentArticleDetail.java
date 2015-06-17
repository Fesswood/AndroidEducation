package com.vk.fesswod.articleView.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;
import com.vk.fesswod.articleView.fragment.dummy.DummyContent;

import java.io.IOException;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.vk.fesswod.articleView.fragment.FragmentArticleDetail.FragmentArticleStateListener} interface
 * to handle interaction events.
 * Use the {@link FragmentArticleDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArticleDetail extends BaseFragment implements FragmentArticleDisplayListener, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentArticleList.FragmentInteractionListener mListener;
    private EditText mNameEditText;
    private EditText mDescEditText;
    private Spinner  mGroupSpinner;
    private Switch   mPublishSwitch;
    private ImageView mArticleImageView;
    private FloatingActionButton mAddImageFloatingActionButton;
    private Button mSaveButton;
    private Button mEditArticleButton;
    private Button mViewArticleButton;
    private LinearLayout mControlslayout;
    private boolean isMyOwn;
    private Article mArticle;

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
        mEditArticleButton = (Button) v.findViewById(R.id.buttonEdit);
        mViewArticleButton = (Button) v.findViewById(R.id.buttonView);
        mControlslayout = (LinearLayout) v.findViewById(R.id.LinearLayout_editLayout);
        mNameEditText = (EditText) v.findViewById(R.id.editTextArticleName);
        mDescEditText = (EditText) v.findViewById(R.id.editTextArticleContent);
        mGroupSpinner = (Spinner) v.findViewById(R.id.spinnerArticleGroup);
        mPublishSwitch = (Switch) v.findViewById(R.id.switchPublished);
        mArticleImageView = (ImageView) v.findViewById(R.id.imageViewThumb);
        mAddImageFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingButtonAddImage);
        mSaveButton = (Button) v.findViewById(R.id.buttonSaveArticle);

        mViewArticleButton.setOnClickListener(this);
        mEditArticleButton.setOnClickListener(this);
        mAddImageFloatingActionButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentArticleList.FragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void displayArticle(Article article) {
        mArticle=article;
        mNameEditText.setText(article.getTitle());
        mDescEditText.setText(article.getDesc());
        initGroupSpinner();
        mPublishSwitch.setChecked(article.isPublished());
    }

    private void initGroupSpinner() {
        ArrayAdapter<ArticleGroup> adapter;
        ArrayList<ArticleGroup> list = new ArrayList<>(DummyContent.GROUP_SET) ;
        adapter = new ArrayAdapter<ArticleGroup>(getActivity(),
                android.R.layout.simple_spinner_item, list );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupSpinner.setAdapter(adapter);
        mGroupSpinner.setSelection(list.indexOf(mArticle.getArticleGroup()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonView:
                ChangeControlEnable(false);
                break;
            case R.id.buttonEdit:
                ChangeControlEnable(true);
                break;
            case R.id.floatingButtonAddImage:
                showImageGallery();
                break;
            case R.id.buttonSaveArticle:

                saveArticle();
                break;
        }
    }

    private void saveArticle() {
        mArticle.setTitle(mNameEditText.getText().toString());
        mArticle.setDesc(mDescEditText.getText().toString());
        ArticleGroup group =(ArticleGroup) mGroupSpinner.getSelectedItem();
        mArticle.setArticleGroup(group);
        mArticle.setIsMyOwn(isMyOwn);
        mArticle.setUpdateAtTimeStamp(System.currentTimeMillis() / 1000L);
        mListener.onFragmentInteraction(mArticle,this.getId());
    }

    private void ChangeControlEnable(boolean isEnabled) {
        mNameEditText.setEnabled(isEnabled);
        mDescEditText.setEnabled(isEnabled);
        mGroupSpinner.setEnabled(isEnabled);
        mPublishSwitch.setEnabled(isEnabled);
        mArticleImageView.setEnabled(isEnabled);
        if(isEnabled){
            changeActiveButtons(true,
                    getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primary_dark),
                    View.VISIBLE);
        }else{
            changeActiveButtons(false,
                    getResources().getColor(R.color.primary_dark),
                    getResources().getColor(R.color.primary),
                    View.GONE);
        }
    }

    private void changeActiveButtons(boolean isEnabled, int color, int color2, int visible) {
        mViewArticleButton.setEnabled(isEnabled);
        mViewArticleButton.setBackgroundColor(color);
        mEditArticleButton.setEnabled(!isEnabled);
        mEditArticleButton.setBackgroundColor(color2);
        mSaveButton.setVisibility(visible);
    }

    private void showImageGallery() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                mArticleImageView.setImageBitmap(bitmap);
                mArticle.setImageUri(getPath(getActivity(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        public void onArticleChangesListener(Article article);
    }

}
