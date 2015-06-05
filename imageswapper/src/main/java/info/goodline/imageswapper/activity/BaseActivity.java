package info.goodline.imageswapper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by sergeyb on 01.06.15.
 */
public class BaseActivity extends Activity {

    protected ArrayList<BaseBehavior> fragmentActions;


    public void registerBaseBehaviorListener(BaseBehavior baseFragmentBehavior){
        fragmentActions.add(baseFragmentBehavior);
    }
    public BaseBehavior getBaseBehaviorListener(int fragmentNumber){
        return fragmentActions.get(fragmentNumber);
    }
    public void deleteAllListener(){
        fragmentActions.clear();
        fragmentActions=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActions=new ArrayList<>();
    }

    public void sendScaleTypeToAll(ImageView.ScaleType scaleType){
        for (BaseBehavior behavior: fragmentActions){
            behavior.changeScaleType(scaleType);
        }
    }
}
