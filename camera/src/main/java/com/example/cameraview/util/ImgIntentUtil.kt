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
import com.example.cameraview.Constants.IMAGE_FROM_GALLERY
import com.example.cameraview.Constants.REQ_IMAGE_CAPTURE
import com.example.cameraview.OnFetchListener
import com.example.cameraview.rxListener.IActivityResultObserver
import java.lang.ref.WeakReference


/**
 * Created by luyiling on 2019-06-08
 * Modified by luyiling on 2019-10-21
 * 抽象化
 *
<title> </title>
 * TODO: 回傳RequestBuilder<Drawable> 的function params
 * Description:
 *
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
open class ImgIntentUtil private constructor() : IntentUtil<RequestBuilder<Drawable>>(){

    companion object{
        private var instance : ImgIntentUtil? = null
        fun instance() : ImgIntentUtil =
            instance
                ?: buildInstance().also { instance = it }
        private fun buildInstance() = ImgIntentUtil()
    }


    private val TAG = ImgIntentUtil::class.java.simpleName


    override var observer: IActivityResultObserver =
        IActivityResultObserver { requestCode, resultCode, data ->
            if(resultCode == Activity.RESULT_OK && data != null){
                when(requestCode){
                    IMAGE_FROM_GALLERY ->{
                        Log.d(TAG, "get gallery...${data.data}")//uri
                        weakCxt?.get()?.let {
                            listener?.onUpdate(
                                Glide.with(it).load(data.data),
//                                checkNotNull(data.data).toFile(),
                                IMAGE_FROM_GALLERY
                            )
                        }
                    }

                    REQ_IMAGE_CAPTURE->{
                        Log.d(TAG, "get camera...")//bitmap
                        weakCxt?.get()?.let {
                            listener?.onUpdate(
                                Glide.with(it).load(data.extras?.get("data")),
                                REQ_IMAGE_CAPTURE
                            )
                        }
                    }
                }
            }
        }


    //TODO: 因為正在找出lifecycle owner不對的問題,所以寫的比較醜
    override fun Context.folderIntent()=
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .also { folderIntent ->
                folderIntent.type = "image/*"
//                startActivityForResult(
//                    weakCxt?.get() as Activity,
//                Intent.createChooser(folderIntent , "Pick any photo"),
//                IMAGE_FROM_GALLERY,
//                null
//                )
                when{
//                    this is Fragment -> startActivityForResult(
//                        Intent.createChooser(folderIntent , "Pick any photo"),
//                        IMAGE_FROM_GALLERY,
//                        null
//                    )
                    this is FragmentActivity -> startActivityForResult(
                        Intent.createChooser(folderIntent, "Pick any photo"),
                        IMAGE_FROM_GALLERY,
                        null
                    )
                }
            }

    override fun Context.imgCaptureIntent() =
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                Log.d("package name:"," ${it.packageName}")
                (this as FragmentActivity).startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE)
            }
        }

}

