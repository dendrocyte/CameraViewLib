package com.example.cameraview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraview.rxListener.IActivityResultObservable
import com.example.cameraview.rxListener.IActivityResultObserver

/**
 * Created by luyiling on 2019-07-30
 * Modified by
 *
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