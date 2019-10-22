package com.example.cameraview.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.AnyRes



/**
 * Created by luyiling on 2019-10-18
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
object TransformUtil {

     fun getDrawableUri(context: Context,
                        @AnyRes drawableId:Int
     ):Uri = Uri.parse(
         ContentResolver.SCHEME_ANDROID_RESOURCE +
         context.resources.run{
             "://${getResourcePackageName(drawableId)}" +
                     "/${getResourceTypeName(drawableId)}"+
                     "/${getResourceEntryName(drawableId)}"
         }
     )




}