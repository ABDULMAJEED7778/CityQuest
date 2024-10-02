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

public class WeekendTripAdapter extends RecyclerView.Adapter<WeekendTripAdapter.CityViewHolder> {

    private Context context;
    private List<City> cityList;

    // Constructor
    public WeekendTripAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weekend_trip_layout, parent, false); // Inflate your layout file (item_city_card.xml)
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);

        holder.cityNameTextView.setText(city.getCityName());



        Glide.with(context)
                .load(city.getCityImageUrl())  // Load the city image from the URL
                .placeholder(R.drawable.bangalore)  // Placeholder image while loading
                .into(holder.cityImageView);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        ImageView cityImageView;
        TextView cityNameTextView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityImageView = itemView.findViewById(R.id.weekendTrip_cityImage);
            cityNameTextView = itemView.findViewById(R.id.weekendTrip_cityName);

        }
    }
}

