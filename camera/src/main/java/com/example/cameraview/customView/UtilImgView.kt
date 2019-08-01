package com.example.cameraview.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.example.cameraview.ImgDisplayLifecycleObserver
import com.example.cameraview.rxListener.IActivityResultObservable
import com.example.cameraview.rxListener.IActivityResultObserver
import com.example.cameraview.rxListener.IViewActionHandler
import com.example.cameraview.util.ImgIntentUtil

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
<title> </title>
 * TODO:
 * Description:
 *
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
open class UtilImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : de.hdodenhof.circleimageview.CircleImageView(context, attrs, defStyleAttr),
    IViewActionHandler {
    private var activityResultObserver: IActivityResultObserver? = null
    private val TAG = UtilImgView::class.java.simpleName

    init {
        activityResultObserver = init()
        ImgDisplayLifecycleObserver.getInstance().registerViewActionHandler(this)
    }


    //***************** FetchImgUtil observe onActivityResult() **************************************//

    //get Util activityResultObserver
    private fun init(): IActivityResultObserver? {
        return ImgIntentUtil.getObserver()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (context is IActivityResultObservable) {
            activityResultObserver?.let { (context as IActivityResultObservable).addObserver(it) }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (context is IActivityResultObservable) {
            activityResultObserver?.let { (context as IActivityResultObservable).removeObserver(it) }
        }
        ImgIntentUtil.clear()
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