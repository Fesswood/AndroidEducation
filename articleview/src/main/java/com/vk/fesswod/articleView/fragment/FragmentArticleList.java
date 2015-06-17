package com.vk.fesswod.articleView.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;


import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.adapter.SimpleCursorAdapterListArticle;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.fragment.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class FragmentArticleList extends BaseFragment implements  AdapterView.OnItemClickListener , FragmentListDisplayListener{

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
    private ArrayAdapter<Article> mAdapter;
    private Spinner mListTypeSpinner;

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

        // TODO: Change Adapter to display your content
        mAdapter= new SimpleCursorAdapterListArticle(getActivity(), DummyContent.ITEMS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article_list, null);
        mListView = (ListView) v.findViewById(R.id.listViewArticleTitle);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListTypeSpinner = (Spinner) v.findViewById(R.id.spinnerChangeListType);
        initListTypeSpinner();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInteractionListener) activity;
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
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position),this.getId());
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
    public void updateListWithItem(Article article) {
        Log.d(DEBUG_TAG,"article received"+ article);
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
        public void onFragmentInteraction(Article id, int fragmentId);
    }

}
