package com.example.cameraview.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.example.cameraview.ImgDisplayLifecycleObserver
import com.example.cameraview.rxListener.IActivityResultObservable
import com.example.cameraview.rxListener.IActivityResultObserver
import com.example.cameraview.rxListener.IViewActionHandler
import com.example.cameraview.util.IntentUtil

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
<title> </title>
 * TODO:
 * Description:
 * 統一開parent observer, 但因為每個child 用的listener 的類別不同
 * 因此由child 去餵給 parent observer
 * TODO: must to do these, child class
 * @params intentUtil
 * @params
 *</IMPORTANT>
 */
open class UtilImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : de.hdodenhof.circleimageview.CircleImageView(context, attrs, defStyleAttr),
    IViewActionHandler {
    private var activityResultObserver: IActivityResultObserver
    private val TAG = UtilImgView::class.java.simpleName

    /** @required
     * let child to fill
     */
    protected lateinit var intentUtil: IntentUtil<*>
    init {
        activityResultObserver = init()
        ImgDisplayLifecycleObserver.registerViewActionHandler(this)
    }


    //***************** FetchImgUtil observe onActivityResult() **************************************//

    //get Util activityResultObserver
    private fun init(): IActivityResultObserver {
        return intentUtil.observer
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.e(TAG, "${context}")
        activityResultObserver = init()
        if (context is IActivityResultObservable) {
            Log.e(TAG, "${context as IActivityResultObservable}")
            (context as IActivityResultObservable).addObserver(activityResultObserver)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (context is IActivityResultObservable) {
            (context as IActivityResultObservable).removeObserver(activityResultObserver)
        }
        intentUtil.clear()
    }

    //****************** UtilBtn observe Activity/Fragment lifecycle *****************************//
    override fun onStart() {
        Log.e(TAG, "detect the lifecycleowner--- started")
    }

    override fun onResume() {
        Log.e(TAG, "detect the lifecycleowner--- resumed")
    }

    override fun onStop() {
        Log.e(TAG, "detect the lifecycleowner--- stoped")
    }
}