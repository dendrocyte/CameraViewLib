package com.example.cameraview

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.cameraview.rxListener.IViewActionHandler

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
class ImgDisplayLifecycleObserver : LifecycleObserver {
    private val TAG = ImgDisplayLifecycleObserver::class.java.simpleName
    private var actionHandler: IViewActionHandler? = null
    private var lifecycle: Lifecycle? = null

    companion object{
        private val imgDisplayLifecycleObserver: ImgDisplayLifecycleObserver by lazy {
            ImgDisplayLifecycleObserver()
        }
        fun getInstance(): ImgDisplayLifecycleObserver = imgDisplayLifecycleObserver
    }


    fun registerViewActionHandler(observable: IViewActionHandler) {
        this.actionHandler = observable
    }

    fun registerLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun start() {
        Log.e(TAG, "lifeowner start")
        if (actionHandler != null) actionHandler!!.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun resume() {
        Log.e(TAG, "lifeowner resume")
        if (actionHandler != null) actionHandler!!.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun stop() {
        Log.e(TAG, "lifeowner stop")
        if (actionHandler != null) actionHandler!!.onStop()
    }

}