package com.rance.chatui.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.rance.chatui.base.BaseFragment;

/**
 * Created by chengz
 *
 * @date 2017/8/1.
 */

public class PhotoUtils {
    private static final String TAG = "PhotoUtils";

    /**
     * 拍照方法
     * @param baseFragment
     * @param imageUri
     * @param requestCodeCamera
     */
    public static void takePicture(BaseFragment baseFragment, Uri imageUri, int requestCodeCamera) {
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.e(TAG, "takePicture: ->" + requestCodeCamera);
        baseFragment.startActivityForResult(intentCamera, requestCodeCamera);
    }

    /**
     * 裁剪图片
     * @param activity
     * @param orgUri
     * @param desUri
     * @param aspectX
     * @param aspectY
     * @param width
     * @param height
     * @param requestCode
     */
    public static void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int aspectX,
                                    int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * open picture
     * @param activity
     * @param requestCode
     */
    public static void openPic(Activity activity, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }
}
