package com.example.cameraview.customView

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.bumptech.glide.Glide
import com.example.cameraview.OnFetchListener
import com.example.cameraview.OnPhotoUpdateListener
import com.example.cameraview.util.ContentUriIntentUtil
import com.example.cameraview.util.PermissionUtil

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
<title> </title>
 * TODO: 專門處理Uri回傳
 * 這個View 會和 xxxIntentUtil 綁在一起
 * Description: 如果要走上傳，要塞這一種view
 *
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
class FetchContentUriImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UtilImgView(context, attrs, defStyleAttr) {
    /**
     * @require if you want to get the photo from lib
     * let user to customize here
     */
    var listener : OnPhotoUpdateListener<Uri>? = null
    val TAG = FetchContentUriImgView::class.java.simpleName

    init {
        //Notice:在客製化View class上會遇到無法使用onClickListener的問題，所以改用touchListener
        Log.d(TAG, "${context}")
        //Notice: 必須要賦予parent class 值
        intentUtil = ContentUriIntentUtil.instance()
        setOnTouchListener { v, event ->
            Log.d(TAG, "on touch: $event")
            if (event.action == MotionEvent.ACTION_DOWN){
                init(context)//NOTICE: context is MainActivity
                true
            }else false
        }

    }

    /**
     * ask permission -> alert (from camera/ from folder) -> get data from onActivityResult
     * -> let UI to fetch the img
     */
    private fun init(context: Context) {
        Log.e(TAG, "init custom view")
        PermissionUtil.getInstance().askCameraPermission(context, object : PermissionUtil.Callback {
            @SuppressLint("MissingPermission")
            override fun onSuccess(granted_permission: List<String>) {
                //permission is ok
                Log.e(TAG, "permission here")
                ContentUriIntentUtil.instance().fetchImg(
                    context,
                    context.getActivityContext(),
                    OnFetchListener { load, from ->
                        Log.d(TAG, "UI:$load")
                        Glide.with(context)
                            .load(load)
                            .thumbnail(0.5f)
                            .dontAnimate()
                            .into(this@FetchContentUriImgView)
                        listener?.updatePhoto(load, from)
                    }
                )
            }
            override fun onFailed(deny_permission: List<String>) { }
        })
    }

}