package com.example.cameraview.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.StringRes;

import com.example.cameraview.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

/**
 * Created by luyiling on 2019/7/30
 * every time click here will confirm here
 * <p>
 * TODO: ref. AndPermission lib: https://yanzhenjie.com/AndPermission
 *
 * <IMPORTANT>require storage and camera permission</IMPORTANT>
 */
public class PermissionUtil {
    private static String TAG = PermissionUtil.class.getSimpleName();
    static PermissionUtil util;
    public static PermissionUtil getInstance(){
        if (util == null) util = new PermissionUtil();
        return util;
    }

    /**
     * @param context Activity: Activity.this; Fragment: getContext()
     */
    public void askCameraPermission(final Context context, final Callback callback){
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.CAMERA, Permission.Group.STORAGE)
                .rationale(new Rtl<List<String>>(
                        R.string.camera_permission_title,
                        R.string.camera_permission_msg))
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // TODO what to do. callback to notice
                        Log.e(TAG, "permission success");
                        callback.onSuccess(permissions);

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.e(TAG, "permission failed");
                        callback.onFailed(permissions);
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            // These permissions are always rejected by the user.
                            // go setting!
                            Log.e(TAG,"permission failed with never ask again");
                        }else {
                            Log.e(TAG,"permission failed without never ask again");
                        }
                    }
        }).start();
    }

    /* dangerous permission: when deny first, second time will show here*/
    static class Rtl<T> implements Rationale<T> {
        @StringRes
        int title, msg;
        public Rtl(@StringRes int title, @StringRes int msg) {
            this.title = title;
            this.msg = msg;
        }

        @Override
        public void showRationale(Context context, T permissions, final RequestExecutor executor) {
            // Here should display a Dialog to ask the user whether to continue.
            Log.e(TAG, "rantionale");
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(title)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When the user to continue the request:
                            executor.execute();
                        }
                    })
                    .setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When the user interrupts the request:
                            executor.cancel();
                        }
                    }).show();

        }
    }

    public interface Callback{
        void onSuccess(List<String> granted_permission);
        void onFailed(List<String> deny_permission);
    }
}
