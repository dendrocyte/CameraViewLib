package com.example.cameraview.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.annotation.GlideModule
import com.example.cameraview.Constants
import com.example.cameraview.Constants.IMAGE_FROM_GALLERY
import com.example.cameraview.Constants.REQ_IMAGE_CAPTURE
import com.example.cameraview.rxListener.IActivityResultObserver
import org.jetbrains.anko.alert
import java.lang.ref.WeakReference
import com.bumptech.glide.module.AppGlideModule



/**
 * Created by luyiling on 2019-06-08
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
object ImgIntentUtil {
    var weakAct: WeakReference<FragmentActivity>? = null
    var listener : OnFetchListener? = null
    private val TAG = ImgIntentUtil::class.java.simpleName


    fun fetchImg(activity: FragmentActivity, onFetchListener: OnFetchListener){
        Log.d(TAG, "req fetch img...")
        listener = onFetchListener
        weakAct = WeakReference<FragmentActivity>(activity)
        activity.fetchImgAlert()
    }

    fun clear() {
        weakAct?.clear()
        listener = null
        removeObserver()
    }

    fun getObserver(): IActivityResultObserver? = observer
    private fun removeObserver() = apply {  }


    private var observer: IActivityResultObserver? =
        IActivityResultObserver { requestCode, resultCode, data ->
            Log.d(TAG, "request code: $requestCode, result code: $resultCode")
            if(resultCode == Activity.RESULT_OK && data != null){
                when(requestCode){
                    IMAGE_FROM_GALLERY ->{
                        Log.d(TAG, "get gallery...")
                        weakAct?.get()?.let {
                            listener?.onUpdate(Glide.with(it).load(data.data), IMAGE_FROM_GALLERY)
                        }
                    }

                    REQ_IMAGE_CAPTURE->{
                        Log.d(TAG, "get camera...")
                        weakAct?.get()?.let {
                            listener?.onUpdate(
                                Glide.with(it).load(data.extras?.get("data")), REQ_IMAGE_CAPTURE
                            )
                        }
                    }
                }
            }
        }



    private fun FragmentActivity.folderIntent()=
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .also { folderIntent ->
                folderIntent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(folderIntent , "Pick any photo"),
                    IMAGE_FROM_GALLERY
                )
            }

    private fun FragmentActivity.imgCaptureIntent() =
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE)
            }
        }

    private fun FragmentActivity.fetchImgAlert(){
        alert("fetch image from"){
            positiveButton("from camera"){ imgCaptureIntent() }
            negativeButton("from folder"){ folderIntent() }
        }.show()
    }

    interface OnFetchListener{
        fun onUpdate(load: RequestBuilder<Drawable> , from: Int)
    }

    @GlideModule class GlideApp : AppGlideModule()
}

