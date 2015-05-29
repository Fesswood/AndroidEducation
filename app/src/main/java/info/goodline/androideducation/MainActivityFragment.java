package info.goodline.androideducation;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements Button.OnClickListener {

    Button toFirstScreenBtn;
    Button toSecondScreenBtn;
    FrameLayout fragmentFrame;


    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        toFirstScreenBtn  = (Button)  view.findViewById(R.id.FirstScreenBtn);
        toSecondScreenBtn = (Button) view.findViewById(R.id.SecondScreenBtn);
        fragmentFrame = (FrameLayout) view.findViewById(R.id.fragment_frame);


        toFirstScreenBtn.setOnClickListener(this);
        toSecondScreenBtn.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
        switch (v.getId()){
            case  R.id.FirstScreenBtn:
                ft.replace(R.id.fragment_frame,  FisrtScreenFragment.newInstance("Jack","wut?"));
                break;
            case  R.id.SecondScreenBtn:
                ft.replace(R.id.fragment_frame, SecondScreenFragment.newInstance("Yuup", "We need go deeper!"));
                break;
        }
        ft.commit();
    }
}
