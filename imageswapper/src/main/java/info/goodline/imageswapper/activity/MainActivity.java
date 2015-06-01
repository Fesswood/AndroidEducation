package info.goodline.imageswapper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.goodline.imageswapper.R;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    static final int ACTION_LIST_REQUEST = 0;
    static final String STRING_RESULT_TAG = "MainActivity.ResultString";

    Button toFragmentScreenButton;
    TextView mLogTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toFragmentScreenButton = (Button) findViewById(R.id.fragment_screen_button);
        mLogTextView = (TextView) findViewById(R.id.log_textview);
        toFragmentScreenButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivityForResult(
                new Intent(this,FragmentSwapperActivity.class),
                ACTION_LIST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_LIST_REQUEST) {
            if (resultCode == RESULT_OK) {
                    String receivedData=data.getStringExtra(STRING_RESULT_TAG);
                    mLogTextView.setText(receivedData);
            }
        }

    }
}
