package com.louisgeek.voskdemo.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2020/4/18.
 */
public class MyBaseAdapter extends BaseAdapter {
    List<MessageModel> mMessageModelList = new ArrayList<>();

    @Override
    public int getCount() {
        return mMessageModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            holder = new Holder();
            holder.id_tv = convertView.findViewById(R.id.id_tv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MessageModel messageModel = mMessageModelList.get(position);
        holder.id_tv.setText(messageModel.title);
        return convertView;
    }

    class Holder {
        TextView id_tv;
    }

    public void refreshData(List<MessageModel> messageModelList) {
        mMessageModelList.clear();
        mMessageModelList.addAll(messageModelList);
        this.notifyDataSetChanged();
    }

    public void addData(MessageModel messageModel) {
        mMessageModelList.add(messageModel);
        this.notifyDataSetChanged();
    }
}
