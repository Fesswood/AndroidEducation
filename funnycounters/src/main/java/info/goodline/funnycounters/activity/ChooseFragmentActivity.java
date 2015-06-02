package info.goodline.funnycounters.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import info.goodline.funnycounters.R;

public class ChooseFragmentActivity extends ActionBarActivity implements View.OnClickListener {

    private Button  mFragment1Button;
    private Button  mFragment2Button;
    private Button  mFragment3Button;
    private Button  mFragment4Button;
    private Button  mFragment5Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_fragment);
        mFragment1Button = (Button) findViewById(R.id.fragment1_button);
        mFragment2Button = (Button) findViewById(R.id.fragment2_button);
        mFragment3Button = (Button) findViewById(R.id.fragment3_button);
        mFragment4Button = (Button) findViewById(R.id.fragment4_button);
        mFragment5Button = (Button) findViewById(R.id.fragment5_button);
        mFragment1Button.setOnClickListener(this);
        mFragment2Button.setOnClickListener(this);
        mFragment3Button.setOnClickListener(this);
        mFragment4Button.setOnClickListener(this);
        mFragment5Button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()){
            case R.id.fragment1_button:
                intent.putExtra(MainActivity.FRAGMENT_ID_TAG,1);
                break;
            case R.id.fragment2_button:
                intent.putExtra(MainActivity.FRAGMENT_ID_TAG, 2);
                break;
            case R.id.fragment3_button:
                intent.putExtra(MainActivity.FRAGMENT_ID_TAG,3);
                break;
            case R.id.fragment4_button:
                intent.putExtra(MainActivity.FRAGMENT_ID_TAG, 4);
                break;
            case R.id.fragment5_button:
                intent.putExtra(MainActivity.FRAGMENT_ID_TAG, 5);
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
