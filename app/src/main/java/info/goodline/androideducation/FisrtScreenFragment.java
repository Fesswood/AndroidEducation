package info.goodline.androideducation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class FisrtScreenFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioButton centerRadioButton;
    private RadioButton centerCropRadioButton;
    private RadioButton fitCenterRadioButton;
    private RadioButton fitEndRadioButton   ;
    private RadioButton fitXYRadioButton    ;
    private RadioButton matrixRadioButton;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private TextView textView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FisrtScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FisrtScreenFragment newInstance(String param1, String param2) {
        FisrtScreenFragment fragment = new FisrtScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FisrtScreenFragment() {
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
        LinearLayout inflateView =(LinearLayout) inflater.inflate(R.layout.fragment_fisrt_screen, container, false);
        imageView= (ImageView) inflateView.findViewById(R.id.imageViewFirst);
        textView= (TextView) inflateView.findViewById(R.id.textView);
        textView.setText(mParam1+" "+mParam2);
        radioGroup = new RadioGroup(getActivity());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        radioGroup.setLayoutParams(new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        initializeBtn();
        setids();
        setListener();

        addToRadiogroup();
        inflateView.addView(radioGroup);

        return inflateView;
    }

    private void initializeBtn() {
        centerRadioButton     = new RadioButton(getActivity());
        centerCropRadioButton = new RadioButton(getActivity());
        fitCenterRadioButton  = new RadioButton(getActivity());
        fitEndRadioButton     = new RadioButton(getActivity());
        fitXYRadioButton      = new RadioButton(getActivity());
        matrixRadioButton     = new RadioButton(getActivity());

        centerRadioButton.setText(" center");
        centerCropRadioButton.setText(" centerCrop");
        fitCenterRadioButton.setText(" fitCenter");
        fitEndRadioButton.setText(" fitEnd");
        fitXYRadioButton.setText(" fitXY");
        matrixRadioButton.setText(" matrix");
    }



    private void setListener() {
        centerRadioButton.setOnClickListener(this);
        centerCropRadioButton.setOnClickListener(this);
        fitCenterRadioButton.setOnClickListener(this);
        fitEndRadioButton.setOnClickListener(this);
        fitXYRadioButton.setOnClickListener(this);
        matrixRadioButton.setOnClickListener(this);
    }

    private void addToRadiogroup() {
        radioGroup.addView(
                centerRadioButton
        );
        radioGroup.addView(
                centerCropRadioButton
        );
        radioGroup.addView(
                fitCenterRadioButton
        );
        radioGroup.addView(
                fitEndRadioButton
        );
        radioGroup.addView(
                fitXYRadioButton
        );
        radioGroup.addView(
                matrixRadioButton
        );
    }
    private void setids() {
        centerRadioButton.setId(R.id.centerRadioBtn);
        centerCropRadioButton.setId(R.id.centerCropRadioBtn);
        fitCenterRadioButton.setId(R.id.fitCenterRadioBtn);
        fitEndRadioButton.setId(R.id.fitEndRadioBtn);
        fitXYRadioButton.setId(R.id.fitXYRadioBtn);
        matrixRadioButton.setId(R.id.matrixRadioBtn);
    }
    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.centerRadioBtn:     if (checked) imageView.setScaleType(ImageView.ScaleType.CENTER); break;
            case R.id.centerCropRadioBtn: if (checked) imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); break;
            case R.id.fitCenterRadioBtn:  if (checked) imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); break;
            case R.id.fitEndRadioBtn:     if (checked) imageView.setScaleType(ImageView.ScaleType.FIT_END); break;
            case R.id.fitXYRadioBtn:      if (checked) imageView.setScaleType(ImageView.ScaleType.FIT_XY); break;
            case R.id.matrixRadioBtn: if (checked) imageView.setScaleType(ImageView.ScaleType.MATRIX); break;
        }

    }
}
