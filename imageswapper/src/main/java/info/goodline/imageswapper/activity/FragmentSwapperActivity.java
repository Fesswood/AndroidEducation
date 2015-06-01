package info.goodline.imageswapper.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import info.goodline.imageswapper.R;

public class FragmentSwapperActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    Spinner mScaleTypeSpinner;
    Button  mFinishButton;
    ArrayList<FrameLayout> mFrameList;
    StringBuffer mResultBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_swapper);

        mScaleTypeSpinner = (Spinner) findViewById(R.id.scale_type_spinner);
        mFinishButton = (Button) findViewById(R.id.finish_button);
        mFrameList = new ArrayList();
        mResultBuffer = new StringBuffer();
        initializeFrameList(mFrameList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scale_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScaleTypeSpinner.setAdapter(adapter);
        mScaleTypeSpinner.setOnItemSelectedListener(this);

    }

    private void initializeFrameList(ArrayList mFrameList) {
        mFrameList.set(R.id.fragment_container_1, findViewById(R.id.fragment_container_1));
        mFrameList.set(R.id.fragment_container_2, findViewById(R.id.fragment_container_2));
        mFrameList.set(R.id.fragment_container_3, findViewById(R.id.fragment_container_3));
        mFrameList.set(R.id.fragment_container_4, findViewById(R.id.fragment_container_4));
        mFrameList.set(R.id.fragment_container_5, findViewById(R.id.fragment_container_5));
        mFrameList.set(R.id.fragment_container_6, findViewById(R.id.fragment_container_6));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
