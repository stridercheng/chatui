package com.rance.chatui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.rance.chatui.R;
import com.rance.chatui.enity.MessageInfo;

/**
 * Created by chengz
 *
 * @date 2017/7/31.
 */

public class ChatContextMenu extends RelativePopupWindow {

    private MessageInfo mMessageInfo;
    private Context mContext;
    public ChatContextMenu(Context context, MessageInfo messageInfo) {
        this.mMessageInfo = messageInfo;
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_context_menu, null);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Disable default animation for circular reveal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationStyle(0);
        }

        TextView tvCopy = (TextView) view.findViewById(R.id.tv_copy);
        TextView tvTransit = (TextView) view.findViewById(R.id.tv_transit);

        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });
        tvTransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitContent();
            }
        });
    }

    private void transitContent() {

    }

    private void copyToClipboard() {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Moa", mMessageInfo.getContent());
        cm.setPrimaryClip(data);
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y, boolean fitInScreen) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal(anchor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth()/2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight()/2;

                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(1);
                animator.start();
            }
        });
    }
}
