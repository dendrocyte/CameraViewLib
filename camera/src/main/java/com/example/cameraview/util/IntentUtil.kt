package com.example.cameraview.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.annotation.GlideModule
import org.jetbrains.anko.alert
import com.bumptech.glide.module.AppGlideModule
import com.example.cameraview.OnFetchListener
import com.example.cameraview.rxListener.IActivityResultObserver
import java.lang.ref.WeakReference


/**
 * Created by luyiling on 2019-10-21
 * 抽象化功能
 * Modified by
 *
<title> </title>
 * TODO: must override the abstract when extends these class
 * TODO: 不可以singleton 這樣就會全部icon 都同步
 * Description:
 *
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
abstract class IntentUtil<T> {

    private val TAG = IntentUtil::class.java.simpleName
    var weakCxt: WeakReference<Context>? = null
    var weakAct: WeakReference<Activity>? = null
    var listener: OnFetchListener<T>? = null
    abstract var observer: IActivityResultObserver
    /**
     * 為了讓frag / activity 各自收到自己的activityResult, 需要匯入自己的context
     */
    fun fetchImg(context: Context, onFetchListener: OnFetchListener<T>){
        Log.d(TAG, "req fetch img...")
        listener = onFetchListener

        weakCxt = WeakReference<Context>(context)
        context.fetchImgAlert()
    }

    fun fetchImg(context: Context, activity: Activity, onFetchListener: OnFetchListener<T>){
        Log.d(TAG, "req fetch img...")
        listener = onFetchListener
        weakAct = WeakReference(activity)
        weakCxt = WeakReference(context)
        context.fetchImgAlert()
    }

    fun clear() {

        weakCxt?.clear()
        listener = null
        removeObserver()
    }

    private fun removeObserver() = apply {  }


    //TODO: 因為正在找出lifecycle owner不對的問題,所以寫的比較醜
    protected abstract fun Context.folderIntent(): Intent
    protected abstract fun Context.imgCaptureIntent() : Intent

    protected fun Context.fetchImgAlert(){
        alert("fetch image from"){
            positiveButton("from camera"){ imgCaptureIntent() }
            negativeButton("from folder"){ folderIntent() }
        }.show()
    }


    @GlideModule class GlideApp : AppGlideModule()
}

