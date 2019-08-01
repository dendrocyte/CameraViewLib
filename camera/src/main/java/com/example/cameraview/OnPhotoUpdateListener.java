package com.example.cameraview;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.RequestBuilder;

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
 * <title> </title>
 * TODO:
 * Description:
 *
 * <IMPORTANT>
 *
 * @params
 * @params </IMPORTANT>
 */
public interface OnPhotoUpdateListener {
    void updatePhoto( RequestBuilder<Drawable> load, int from );
}
