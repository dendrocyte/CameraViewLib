package com.example.cameraview.customView

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.cameraview.ImgDisplayLifecycleObserver
import com.example.cameraview.rxListener.IActivityResultObservable
import com.example.cameraview.rxListener.IActivityResultObserver
import com.example.cameraview.rxListener.IViewActionHandler
import com.example.cameraview.util.IntentUtil
import kotlin.coroutines.coroutineContext

/**
 * Created by luyiling on 2019-07-30
 * Modified by luyiling on 2019-10-22
 *
<title> </title>
 * TODO:
 * Description:
 * 統一開parent observer, 但因為每個child 用的listener 的類別不同
 * 因此由child 去餵給 parent observer
 * TODO: must to do these, child class
 * @required intentUtil
 * @params
 *</IMPORTANT>
 */
open class UtilImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : de.hdodenhof.circleimageview.CircleImageView(context, attrs, defStyleAttr),
    IViewActionHandler {
    private lateinit var activityResultObserver: IActivityResultObserver
    private val TAG = UtilImgView::class.java.simpleName

    /** @required
     * let child to fill
     * parent init()
     * -> child: init() //feed intentUtil instance
     * -> parent : onAttachedToWindow() //bind the observer
     */
    protected var intentUtil: IntentUtil<*>? = null
    set(value) {
        field = value
        field.let {
            activityResultObserver = init()
            //NOTICE: check context is the one who can bind IActivityResultObservable
            Log.d(TAG, "is IActivityResultObservable: " +
                    "${context.getActivityContext() is IActivityResultObservable}")

            if (context.getActivityContext() is IActivityResultObservable) {
                (context.getActivityContext() as IActivityResultObservable).addObserver(activityResultObserver)
            }
        }
    }

    init {
        ImgDisplayLifecycleObserver.getInstance().registerViewActionHandler(this)
    }


    //***************** FetchImgUtil observe onActivityResult() **************************************//

    //get Util activityResultObserver
    private fun init(): IActivityResultObserver {
        return intentUtil!!.observer
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (context is IActivityResultObservable) {
            (context as IActivityResultObservable).removeObserver(activityResultObserver)
        }
        intentUtil?.clear()
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

    /*為了拿到activity的context */
    fun Context.getActivityContext(): Activity = when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivityContext()
        else -> throw IllegalArgumentException("Not an activity context")
    }
}