package com.vk.fesswod.articleView.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.api.response.ResponseCategoriesWrapper;
import com.vk.fesswod.articleView.api.utils.Utils;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vk.fesswod.articleView.fragment.FragmentArticleList.FragmentInteractionListener;
import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.COLUMN_ID;


public class FragmentArticleDetail extends BaseFragment implements FragmentArticleDisplayListener, View.OnClickListener, BaseFragment.HttpResponseErrorListener, BaseFragment.HttpResponseListener {

    private static final int PICK_IMAGE_REQUEST = 100;

    private FragmentInteractionListener mListener;

    private EditText mNameEditText;
    private EditText mDescEditText;
    private Spinner mGroupSpinner;
    private Switch mPublishSwitch;
    private ImageView mArticleImageView;

    private FloatingActionButton mAddImageFloatingActionButton;
    private Button mSaveButton;
    private Button mEditArticleButton;
    private Button mViewArticleButton;


    private Article mArticle;
    private boolean isMyOwn = true;
    private boolean mIsNewArticle;
    private Uri mImageUri;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_detail, null);
        mEditArticleButton = (Button) v.findViewById(R.id.buttonEdit);
        mViewArticleButton = (Button) v.findViewById(R.id.buttonView);
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
    public void showArticle(long articleId) {

        if (articleId != -1) {
            fillArticleFromDB(articleId);
            mIsNewArticle = false;
        } else {
            mIsNewArticle = true;
            mArticle = new Article("New Article", "Please add content here!");
        }
        mNameEditText.setText(mArticle.getTitle());
        mDescEditText.setText(mArticle.getDesc());
        mPublishSwitch.setChecked(mArticle.isPublished());
        if (!mArticle.isMyOwn()) {
            ChangeControlEnable(false);
            mEditArticleButton.setVisibility(View.GONE);
        } else {
            ChangeControlEnable(true);
            mEditArticleButton.setVisibility(View.VISIBLE);

        }
        fillCategories();
        showImage();
    }

    private void showImage() {
        if (mArticle.getPhotoContainer() != null
                && !TextUtils.isEmpty(mArticle.getPhotoContainer().getImageUrl())) {
            sendRequestPhoto(mArticleImageView, mArticle.getPhotoContainer().getImageUrl());
        } else {
            mArticleImageView.setImageResource(android.R.color.transparent);
        }
    }

    private void fillArticleFromDB(long articleId) {

        Cursor cursor = getActivity().getContentResolver().query(
                AppContentProvider.CONTENT_URI_ARTICLES,
                null,
                COLUMN_ID + " = ?",
                new String[]{"" + articleId},
                null);
        if (cursor.moveToFirst()) {
            mArticle = Article.fromCursor(cursor);
        }
        cursor.close();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        ArticleCategory group = (ArticleCategory) mGroupSpinner.getSelectedItem();
        mArticle.setArticleGroupId(group.getId());
         //   mArticle.setIsPublished(mPublishSwitch.isChecked());
        mArticle.setIsPublished(true);
        mArticle.setIsMyOwn(isMyOwn);

        if (mIsNewArticle) {
            sendAddArticleRequest(mArticle, this, this, BaseFragment.ADD_ARTICLES);
            mIsNewArticle = false;
        } else {
            sendEditArticleRequest(mArticle, this, this, BaseFragment.EDIT_ARTICLES);
        }

    }

    private void ChangeControlEnable(boolean isEnabled) {
        mNameEditText.setEnabled(isEnabled);
        mDescEditText.setEnabled(isEnabled);
        mGroupSpinner.setEnabled(isEnabled);
        mPublishSwitch.setEnabled(isEnabled);
        mArticleImageView.setEnabled(isEnabled);
        if (isEnabled) {
            changeActiveButtons(true,
                    getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primary_dark),
                    View.VISIBLE);
        } else {
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

            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                // Log.d(TAG, String.valueOf(bitmap));
                mArticleImageView.setImageBitmap(bitmap);
                mArticle.setImageUri(Utils.getPath(getActivity(), mImageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void setCurrentCategory(ArrayAdapter<ArticleCategory> adapter, List<ArticleCategory> list) {
        if (mArticle.getArticleGroupId() == 0) {
            //there are no normal selection
            mGroupSpinner.setSelection(1);

        } else {
            ArticleCategory selectedGroup = new ArticleCategory();
            long articleGroupId = mArticle.getArticleGroupId();
            for (ArticleCategory articleGroup : list) {
                if (articleGroup.getId() == articleGroupId) {
                    selectedGroup = articleGroup;
                    break;
                }
            }
            mGroupSpinner.setSelection(adapter.getPosition(selectedGroup));
        }

    }

    public void fillCategories() {
        Cursor cursor = getActivity().getContentResolver().query(AppContentProvider.CONTENT_URI_GROUPS, null, null, null, null);
        ArrayList<ArticleCategory> listFromCursor = ArticleCategory.createListFromCursor(cursor);

        if (listFromCursor.isEmpty()) {
            sendGetCategoriesRequest(this, this, BaseFragment.GET_CATEGORIES);
        } else {
            initCurrentSpinner(new ResponseCategoriesWrapper(listFromCursor.toArray(new ArticleCategory[]{})));
        }
        cursor.close();
    }
    private void initCurrentSpinner(ResponseCategoriesWrapper responseCategoriesWrapper) {

       ArrayList<ArticleCategory> listFromCursor;

       if(responseCategoriesWrapper != null){
           listFromCursor = new ArrayList<>(Arrays.asList(responseCategoriesWrapper.categories));

       }else {
           Cursor cursor = getActivity().getContentResolver().query(AppContentProvider.CONTENT_URI_GROUPS, null, null, null, null);
           listFromCursor = ArticleCategory.createListFromCursor(cursor);
           cursor.close();
       }

        ArrayAdapter<ArticleCategory> adapter;
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listFromCursor);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupSpinner.setAdapter(adapter);
        setCurrentCategory(adapter, listFromCursor);
    }



    @Override
    protected void showSnackbar(int stringResource, int scnakbarActionString, View.OnClickListener listener) {
        mListener.showSnackBar(stringResource, scnakbarActionString, listener);
    }


    @Override
    public void onError() {
        showSnackbar(R.string.something_goes_wrong, R.string.scnakbar_ok, null);
    }

    @Override
    public void onResponse(long id, int operationId) {
        switch (operationId) {
            case BaseFragment.ADD_ARTICLES:
                mArticle.setId(id);
                showSnackbar(R.string.snackbar_article_save_success_text, -1, null);
                break;
            case BaseFragment.EDIT_ARTICLES:
                showSnackbar(R.string.snackbar_article_save_success_text, -1, null);
                break;

            case BaseFragment.GET_CATEGORIES:
                initCurrentSpinner(null);
                break;
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
