package info.goodline.imageswapper.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import info.goodline.imageswapper.R;
import info.goodline.imageswapper.fragment.ImageViewFragment;

public class FragmentSwapperActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, ImageViewFragment.OnFragmentInteractionListener {

    private static final String TAG=FragmentSwapperActivity.class.getSimpleName();
    private Spinner mScaleTypeSpinner;
    private Button  mFinishButton;
    private StringBuilder mResultBuffer;
    private int mSwapCount=0;
    private boolean isFirstClick=true;
    private BaseBehavior fragmentFrom;
    private BaseBehavior fragmentTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_swapper);

        mScaleTypeSpinner = (Spinner) findViewById(R.id.scale_type_spinner);
        mFinishButton = (Button) findViewById(R.id.finish_button);
        mResultBuffer = new StringBuilder();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scale_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScaleTypeSpinner.setAdapter(adapter);

        mScaleTypeSpinner.setOnItemSelectedListener(this);
        mFinishButton.setOnClickListener(this);
        replaceFrames();

    }
    private void replaceFrames(){
            int frameID;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        for (int i=1;i<=6;i++)
        {
            ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(i, i);
            frameID=getResources()
                    .getIdentifier("fragment_container_" +(i), "id", getPackageName());
            fragmentTransaction
                    .add(frameID, imageViewFragment);

            registerBaseBehaviorListener(imageViewFragment);
        }
        fragmentTransaction.commit();

    }

    /**
     *   Change Scale type of imageView switch based on array in /res/values/ScaleType.xml
     * @param parent
     * @param view
     * @param pos  position in ScaleType array
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
          BaseActivity baseActivity= this;
        switch (pos){
            case  0:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.CENTER); break;
            case  1:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.CENTER_CROP); break;
            case  2:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.CENTER_INSIDE); break;
            case  3:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.FIT_CENTER); break;
            case  4:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.FIT_START); break;
            case  5:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.FIT_END); break;
            case  6:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.FIT_XY); break;
            case  7:  baseActivity.sendScaleTypeToAll(ImageView.ScaleType.MATRIX); break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.STRING_RESULT_TAG, mResultBuffer.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(int fragmentNumber) {
        Log.d(TAG,"click to "+fragmentNumber);
        fragmentNumber = fragmentNumber-1;
        if(isFirstClick){
            fragmentFrom = getBaseBehaviorListener(fragmentNumber);
            isFirstClick=false;
        }else{
            fragmentTo = getBaseBehaviorListener(fragmentNumber);
            if(fragmentTo.equals(fragmentFrom)){
                fragmentTo.clearSelection();
                isFirstClick=true;
            }else{
               int swapImageId = fragmentTo.getImageId();
               fragmentTo.setImageId(fragmentFrom.getImageId());
               fragmentFrom.setImageId(swapImageId);
               isFirstClick=true;
               fragmentTo.clearSelection();
               fragmentFrom.clearSelection();
                mResultBuffer.append("\n[")
                             .append(++mSwapCount)
                             .append(",")
                             .append(fragmentFrom.getNumber())
                             .append(",")
                             .append(fragmentTo.getNumber())
                             .append("]");
            }
        }
    }

    @Override
    protected void onDestroy() {
        deleteAllListener();
        super.onPause();
    }
}
