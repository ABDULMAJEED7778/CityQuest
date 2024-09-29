package com.example.cityquest.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cityquest.R;
import com.example.cityquest.model.Companion;

import java.util.List;

public class CompanionAdapter extends BaseAdapter {
    private List<Companion> companionList;
    private Context mContext;
    private int selectedPosition = -1; // Track the selected position

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

    public String getSelectedCompanion() {
        if (selectedPosition != -1) {
            return companionList.get(selectedPosition).getName();
        }
        return null; // No selection
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

        // Update the checkmark visibility based on the selected position
        holder.checkMarkImageView.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

        // Set click listener
        convertView.setOnClickListener(v -> {
            selectedPosition = position; // Update selected position
            notifyDataSetChanged(); // Refresh the adapter
        });

        return convertView;
    }

    static class ViewHolder {
        TextView companionName;
        ImageView companionImage, checkMarkImageView;
    }
}
