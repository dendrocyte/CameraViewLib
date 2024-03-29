package com.example.cameraview.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity

import com.bumptech.glide.RequestBuilder
import com.example.cameraview.Constants
import com.example.cameraview.OnFetchListener
import com.example.cameraview.OnPhotoUpdateListener
import com.example.cameraview.util.ImgIntentUtil
import com.example.cameraview.util.PermissionUtil

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
<title> </title>
 * TODO:
 * Description: 如果要“不”走上傳，要塞這一種view
 *
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
class FetchPhoneImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UtilImgView(context, attrs, defStyleAttr) {
    /**
     * @require if you want to get the photo from lib
     * let user to customize here
     */
    var listener : OnPhotoUpdateListener<RequestBuilder<Drawable>>? = null
    val TAG = FetchPhoneImgView::class.java.simpleName

    init {
        //Notice:在客製化View class上會遇到無法使用onClickListener的問題，所以改用touchListener
        /**
         * NOTICE: 在Kotlin 剛開始的版本 自定義的view : context is MainActivity
         * NOTICE: 現在 自定義的view : context is ContextThemeWrapper
         */
        Log.d(TAG, "${context}")
        //Notice: 必須要賦予parent class 值
        intentUtil = ImgIntentUtil.instance()
        setOnTouchListener { v, event ->
            Log.d(TAG, "on touch: $event")
            if (event.action == MotionEvent.ACTION_DOWN){
                init(context)
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
                ImgIntentUtil.instance().fetchImg(
                    context, //Context
                    context.getActivityContext(), //Activity 為了要startActivityForResult
                    OnFetchListener { load, from ->
                        Log.d(TAG, "UI:$load")
                        load.thumbnail(0.5f)
                            .dontAnimate()
                            .into(this@FetchPhoneImgView)
                        listener?.updatePhoto(load, from)
                    }
                )
            }
            override fun onFailed(deny_permission: List<String>) { }
        })
    }

}