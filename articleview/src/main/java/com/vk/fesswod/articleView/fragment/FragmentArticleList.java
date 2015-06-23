package com.vk.fesswod.articleView.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.activity.ChangeFilterClauseListener;
import com.vk.fesswod.articleView.activity.DataStateChangeListener;
import com.vk.fesswod.articleView.adapter.AdapterExpandableListArticle;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.api.response.ArticleContainer;

import java.util.ArrayList;

/**
 * A fragment representing a list of Articles.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class FragmentArticleList extends BaseFragment implements  ListItemDeleteListener, AdapterView.OnItemClickListener , FragmentListDisplayListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ExpandableListView.OnChildClickListener {


    private static final String DEBUG_TAG = FragmentArticleList.class.getSimpleName();

    private ListView mListView;
    private ExpandableListView mExpListView;
    private SimpleCursorAdapterListArticle mAdapter;


    private ImageButton mRefreshFilterButton;
    private ImageButton mInitFilterButton;

    private Switch mPublishedFilterSwitch;
    private Switch mMyOwnFilterSwitch;
    private EditText mKeyWordEditText;

    private Spinner mListTypeSpinner;
    private Button mAddArticleButton;
    private ChangeFilterClauseListener mFilterListener;
    private FragmentInteractionListener mListener;
    private DataStateChangeListener mDataListener;
    private AdapterExpandableListArticle mAdapterExp;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentArticleList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendRequestGetArticles();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_list, null);
        mListView = (ListView) v.findViewById(R.id.listViewArticleTitle);
        mListView.setOnItemClickListener(this);
        mExpListView = (ExpandableListView) v.findViewById(R.id.ExpandableListViewArticleCategories);
        mExpListView.setOnChildClickListener(this);
        mAddArticleButton = (Button) v.findViewById(R.id.buttonAddNewArticle);
        mListTypeSpinner = (Spinner) v.findViewById(R.id.spinnerChangeListType);
        mListTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mExpListView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }else if(position == 1){
                    mExpListView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAddArticleButton.setOnClickListener(this);

        mMyOwnFilterSwitch = (Switch) v.findViewById(R.id.switchSortOnlyMy);
        mPublishedFilterSwitch  = (Switch) v.findViewById(R.id.switchSortPublished);

        mKeyWordEditText = (EditText) v.findViewById(R.id.EditTextKeyWord);
        mRefreshFilterButton= (ImageButton) v.findViewById(R.id.imageButtonRefreshFilter);
        mInitFilterButton= (ImageButton) v.findViewById(R.id.imageButtonInitFilter);

        mMyOwnFilterSwitch.setOnCheckedChangeListener(this);
        mPublishedFilterSwitch.setOnCheckedChangeListener(this);
        mRefreshFilterButton.setOnClickListener(this);
        mInitFilterButton.setOnClickListener(this);

        initListTypeSpinner();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInteractionListener) activity;
            mDataListener = (DataStateChangeListener) activity;
            mFilterListener = (ChangeFilterClauseListener) activity;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(id,this.getId());
        }
    }
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(id,this.getId());
        }
        return true;
    }


    private void initListTypeSpinner() {
        ArrayAdapter<String> adapter;
        ArrayList<String> list = new ArrayList<>();
        list.add( getActivity().getResources().getString(R.string.expandable_list));
        list.add( getActivity().getResources().getString(R.string.simple_list));
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListTypeSpinner.setAdapter(adapter);
    }

    @Override
    public void updateListWithItem(long article) {
        Log.d(DEBUG_TAG, "article received" + article);
    }

    @Override
    public void setAdapter(SimpleCursorAdapterListArticle adapter, AdapterExpandableListArticle adapterExp) {
        mAdapter=adapter;
        mListView.setAdapter(mAdapter);
        mAdapter.setOnDeleteListener(this);
        mAdapterExp= adapterExp;
        mExpListView.setAdapter(mAdapterExp);
        mAdapterExp.setOnDeleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAddNewArticle:
                showSnackbar(R.string.snackbar_new_article, R.string.scnakbar_ok, null);
                mListener.onFragmentInteraction(-1, FragmentArticleList.this.getId());
                break;
            case R.id.imageButtonRefreshFilter:
                cancelFilter();
                break;

            case R.id.imageButtonInitFilter:
                filterArticles();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        filterArticles();

    }

    private void cancelFilter() {
        mPublishedFilterSwitch.setChecked(false);
        mMyOwnFilterSwitch.setChecked(false);
        mKeyWordEditText.setText("");
        filterArticles();
    }

    private void filterArticles() {
        mFilterListener.initFilter(
                mMyOwnFilterSwitch.isChecked(),
                mPublishedFilterSwitch.isChecked(),
                mKeyWordEditText.getText());
    }


    @Override
    void receiveArticlesCallback(ArticleContainer articleContainer) {
        ArrayList<Long> serverIds=new ArrayList();
        for (int i=0; i<articleContainer.articles.length;i++){
            mDataListener.insert(articleContainer.articles[i]);
            serverIds.add(articleContainer.articles[i].getId());
        }
        mDataListener.synchronizeDB(serverIds);
    }

    @Override
    protected void showSnackbar(int stringResource, int scnakbarActionString, View.OnClickListener listener) {
        mListener.showSnackBar(stringResource, scnakbarActionString, listener);
    }

    /**
     * CallBack from Adapter, when user click to delete button
     * @param v
     */
    @Override
    public void deleteArticle(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
               .setTitle(R.string.dialog_title)
               .setPositiveButton(R.string.scnakbar_ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                       Object idWrapper = (Object) v.getTag();
                       sendRequestDeleteArticle(Long.parseLong((String) idWrapper));
                   }
               }).setNegativeButton(R.string.snackbar_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        }).show();
    }

    @Override
    void receiveDeleteCallback(long id) {
        Uri uri = Uri.parse(AppContentProvider.CONTENT_URI_ARTICLES + "/"
                + id);
        getActivity().getContentResolver().delete(uri, null, null);
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
    public interface FragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(long id, int fragmentId);
        void showSnackBar(int stringResource, int scnakbarActionString, View.OnClickListener listener);
    }
}
