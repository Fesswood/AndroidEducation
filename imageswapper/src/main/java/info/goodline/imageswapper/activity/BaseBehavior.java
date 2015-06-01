package info.goodline.imageswapper.activity;

import android.widget.ImageView;

/**
 * Created by sergeyb on 01.06.15.
 */
public interface BaseBehavior {
     void changeScaleType(ImageView.ScaleType scaleType);
     int getImageId();
     int getNumber();
     void setImageId(int imageId);
     void clearSelection();
}
