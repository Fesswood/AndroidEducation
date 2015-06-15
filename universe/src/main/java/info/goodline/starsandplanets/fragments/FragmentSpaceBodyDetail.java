package info.goodline.starsandplanets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;


import java.util.ArrayList;

import info.goodline.starsandplanets.R;
import info.goodline.starsandplanets.activity.ViewPagerCallback;
import info.goodline.starsandplanets.activity.ActivitySpaceBodyDetail;
import info.goodline.starsandplanets.activity.ActivitySpaceBodyList;
import info.goodline.starsandplanets.data.MyWebViewClient;
import info.goodline.starsandplanets.data.SpaceBody;

/**
 * A fragment representing a single spaceBody detail screen.
 * This fragment is either contained in a {@link ActivitySpaceBodyList}
 * in two-pane mode (on tablets) or a {@link ActivitySpaceBodyDetail}
 * on handsets.
 */
public class FragmentSpaceBodyDetail extends Fragment  implements ViewPagerCallback {
    /**
     * The fragment argument representing the url of space body
     */
    public static final String URL_ID = "space_body_url";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String  Fav_ID="item_fav_state";

    /**
     * The SpaceBody content this fragment is presenting.
     */
    private ArrayList<SpaceBody> mSpaceBodiesList;
    private SpaceBody mCurrentItem;
    /**
     * Url of selected Spacebody
     */
    private String mCurrentUrl;
    private ImageView mImageView;
    private boolean mFavoriteState;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentSpaceBodyDetail() {
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public static FragmentSpaceBodyDetail newInstance(SpaceBody spaceBody) {
        FragmentSpaceBodyDetail fragment = new FragmentSpaceBodyDetail();
        Bundle args = new Bundle();
        args.putString(URL_ID, spaceBody.getLink());
        args.putBoolean(Fav_ID,spaceBody.isFavorite());
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentUrl = getArguments().getString(URL_ID);
            mFavoriteState = getArguments().getBoolean(Fav_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spacebody_detail, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.imgFavorite);
        mImageView.setImageResource(R.drawable.drawable_list_item_bg);
        if(mFavoriteState){
            mImageView.setImageLevel(1);
        }else {
            mImageView.setImageLevel(0);
        }
        // Show the dummy content as text in a TextView.
        if (mCurrentUrl != null) {
            WebView webView=((WebView) rootView.findViewById(R.id.spacebody_detail));
            MyWebViewClient viewClient=new MyWebViewClient();
            webView.setWebViewClient(viewClient);
            webView.loadUrl(mCurrentUrl);
        }

        return rootView;
    }

    @Override
    public void setCurrentItem(int pos) {
        mCurrentItem=mSpaceBodiesList.get(pos);
    }
}
