package com.example.cameraview.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.example.cameraview.ImgDisplayLifecycleObserver;
import com.example.cameraview.rxListener.IActivityResultObservable;
import com.example.cameraview.rxListener.IActivityResultObserver;
import com.example.cameraview.rxListener.IViewActionHandler;
import com.example.cameraview.util.IntentUtil;

/**
 * Created by luyiling on 2019-07-31
 * Modified by luyiling on 2019-10-21
 * 不讓init() 在 view create的時候，改為attchWindow的時候啟動
 * 才不會child還沒餵入就呼叫
 *
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
public class UtilImgView1 extends de.hdodenhof.circleimageview.CircleImageView implements IViewActionHandler {
    private IActivityResultObserver activityResultObserver;
    /** @required
     * let child to fill
     */
    IntentUtil intentUtil;
    public UtilImgView1(Context context) {
        super(context);
//        ini();
    }

    public UtilImgView1(Context context, AttributeSet attrs) {
        super(context, attrs);
//        ini();
    }

    public UtilImgView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        ini();
    }

    private void ini(){
        activityResultObserver = init();
        ImgDisplayLifecycleObserver.INSTANCE.registerViewActionHandler(this);
    }


    private IActivityResultObserver init() {
        return intentUtil.getObserver();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("fake", getContext().toString());
        ini();
//        activityResultObserver = init();
        if (getContext() instanceof IActivityResultObservable) {
            Log.e("fake", getContext().toString());
            ((IActivityResultObservable) getContext() ).addObserver(activityResultObserver);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getContext() instanceof IActivityResultObservable) {
            ((IActivityResultObservable) getContext() ).removeObserver(activityResultObserver);
        }

    }



    @Override
    public void onStop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }
}
