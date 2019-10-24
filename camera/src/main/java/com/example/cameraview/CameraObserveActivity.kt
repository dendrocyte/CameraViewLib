package com.example.cameraview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraview.rxListener.IActivityResultObservable
import com.example.cameraview.rxListener.IActivityResultObserver

/**
 * Created by luyiling on 2019-07-30
 * Modified by luyiling on 2019-10-24
 * 為了偵測是否有加入observer
<title> </title>
 * TODO:
 * Description:
 *
 *<IMPORTANT>
 * @params contain lifecycle observer and onActivityResultObservable
 * @params
 *</IMPORTANT>
 */
open class CameraObserveActivity : AppCompatActivity(), IActivityResultObservable {

    var observableList: MutableList<IActivityResultObserver> = mutableListOf()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("activity","onActivityResult")
        Log.d("activity","size:"+observableList.size)
        for (observer in observableList) {
            observer.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImgDisplayLifecycleObserver.getInstance().registerLifecycle(lifecycle)
    }


    override fun addObserver(activityResultObserver: IActivityResultObserver) {
        observableList.add(activityResultObserver)
    }

    override fun removeObserver(activityResultObserver: IActivityResultObserver) {
        observableList.remove(activityResultObserver)
    }
}