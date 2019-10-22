package com.example.cameraview.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.example.cameraview.Constants.*
import com.example.cameraview.R
import com.example.cameraview.rxListener.IActivityResultObserver
import java.io.File


/**
 * Created by luyiling on 2019-06-08
 * Modified by
 *
<title> </title>
 * TODO:
 * Description:
 * @request_code: 標記時代表有要改Constant req_code的話要改這裡
 * @camera: camera改變功能時需要改這裡
 *<IMPORTANT>
 * @params
 * @params
 *</IMPORTANT>
 */
open class FileIntentUtil private constructor() : IntentUtil<Uri>(){

    companion object{
        private var instance : FileIntentUtil? = null
        private var file_path : Uri? = null
        fun instance() : FileIntentUtil =
            instance
                ?: buildInstance().also { instance = it }
        private fun buildInstance() = FileIntentUtil()
    }


    private val TAG = FileIntentUtil::class.java.simpleName
    //    var weakAct: WeakReference<FragmentActivity>? = null


    /**
     * @camera
     * 因為camera 需要儲存檔案
     * 需要修改這裡
     * change the camera intent, -> data = null
     * @required
     * @request_code [FILE_FROM_GALLERY] [REQ_FILE_CAPTURE]
     *
     */
    override var observer: IActivityResultObserver =
        IActivityResultObserver { requestCode, resultCode, data ->
            if(resultCode == Activity.RESULT_OK ){
                when(requestCode){
                    FILE_FROM_GALLERY ->{
                        Log.d(TAG, "get gallery...${data.data}")//uri
                        weakCxt?.get()?.let {
                            listener?.onUpdate(
                                data.data!!,
//                                checkNotNull(data.data).toFile(),
                                FILE_FROM_GALLERY
                            )
                        }
                    }

                    REQ_FILE_CAPTURE ->{
                        Log.d(TAG, "get camera...")//bitmap
                        //data = null
                        weakCxt?.get()?.let {
                            //TODO
                            listener?.onUpdate(
                                file_path,
                                REQ_FILE_CAPTURE
                            )
                        }
                    }
                }
            }
        }


    //TODO: 因為正在找出lifecycle owner不對的問題,所以寫的比較醜
    /**
     * @request_code [FILE_FROM_GALLERY]
     */
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
                        FILE_FROM_GALLERY,
                        null
                    )
                }
            }

    /**
     * TODO
     * override
     * @request_code [REQ_FILE_CAPTURE]
     */
    override fun Context.imgCaptureIntent() =
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                /**
                 * Step1: create file
                 * Step2: get file_path -> 因為我們要傳uri 所以就不額外拿 file://{absolutePath}
                 *                         直接傳這個參數
                 */
                 with(createFile()){
                    //cannot use the file.toUri() ->
                    // * 使用FileProvider解决file:// URI引起的FileUriExposedException
                    // * http://gelitenight.github.io/android/2017/01/29/solve-FileUriExposedException-caused-by-file-uri-with-FileProvider.html
                    weakCxt?.get()?.let {
                        //save file_path to be global
                        file_path = FileProvider.getUriForFile(
                            it,
                            getString(R.string.authorize_file_provider),
                            this
                        )
                    }
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_path ) //to camera
                Log.d("package name:"," ${it.packageName}")
                (this as FragmentActivity).startActivityForResult(takePictureIntent, REQ_FILE_CAPTURE)
            }
        }


    protected fun createFile() : File{
        val nName = "temp"
        /** create temp file
         *  storage dir -> 這裡是files-path的root,
         *                 若要儲存在root下的folder 要自己建, 搭配xml/path裡的path=替䤭在更內層的folder
         * @param nName -> 因為是tempFile 他自己就有變數
         */
        return File.createTempFile(
            "$nName",
            ".jpg",
            weakCxt?.get()?.filesDir //storage dir
        )
    }



}

