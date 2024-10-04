package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.City;

import java.util.List;

public class ChangeCityLocationAdapter extends RecyclerView.Adapter<ChangeCityLocationAdapter.CityViewHolder> {

    private Context context;
    private List<String> cityList;
    private OnCityClickListener cityClickListener;

    // Constructor
    public ChangeCityLocationAdapter(Context context, List<String> cityList,OnCityClickListener cityClickListener) {
        this.context = context;
        this.cityList = cityList;
        this.cityClickListener = cityClickListener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_name_element, parent, false); // Inflate your layout file (item_city_card.xml)
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String city = cityList.get(position);

        holder.cityNameTextView.setText(city);
        holder.itemView.setOnClickListener(v -> {
            if (cityClickListener != null) {
                cityClickListener.onCityClick(city); // Notify the click
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {


        TextView cityNameTextView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityNameTextView = itemView.findViewById(R.id.cityName_changeLocation);

        }
    }
    // Define the click listener interface
    public interface OnCityClickListener {
        void onCityClick(String cityName);
    }
}


