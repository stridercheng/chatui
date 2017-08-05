package com.rance.chatui.adapter.holder;

import android.content.Context;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rance.chatui.R;
import com.rance.chatui.adapter.ChatAdapter;
import com.rance.chatui.enity.IMContact;
import com.rance.chatui.enity.Link;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.util.Constants;
import com.rance.chatui.util.FileUtils;
import com.rance.chatui.util.Utils;
import com.rance.chatui.widget.BubbleImageView;
import com.rance.chatui.widget.BubbleLinearLayout;
import com.rance.chatui.widget.GifTextView;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatAcceptViewHolder extends BaseViewHolder<MessageInfo> {
    private static final String TAG = "ChatAcceptViewHolder";
    TextView chatItemDate;
    ImageView chatItemHeader;
    GifTextView chatItemContentText;
    BubbleImageView chatItemContentImage;
    ImageView chatItemVoice;
    BubbleLinearLayout chatItemLayoutContent;
    TextView chatItemVoiceTime;
    BubbleLinearLayout chatItemLayoutFile;
    ImageView ivFileType;
    TextView tvFileName;
    TextView tvFileSize;

    BubbleLinearLayout chatItemLayoutContact;
    TextView tvContactSurname;
    TextView tvContactName;
    TextView tvContactPhone;

    BubbleLinearLayout chatItemLayoutLink;
    TextView tvLinkSubject;
    TextView tvLinkText;
    ImageView ivLinkPicture;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private RelativeLayout.LayoutParams layoutParams;
    private Context mContext;

    public ChatAcceptViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_accept, parent, false));
        findViewByIds(itemView);
        setItemLongClick();
        setItemClick();
        this.mContext = parent.getContext();
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    private void findViewByIds(View itemView) {
        chatItemDate = (TextView) itemView.findViewById(R.id.chat_item_date);
        chatItemHeader = (ImageView) itemView.findViewById(R.id.chat_item_header);
        chatItemContentText = (GifTextView) itemView.findViewById(R.id.chat_item_content_text);
        chatItemContentImage = (BubbleImageView) itemView.findViewById(R.id.chat_item_content_image);
        chatItemVoice = (ImageView) itemView.findViewById(R.id.chat_item_voice);
        chatItemLayoutContent = (BubbleLinearLayout) itemView.findViewById(R.id.chat_item_layout_content);
        chatItemVoiceTime = (TextView) itemView.findViewById(R.id.chat_item_voice_time);
        chatItemLayoutFile = (BubbleLinearLayout) itemView.findViewById(R.id.chat_item_layout_file);
        ivFileType = (ImageView) itemView.findViewById(R.id.iv_file_type);
        tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
        tvFileSize = (TextView) itemView.findViewById(R.id.tv_file_size);
        chatItemLayoutContact = (BubbleLinearLayout) itemView.findViewById(R.id.chat_item_layout_contact);
        tvContactSurname = (TextView) itemView.findViewById(R.id.tv_contact_surname);
        tvContactPhone = (TextView) itemView.findViewById(R.id.tv_contact_phone);
        chatItemLayoutLink = (BubbleLinearLayout) itemView.findViewById(R.id.chat_item_layout_link);
        tvLinkSubject = (TextView) itemView.findViewById(R.id.tv_link_subject);
        tvLinkText = (TextView) itemView.findViewById(R.id.tv_link_text);
        ivLinkPicture = (ImageView) itemView.findViewById(R.id.iv_link_picture);
    }

    @Override
    public void setData(MessageInfo data) {
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "");
        Glide.with(mContext).load(data.getHeader()).into(chatItemHeader);
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick((Integer) itemView.getTag());
            }
        });
        switch (data.getFileType()) {
            case Constants.CHAT_FILE_TYPE_TEXT:
                chatItemContentText.setSpanText(handler, data.getContent(), true);
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemLayoutContact.setVisibility(View.GONE);

                TextPaint paint = chatItemContentText.getPaint();
                // 计算textview在屏幕上占多宽
                int len = (int) paint.measureText(chatItemContentText.getText().toString().trim());
                if (len < Utils.dp2px(mContext, 200)){
                    layoutParams.width = len + Utils.dp2px(mContext, 30);
                } else {
                    layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                }
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case Constants.CHAT_FILE_TYPE_IMAGE:
                chatItemVoice.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.VISIBLE);
                chatItemLayoutContact.setVisibility(View.GONE);

                Glide.with(mContext).load(data.getFilepath()).into(chatItemContentImage);
                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, (Integer) itemView.getTag());
                    }
                });
                layoutParams.width = Utils.dp2px(mContext, 120);
                layoutParams.height = Utils.dp2px(mContext, 48);
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case Constants.CHAT_FILE_TYPE_VOICE:
                chatItemVoice.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.VISIBLE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemLayoutContact.setVisibility(View.GONE);

                chatItemVoiceTime.setText(Utils.formatTime(data.getVoiceTime()));
                chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onVoiceClick(chatItemVoice, (Integer) itemView.getTag());
                    }
                });
                layoutParams.width = Utils.dp2px(mContext, 120);
                layoutParams.height = Utils.dp2px(mContext, 48);
                chatItemLayoutContent.setLayoutParams(layoutParams);
                break;
            case Constants.CHAT_FILE_TYPE_FILE:
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemLayoutContact.setVisibility(View.GONE);

//                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemLayoutFile.setVisibility(View.VISIBLE);
                tvFileName.setText(FileUtils.getFileName(data.getFilepath()));
                try {
                    tvFileSize.setText(FileUtils.getFileSize(data.getFilepath()));
                    switch (FileUtils.getExtensionName(data.getFilepath())) {
                        case "doc":
                        case "docx":
                            ivFileType.setImageResource(R.mipmap.icon_file_word);
                            break;
                        case "ppt":
                        case "pptx":
                            ivFileType.setImageResource(R.mipmap.icon_file_ppt);
                            break;
                        case "xls":
                        case "xlsx":
                            ivFileType.setImageResource(R.mipmap.icon_file_excel);
                            break;
                        case "pdf":
                            ivFileType.setImageResource(R.mipmap.icon_file_pdf);
                            break;
                        default:
                            ivFileType.setImageResource(R.mipmap.icon_file_other);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.CHAT_FILE_TYPE_CONTACT:
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemLayoutFile.setVisibility(View.GONE);

                chatItemLayoutContact.setVisibility(View.VISIBLE);

                IMContact imContact = (IMContact) data.getObject();
                tvContactSurname.setText(imContact.getSurname());
                tvContactName.setText(imContact.getName());
                tvContactPhone.setText(imContact.getPhonenumber());
                break;
            case Constants.CHAT_FILE_TYPE_LINK:
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemLayoutFile.setVisibility(View.GONE);
                chatItemLayoutContact.setVisibility(View.GONE);

                chatItemLayoutLink.setVisibility(View.VISIBLE);
                Link link = (Link) data.getObject();

                tvLinkSubject.setText(link.getSubject());
                tvLinkText.setText(link.getText());
                Glide.with(mContext).load(link.getStream()).into(ivLinkPicture);
                break;
        }
    }

    public void setItemLongClick() {
        chatItemContentImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onLongClickImage(v, (Integer) itemView.getTag());
                return true;
            }
        });
        chatItemContentText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onLongClickText(v, (Integer) itemView.getTag());
                return true;
            }
        });
        chatItemLayoutContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onLongClickItem(v, (Integer) itemView.getTag());
                return true;
            }
        });
        chatItemLayoutFile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onLongClickFile(v, (Integer) itemView.getTag());
                return true;
            }
        });
    }

    public void setItemClick() {
        chatItemContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onImageClick(chatItemContentImage, (Integer) itemView.getTag());
            }
        });

        chatItemLayoutFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onVoiceClick(chatItemVoice, (Integer) itemView.getTag());
            }
        });

        chatItemLayoutFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onFileClick(v, (Integer) itemView.getTag());
            }
        });

        chatItemLayoutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onLinkClick(v, (Integer) itemView.getTag());
            }
        });
    }
}
