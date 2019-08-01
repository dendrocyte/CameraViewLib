package com.example.cameraview.rxListener;

import android.content.Intent;

/**
 * Created by luyiling on 2019/4/4
 * <p>
 * TODO:
 *
 * <IMPORTANT></IMPORTANT>
 */
public interface IActivityResultObserver {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
