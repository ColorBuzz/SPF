package com.colorbuzztechgmail.spf;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC01 on 07/04/2018.
 */

public class ListPopupWindowAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<ListPopUpMenuItem> mItemList;
    Context mContext;

    public ListPopupWindowAdapter(Context context, List<ListPopUpMenuItem> itemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
        this.mContext=context;
    }

    public void add(ListPopUpMenuItem item){


        mItemList.add(item);



    }

    public void addAll(List<ListPopUpMenuItem> items){

        mItemList.addAll(items);




    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public ListPopUpMenuItem getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.listpopup_menuitem, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(getItem(position).getTitle());

        if(getItem(position).getImageRes()!=0){

            holder.ivImage.setImageResource(getItem(position).getImageRes());

        }

        if(getItem(position).getImageDrawable()!=null){


            holder.ivImage.setImageDrawable(getItem(position).getImageDrawable());

        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.text);
            ivImage = (ImageView) view.findViewById(R.id.image);
        }
    }
}