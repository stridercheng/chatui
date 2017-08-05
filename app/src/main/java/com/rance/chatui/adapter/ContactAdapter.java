package com.rance.chatui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rance.chatui.R;
import com.rance.chatui.enity.IMContact;

import java.util.List;


/**
 * Created by chengz
 *
 * @date 2017/8/3.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<IMContact> imContactList;
    private OnContactClickListener mOnContactClickListener;

    public ContactAdapter(List<IMContact> imContactList) {
        this.imContactList = imContactList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IMContact imContact = imContactList.get(position);
        holder.tvName.setText(imContact.getName());
        holder.tvPhone.setText(imContact.getPhonenumber());

        holder.itemView.setTag(imContact);
    }

    @Override
    public int getItemCount() {
        return imContactList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnContactClickListener == null) {
            return;
        }

        mOnContactClickListener.onContactClick(v, (IMContact) v.getTag());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPhone;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }

    public interface OnContactClickListener {
        void onContactClick(View view, IMContact imContact);
    }

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.mOnContactClickListener = onContactClickListener;
    }

}
