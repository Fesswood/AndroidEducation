package info.goodline.androideducation;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SecondScreenFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private Spinner mSpinner;
    private TextView mTitleTextView;
    private ImageView imageView;



    public static SecondScreenFragment newInstance(String param1, String param2) {
        SecondScreenFragment fragment = new SecondScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondScreenFragment() {
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
        // Inflate the layout for this fragment
        LinearLayout inflateView =(LinearLayout) inflater.inflate(R.layout.fragment_second_screen, container, false);
        imageView= (ImageView) inflateView.findViewById(R.id.imageViewSecond);
        mTitleTextView =(TextView) inflateView.findViewById(R.id.TitleSecondScreenTextView);
        mTitleTextView.setText(mParam1 + " " + mParam2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.scale_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner = new Spinner(getActivity());
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        inflateView.addView(mSpinner , layoutParams);
        return inflateView;
    }


        public void onItemSelected(AdapterView<?> parent, View view,
        int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            String item=(String)parent.getItemAtPosition(pos);
            switch (item){
                case "Center":        imageView.setScaleType(ImageView.ScaleType.CENTER); break;
                case "Center crop":   imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); break;
                case "Center inside": imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); break;
                case "Fit center":    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); break;
                case "Fit start":     imageView.setScaleType(ImageView.ScaleType.FIT_START); break;
                case "Fit end":       imageView.setScaleType(ImageView.ScaleType.FIT_END); break;
                case "Fit XY":        imageView.setScaleType(ImageView.ScaleType.FIT_XY); break;
                case "matrix":        imageView.setScaleType(ImageView.ScaleType.MATRIX); break;
            }
        }

    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(),"Why nothing bro?",Toast.LENGTH_SHORT);
    }
}
