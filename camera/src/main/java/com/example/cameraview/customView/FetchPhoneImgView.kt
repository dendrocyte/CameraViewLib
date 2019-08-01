package com.example.cameraview.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.RequestBuilder
import com.example.cameraview.OnPhotoUpdateListener
import com.example.cameraview.util.ImgIntentUtil
import com.example.cameraview.util.PermissionUtil

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
class FetchPhoneImgView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UtilImgView(context, attrs, defStyleAttr) {
    /**
     * @require if you want to get the photo from lib
     * let user to customize here
     */

    var listener : OnPhotoUpdateListener? = null
    val TAG = FetchPhoneImgView::class.java.simpleName

    init {
        //Notice:在客製化View class上會遇到無法使用onClickListener的問題，所以改用touchListener
        setOnTouchListener { v, event ->
            Log.d(TAG, "on touch: $event")
            if (event.action == MotionEvent.ACTION_DOWN){
                Log.d(TAG, "on touch")
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
                ImgIntentUtil.fetchImg(context as FragmentActivity, object : ImgIntentUtil.OnFetchListener {
                    override fun onUpdate(load: RequestBuilder<Drawable>, from: Int) {
                        Log.d(TAG, "UI:$load")
                        load.thumbnail(0.5f)
                            .dontAnimate()
                            .into(this@FetchPhoneImgView)
                        listener?.updatePhoto(load, from )
                    }
                })
            }
            override fun onFailed(deny_permission: List<String>) { }
        })
    }





}