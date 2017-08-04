package com.rance.chatui.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rance.chatui.adapter.holder.BaseViewHolder;
import com.rance.chatui.adapter.holder.ChatAcceptViewHolder;
import com.rance.chatui.adapter.holder.ChatSendViewHolder;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Rance on 2016/11/29 10:46
 * 邮箱：rance935@163.com
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private onItemClickListener onItemClickListener;
    public Handler handler;
    private List<MessageInfo> messageInfoList;

    public ChatAdapter(List<MessageInfo> messageInfoList) {
        handler = new Handler();
        this.messageInfoList = messageInfoList;
    }

//    @Override
//    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//        BaseViewHolder viewHolder = null;
//        switch (viewType) {
//            case Constants.CHAT_ITEM_TYPE_LEFT:
//                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
//                break;
//            case Constants.CHAT_ITEM_TYPE_RIGHT:
//                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);
//                break;
//        }
//        return viewHolder;
//    }
//
//    @Override
//    public int getViewType(int position) {
//        return getAllData().get(position).getType();
//    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(messageInfoList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return messageInfoList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        if (messageInfoList == null) {
            return 0;
        } else {
            return messageInfoList.size();
        }
    }

    public void addAll(List<MessageInfo> messageInfos) {
        if (messageInfoList == null) {
            messageInfoList = messageInfos;
        } else {
            messageInfoList.addAll(messageInfos);
        }

        notifyDataSetChanged();
    }

    public void add(MessageInfo messageInfo) {
        if (messageInfoList == null) {
            messageInfoList = new ArrayList<>();
        }

        messageInfoList.add(messageInfo);

        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, int position);

        void onVoiceClick(ImageView imageView, int position);

        void onFileClick(View view, int position);

        void onLinkClick(View view, int position);

        void onLongClickImage(View view, int position);

        void onLongClickText(View view, int position);

        void onLongClickItem(View view, int position);

        void onLongClickFile(View view, int position);

        void onLongClickLink(View view, int position);
    }
}
