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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.activity.DataStateChangeListener;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.Article;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class FragmentArticleList extends BaseFragment implements  SimpleCursorAdapterListArticle.ListItemDeleteListener, AdapterView.OnItemClickListener , FragmentListDisplayListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEBUG_TAG = FragmentArticleList.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mListView;
    private ExpandableListView mExpListView;
    private FragmentInteractionListener mListener;
    private DataStateChangeListener mDataListener;
    private SimpleCursorAdapterListArticle mAdapter;
    private Spinner mListTypeSpinner;
    private Button mButtonAddarticle;

    // TODO: Rename and change types of parameters
    public static FragmentArticleList newInstance(String param1, String param2) {
        FragmentArticleList fragment = new FragmentArticleList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentArticleList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
            sendRequestGetArticles();

        // TODO: Change Adapter to display your content

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_list, null);
        mListView = (ListView) v.findViewById(R.id.listViewArticleTitle);
        mListView.setOnItemClickListener(this);
        mButtonAddarticle = (Button) v.findViewById(R.id.buttonAddNewArticle);
        mListTypeSpinner = (Spinner) v.findViewById(R.id.spinnerChangeListType);
        mButtonAddarticle.setOnClickListener(this);
        initListTypeSpinner();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInteractionListener) activity;
            mDataListener = (DataStateChangeListener) activity;
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
        Log.d(DEBUG_TAG,"article received"+ article);
    }

    @Override
    public void setAdapter(SimpleCursorAdapterListArticle adapter) {
        mAdapter=adapter;
        mListView.setAdapter(mAdapter);
        mAdapter.setOnDeleteListener(this);
    }

    @Override
    public void onClick(View v) {
        showSnackbar(R.string.snackbar_new_article, R.string.scnakbar_ok, null);
        mListener.onFragmentInteraction(-1, FragmentArticleList.this.getId());
    }

    @Override
    void receiveArticlesCallback(Article.ArticleContainer articleContainer) {
        for (int i=0; i<articleContainer.articles.length;i++){
            mDataListener.insert(articleContainer.articles[i]);
        }
    }

    @Override
    protected void showSnackbar(int stringResource, int scnakbarActionString, View.OnClickListener listener) {
        mListener.showSnackBar(stringResource, scnakbarActionString, listener);
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
        public void onFragmentInteraction(long id, int fragmentId);
        void showSnackBar(int stringResource, int scnakbarActionString, View.OnClickListener listener);
    }
    /**
     * CallBack from Adapter, when user click to delete button
     * @param v
     */
    @Override
    public void deleteArticle(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.scnakbar_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Object idWrapper =(Object) v.getTag();
                    sendRequestDeleteArticle(Long.parseLong((String) idWrapper));
            }
        });
        builder.setNegativeButton(R.string.snackbar_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    void receiveDeleteCallback(long id) {
        Uri uri = Uri.parse(AppContentProvider.CONTENT_URI_ARTICLES + "/"
                + id);
        getActivity().getContentResolver().delete(uri, null, null);
    }
}
