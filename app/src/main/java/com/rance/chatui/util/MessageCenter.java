package com.rance.chatui.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.rance.chatui.enity.Link;
import com.rance.chatui.enity.MessageInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chengz
 *
 * @date 2017/8/4.
 */

public class MessageCenter {
    private static final String TAG = "MessageCenter";
    //including images and links
    public final static String MIME_TYPE_IMAGE = "image/*";
    public final static String MIME_TYPE_IMAGE_JPG = "image/jpg";
    public final static String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
    public final static String MIME_TYPE_IMAGE_PNG = "image/png";
    public final static String MIME_TYPE_IMAGE_BMP = "image/x-ms-bmp";
    public final static String MIME_TYPE_IMAGE_OTHER = "image/x-adobe-dng";
    public final static String MIME_TYPE_PDF = "application/pdf";
    public final static String MIME_TYPE_XLS = "application/vnd.ms-excel";
    public final static String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public final static String MIME_TYPE_DOC = "application/msword";
    public final static String MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public final static String MIME_TYPE_PPT = "application/vnd.ms-powerpoint";
    public final static String MIME_TYPE_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public final static String MIME_TYPE_TEXT = "text/plain";
    /**
     * process received messages
     * @param bundle
     * @param mimeType
     */
    public static void handleIncoming(Bundle bundle, String mimeType, Activity activity) {
        for (String key : bundle.keySet()) {
            Log.e(TAG, "handleIncomeAction: " + key + " => " + bundle.get(key) + ";");
        }
//
//        Log.e(TAG, "handleIncomeAction: ->" + mimeType);
        Log.e(TAG, "handleIncoming: mimeType->" + mimeType);
        if (mimeType == null) {
            return;
        }
        switch (mimeType) {
            case MIME_TYPE_IMAGE:
            case MIME_TYPE_IMAGE_JPG:
            case MIME_TYPE_IMAGE_JPEG:
            case MIME_TYPE_IMAGE_PNG:
            case MIME_TYPE_IMAGE_BMP:
            case MIME_TYPE_IMAGE_OTHER:
                if (bundle.containsKey("url") && bundle.getString("url") != null
                        && !"" .equals(bundle.getString("url"))) {
                    Log.e(TAG, "handleIncoming: url->" + bundle.getString("url") );
                    // link
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMimeType(mimeType);
                    messageInfo.setFileType(Constants.CHAT_FILE_TYPE_LINK);
                    Link link = new Link();
                    link.setSubject(bundle.getString(Intent.EXTRA_SUBJECT));
                    link.setText(bundle.getString(Intent.EXTRA_TEXT));
                    link.setStream(bundle.get(Intent.EXTRA_STREAM).toString());
                    link.setUrl(bundle.getString("url"));
                    messageInfo.setObject(link);
                    EventBus.getDefault().post(messageInfo);
                } else {
                    // image
                    Log.e(TAG, "handleIncoming: stream ->" + bundle.getString(Intent.EXTRA_STREAM) );
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMimeType(mimeType);
                    messageInfo.setFilepath(bundle.get(Intent.EXTRA_STREAM).toString());
                    messageInfo.setFileType(Constants.CHAT_FILE_TYPE_IMAGE);
                    EventBus.getDefault().post(messageInfo);
                }
                break;
            case MIME_TYPE_PDF:
            case MIME_TYPE_DOC:
            case MIME_TYPE_DOCX:
            case MIME_TYPE_XLS:
            case MIME_TYPE_XLSX:
            case MIME_TYPE_PPT:
            case MIME_TYPE_PPTX:
            case MIME_TYPE_TEXT:
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMimeType(mimeType);
                messageInfo.setFileType(Constants.CHAT_FILE_TYPE_FILE);
                messageInfo.setFilepath(FileUtils.getFileAbsolutePath(activity, (Uri) bundle.get(Intent.EXTRA_STREAM)));
                EventBus.getDefault().post(messageInfo);
                break;
            default:
                break;
        }
    }
}
