package com.example.cameraview.rxListener;

/**
 * Created by luyiling on 2019/4/4
 * <p>
 * TODO:
 *
 * <IMPORTANT></IMPORTANT>
 */
public interface IActivityResultObservable {
    void addObserver(IActivityResultObserver activityResultObserver);
    void removeObserver(IActivityResultObserver activityResultObserver);
}
