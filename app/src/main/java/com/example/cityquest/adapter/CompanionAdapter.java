package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.Companion;

import java.util.List;

public class CompanionAdapter extends BaseAdapter {
    private List<Companion> companionList;

    private Context mContext;

    public CompanionAdapter(Context context, List<Companion> companionList) {
        this.mContext = context;
        this.companionList = companionList;
    }



    @Override
    public int getCount() {
        return companionList.size();
    }

    @Override
    public Object getItem(int position) {
        return companionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.companion_item, parent, false);
            holder = new ViewHolder();
            holder.companionName = convertView.findViewById(R.id.companion_item_name);
            holder.companionImage = convertView.findViewById(R.id.companion_item_image_view);
            holder.checkMarkImageView = convertView.findViewById(R.id.checkmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Companion companion = companionList.get(position);

        holder.companionName.setText(companion.getName());


        holder.companionImage.setImageResource(companion.getImgId());


        // Set click listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkMarkImageView.getVisibility() == View.VISIBLE) {
                    holder.checkMarkImageView.setVisibility(View.INVISIBLE);
                } else {
                    holder.checkMarkImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView companionName;
        ImageView companionImage,checkMarkImageView;
    }


}
